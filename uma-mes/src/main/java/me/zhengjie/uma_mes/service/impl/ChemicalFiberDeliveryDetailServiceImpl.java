package me.zhengjie.uma_mes.service.impl;

import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNoteMapper;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryDetailServiceImpl implements ChemicalFiberDeliveryDetailService {

    private final ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository;

    private final ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public ChemicalFiberDeliveryDetailServiceImpl(ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository,
                                                  ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper,
                                                  ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.chemicalFiberDeliveryDetailRepository = chemicalFiberDeliveryDetailRepository;
        this.chemicalFiberDeliveryDetailMapper = chemicalFiberDeliveryDetailMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }
        Page<ChemicalFiberDeliveryDetail> page = chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);

        List<ChemicalFiberDeliveryDetailVo> chemicalFiberDeliveryDetailVos = new ArrayList<>();
        List<ChemicalFiberDeliveryDetail> chemicalFiberDeliveryDetails = page.getContent();
        for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryDetails) {
            ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.getOne(chemicalFiberDeliveryDetail.getDeliveryNoteId());
            ChemicalFiberDeliveryDetailVo chemicalFiberDeliveryDetailVo = new ChemicalFiberDeliveryDetailVo();
            ObjectTransfer.transValue(chemicalFiberDeliveryNote, chemicalFiberDeliveryDetailVo);
            ObjectTransfer.transValue(chemicalFiberDeliveryDetail, chemicalFiberDeliveryDetailVo);
            chemicalFiberDeliveryDetailVo.setDetailTotalCost(chemicalFiberDeliveryDetail.getTotalCost());
            chemicalFiberDeliveryDetailVos.add(chemicalFiberDeliveryDetailVo);
        }

        return PageUtil.toPage(new PageImpl<>(chemicalFiberDeliveryDetailVos, pageable, page.getTotalElements()));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberDeliveryDetailDTO> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria){
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryDetailDTO findById(Integer id) {
        ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = chemicalFiberDeliveryDetailRepository.findById(id).orElseGet(ChemicalFiberDeliveryDetail::new);
        ValidationUtil.isNull(chemicalFiberDeliveryDetail.getId(),"ChemicalFiberDeliveryDetail","id",id);
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetail);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryDetailDTO create(ChemicalFiberDeliveryDetail resources) {
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.save(resources));
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryDetail resources) {
        ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = chemicalFiberDeliveryDetailRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryDetail::new);
        ValidationUtil.isNull( chemicalFiberDeliveryDetail.getId(),"ChemicalFiberDeliveryDetail","id",resources.getId());
        chemicalFiberDeliveryDetail.copy(resources);
        chemicalFiberDeliveryDetailRepository.save(chemicalFiberDeliveryDetail);

        // 修改出货单成本，总金额
        ChemicalFiberDeliveryNoteQueryCriteria chemicalFiberDeliveryNoteQueryCriteria = new ChemicalFiberDeliveryNoteQueryCriteria();
        chemicalFiberDeliveryNoteQueryCriteria.setScanNumber(chemicalFiberDeliveryDetail.getScanNumber());
        List<ChemicalFiberDeliveryNote> chemicalFiberDeliveryNotes = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,chemicalFiberDeliveryNoteQueryCriteria,criteriaBuilder));
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNotes.get(0);

        BigDecimal tempTotalCost = new BigDecimal(0.0);
        BigDecimal tempTotalPrice = new BigDecimal(0.0);
        ChemicalFiberDeliveryDetailQueryCriteria criteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        criteria.setScanNumber(chemicalFiberDeliveryDetail.getScanNumber());
        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
            tempTotalCost = tempTotalCost.add(chemicalFiberDeliveryDetailDTO.getTotalCost() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetailDTO.getTotalCost());
            tempTotalPrice = tempTotalPrice.add(chemicalFiberDeliveryDetailDTO.getTotalPrice() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetailDTO.getTotalPrice());
        }
        chemicalFiberDeliveryNote.setTotalCost(tempTotalCost);
        chemicalFiberDeliveryNote.setTotalPrice(tempTotalPrice);
        chemicalFiberDeliveryNoteRepository.save(chemicalFiberDeliveryNote);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberDeliveryDetailRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberDeliveryDetailDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品id", chemicalFiberDeliveryDetail.getProdId());
            map.put("产品型号", chemicalFiberDeliveryDetail.getProdModel());
            map.put("产品名称", chemicalFiberDeliveryDetail.getProdName());
            map.put("产品色号", chemicalFiberDeliveryDetail.getProdColor());
            map.put("产品纤度", chemicalFiberDeliveryDetail.getProdFineness());
            map.put("成本单价", chemicalFiberDeliveryDetail.getCost());
            map.put("销售单价", chemicalFiberDeliveryDetail.getSellingPrice());
            map.put("单位", chemicalFiberDeliveryDetail.getUnit());
            map.put("总成本", chemicalFiberDeliveryDetail.getTotalCost());
            map.put("总金额", chemicalFiberDeliveryDetail.getTotalPrice());
            map.put("总件数", chemicalFiberDeliveryDetail.getTotalBag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Result getSalesReportSummaries(ChemicalFiberDeliveryDetailQueryCriteria criteria) {
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }

        List<ChemicalFiberDeliveryDetail> chemicalFiberDeliveryDetails = chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));

        // 包数
        Integer totalNumber = 0;

        // 个数
        Integer totalBag = 0;

        // 净重
        BigDecimal totalWeight = new BigDecimal(0.0);

        // 成本
        BigDecimal totalCost = new BigDecimal(0.0);

        // 销售
        BigDecimal totalPrice = new BigDecimal(0.0);


        for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryDetails) {
            totalNumber = totalNumber + (chemicalFiberDeliveryDetail.getTotalBag() == null ? 0 : chemicalFiberDeliveryDetail.getTotalBag());
            totalBag = totalBag + (chemicalFiberDeliveryDetail.getTotalNumber() == null ? 0 : chemicalFiberDeliveryDetail.getTotalNumber());
            totalWeight = totalWeight.add((chemicalFiberDeliveryDetail.getTotalWeight() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalWeight()));
            totalCost = totalCost.add((chemicalFiberDeliveryDetail.getTotalCost() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalCost()));
            totalPrice = totalPrice.add((chemicalFiberDeliveryDetail.getTotalPrice() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalPrice()));
        }

        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add(totalNumber);
        list.add(totalBag);
        list.add(totalWeight);
        list.add("");
        list.add(totalCost);
        list.add("");
        list.add(totalPrice);
        return Result.success(list);
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryStatementDetailsAll(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }

        Page<ChemicalFiberDeliveryDetail> page = chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryDetailMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateList(List<ChemicalFiberDeliveryDetail> resources) {
        for (ChemicalFiberDeliveryDetail Detail : resources) {
            ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = chemicalFiberDeliveryDetailRepository.findById(Detail.getId()).orElseGet(ChemicalFiberDeliveryDetail::new);
            ValidationUtil.isNull( chemicalFiberDeliveryDetail.getId(),"ChemicalFiberDeliveryDetail","id",Detail.getId());
            chemicalFiberDeliveryDetail.copy(Detail);
            chemicalFiberDeliveryDetailRepository.save(chemicalFiberDeliveryDetail);

            // 修改出货单成本，总金额
            ChemicalFiberDeliveryNoteQueryCriteria chemicalFiberDeliveryNoteQueryCriteria = new ChemicalFiberDeliveryNoteQueryCriteria();
            chemicalFiberDeliveryNoteQueryCriteria.setScanNumber(chemicalFiberDeliveryDetail.getScanNumber());
            List<ChemicalFiberDeliveryNote> chemicalFiberDeliveryNotes = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,chemicalFiberDeliveryNoteQueryCriteria,criteriaBuilder));
            ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNotes.get(0);

            BigDecimal tempTotalCost = new BigDecimal(0.0);
            BigDecimal tempTotalPrice = new BigDecimal(0.0);
            ChemicalFiberDeliveryDetailQueryCriteria criteria = new ChemicalFiberDeliveryDetailQueryCriteria();
            criteria.setScanNumber(chemicalFiberDeliveryDetail.getScanNumber());
            List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
            for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
                tempTotalCost = tempTotalCost.add(chemicalFiberDeliveryDetailDTO.getTotalCost() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetailDTO.getTotalCost());
                tempTotalPrice = tempTotalPrice.add(chemicalFiberDeliveryDetailDTO.getTotalPrice() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetailDTO.getTotalPrice());
            }
            chemicalFiberDeliveryNote.setTotalCost(tempTotalCost);
            chemicalFiberDeliveryNote.setTotalPrice(tempTotalPrice);
            chemicalFiberDeliveryNoteRepository.save(chemicalFiberDeliveryNote);
        }

    }
}