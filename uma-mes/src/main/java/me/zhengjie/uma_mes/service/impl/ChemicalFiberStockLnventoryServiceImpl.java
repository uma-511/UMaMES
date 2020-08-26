package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventory;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockLnventoryDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockLnventoryRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberStockLnventoryService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockLnventoryMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@CacheConfig(cacheNames = "chemicalFiberStockLnventory")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberStockLnventoryServiceImpl implements ChemicalFiberStockLnventoryService {

    final ChemicalFiberStockLnventoryRepository chemicalFiberStockLnventoryRepository;

    final ChemicalFiberStockLnventoryMapper chemicalFiberStockLnventoryMapper;

    @Autowired
    private ChemicalFiberStockLnventoryDetailRepository chemicalFiberStockLnventoryDetailRepository;

    @Autowired
    private ChemicalFiberStockRepository chemicalFiberStockRepository;


    public ChemicalFiberStockLnventoryServiceImpl(ChemicalFiberStockLnventoryRepository chemicalFiberStockLnventoryRepository,
                                                  ChemicalFiberStockLnventoryMapper chemicalFiberStockLnventoryMapper) {
        this.chemicalFiberStockLnventoryRepository = chemicalFiberStockLnventoryRepository;
        this.chemicalFiberStockLnventoryMapper = chemicalFiberStockLnventoryMapper;
    }

    public Map<String, Object> queryAll(ChemicalFiberStockLnventoryQueryCriteria criteria, Pageable pageable) {
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Integer> invalidList = new ArrayList<>();
        invalidList.add(0);
        if (null != criteria.getQueryWithInvalid() && criteria.getQueryWithInvalid())
        {
            invalidList.add(1);
        }
        criteria.setInvalidList(invalidList);
        Page<ChemicalFiberStockLnventory> page = chemicalFiberStockLnventoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberStockLnventoryMapper::toDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberStockLnventoryDTO create() {
        ChemicalFiberStockLnventory resources = new ChemicalFiberStockLnventory();
        resources.setCreateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        resources.setLnventoryNumber(getLnventoryNumber());
        resources.setLnventoryName(getLnventoryName());
        resources.setInvalid(0);
        resources.setLnventoryUser(chemicalFiberStockLnventoryRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setLnventoryStatus(1);
        return chemicalFiberStockLnventoryMapper.toDto(chemicalFiberStockLnventoryRepository.save(resources));
    }


    public String getLnventoryNumber () {
        String scanNumber;
        String type = "PD";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        Integer currenCount = chemicalFiberStockLnventoryRepository.getCurrenNoteCount(year+"-"+month);
        String scanNumberMax = chemicalFiberStockLnventoryRepository.getScanNumberMax();
        Integer numberMax = 0;
        if (scanNumberMax != null) {
            String scan[] = scanNumberMax.split("", 7);
            numberMax = Integer.valueOf(scan[6]);
        }
        if (currenCount == 0) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer number = numberMax+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
    }

    public Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        return map;
    }

    public String getLnventoryName() {
        Map<String, Object> timeMap = monthTimeInMillis();
        String month = timeMap.get("month").toString();
        String lnventoryName = month + "月盘点单";
        return lnventoryName;

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberStockLnventory lnventory = chemicalFiberStockLnventoryRepository.findById(id).orElseGet(ChemicalFiberStockLnventory::new);
        if (lnventory.getLnventoryStatus() == 1) {
            chemicalFiberStockLnventoryDetailRepository.deleteByLnventoryId(id);
            // chemicalFiberStockLnventoryRepository.deleteById(id);
            lnventory.setInvalid(1);
            chemicalFiberStockLnventoryRepository.save(lnventory);
        } else {
            List<ChemicalFiberStockLnventoryDetail> detaList = chemicalFiberStockLnventoryDetailRepository.getDetaList(id);
            for (ChemicalFiberStockLnventoryDetail date : detaList) {
                ChemicalFiberStock stockadd = chemicalFiberStockRepository.findById(date.getStockId()).orElseGet(ChemicalFiberStock::new);
                if (stockadd.getProdId() != null) {
                    stockadd.setTotalNumber(date.getProdNumber());
                    chemicalFiberStockRepository.save(stockadd);
                }
            }
            lnventory.setInvalid(1);
            chemicalFiberStockLnventoryRepository.save(lnventory);

        }
    }

}
