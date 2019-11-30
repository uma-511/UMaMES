package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ViewScanRecord;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ViewScanRecordRepository;
import me.zhengjie.uma_mes.service.ViewScanRecordService;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ViewScanRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Service
@CacheConfig(cacheNames = "viewScanRecord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ViewScanRecordServiceImpl implements ViewScanRecordService {

    private final ViewScanRecordRepository viewScanRecordRepository;

    private final ViewScanRecordMapper viewScanRecordMapper;

    public ViewScanRecordServiceImpl(ViewScanRecordRepository viewScanRecordRepository, ViewScanRecordMapper viewScanRecordMapper) {
        this.viewScanRecordRepository = viewScanRecordRepository;
        this.viewScanRecordMapper = viewScanRecordMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ViewScanRecordQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        Page<ViewScanRecord> page = viewScanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(viewScanRecordMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ViewScanRecordDTO> queryAll(ViewScanRecordQueryCriteria criteria){
        return viewScanRecordMapper.toDto(viewScanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ViewScanRecordDTO findById(Integer id) {
        ViewScanRecord viewScanRecord = viewScanRecordRepository.findById(id).orElseGet(ViewScanRecord::new);
        ValidationUtil.isNull(viewScanRecord.getId(),"ViewScanRecord","id",id);
        return viewScanRecordMapper.toDto(viewScanRecord);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ViewScanRecordDTO create(ViewScanRecord resources) {
        return viewScanRecordMapper.toDto(viewScanRecordRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ViewScanRecord resources) {
        ViewScanRecord viewScanRecord = viewScanRecordRepository.findById(resources.getId()).orElseGet(ViewScanRecord::new);
        ValidationUtil.isNull( viewScanRecord.getId(),"ViewScanRecord","id",resources.getId());
        viewScanRecord.copy(resources);
        viewScanRecordRepository.save(viewScanRecord);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        viewScanRecordRepository.deleteById(id);
    }


    @Override
    public void download(List<ViewScanRecordDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ViewScanRecordDTO viewScanRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" id",  viewScanRecord.getId());
            map.put("扫描时间", viewScanRecord.getScanTime());
            map.put("扫描单号", viewScanRecord.getScanNumber());
            map.put("扫描类型（入库：RK 出库：SH 退库：TK 退货：TH）", viewScanRecord.getType());
            map.put("标签编号（条码号）", viewScanRecord.getLabelNumber());
            map.put("实际每袋个数", viewScanRecord.getFactPerBagNumber());
            map.put("净重", viewScanRecord.getNetWeight());
            map.put("皮重", viewScanRecord.getTare());
            map.put("毛重", viewScanRecord.getGrossWeight());
            map.put("班次", viewScanRecord.getShifts());
            map.put("包装员", viewScanRecord.getPacker());
            map.put("订单编号", viewScanRecord.getNumber());
            map.put("客户编号", viewScanRecord.getCustomerCode());
            map.put("产品名称", viewScanRecord.getProdName());
            map.put("产品颜色", viewScanRecord.getProdColor());
            map.put("产品纤度", viewScanRecord.getProdFineness());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}