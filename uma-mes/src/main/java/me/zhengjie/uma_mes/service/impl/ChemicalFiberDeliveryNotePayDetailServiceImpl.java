package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNotePayDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNotePayDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNotePayDetailMapper;
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
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryNotePayDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryNotePayDetailServiceImpl implements ChemicalFiberDeliveryNotePayDetailService {

    private final ChemicalFiberDeliveryNotePayDetailRepository chemicalFiberDeliveryNotePayDetailRepository;

    private final ChemicalFiberDeliveryNotePayDetailMapper chemicalFiberDeliveryNotePayDetailMapper;

    public ChemicalFiberDeliveryNotePayDetailServiceImpl(ChemicalFiberDeliveryNotePayDetailRepository chemicalFiberDeliveryNotePayDetailRepository, ChemicalFiberDeliveryNotePayDetailMapper chemicalFiberDeliveryNotePayDetailMapper) {
        this.chemicalFiberDeliveryNotePayDetailRepository = chemicalFiberDeliveryNotePayDetailRepository;
        this.chemicalFiberDeliveryNotePayDetailMapper = chemicalFiberDeliveryNotePayDetailMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberDeliveryNotePayDetail> page = chemicalFiberDeliveryNotePayDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryNotePayDetailMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberDeliveryNotePayDetailDTO> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria){
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryNotePayDetailDTO findById(Integer id) {
        ChemicalFiberDeliveryNotePayDetail chemicalFiberDeliveryNotePayDetail = chemicalFiberDeliveryNotePayDetailRepository.findById(id).orElseGet(ChemicalFiberDeliveryNotePayDetail::new);
        ValidationUtil.isNull(chemicalFiberDeliveryNotePayDetail.getId(),"ChemicalFiberDeliveryNotePayDetail","id",id);
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNotePayDetailDTO create(ChemicalFiberDeliveryNotePayDetail resources) {
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryNotePayDetail resources) {
        ChemicalFiberDeliveryNotePayDetail chemicalFiberDeliveryNotePayDetail = chemicalFiberDeliveryNotePayDetailRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryNotePayDetail::new);
        ValidationUtil.isNull( chemicalFiberDeliveryNotePayDetail.getId(),"ChemicalFiberDeliveryNotePayDetail","id",resources.getId());
        chemicalFiberDeliveryNotePayDetail.copy(resources);
        chemicalFiberDeliveryNotePayDetailRepository.save(chemicalFiberDeliveryNotePayDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberDeliveryNotePayDetailRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberDeliveryNotePayDetailDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberDeliveryNotePayDetailDTO chemicalFiberDeliveryNotePayDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户id", chemicalFiberDeliveryNotePayDetail.getCustomerId());
            map.put("客户名称", chemicalFiberDeliveryNotePayDetail.getCustomerName());
            map.put("付款方式", chemicalFiberDeliveryNotePayDetail.getPayment());
            map.put("创建日期", chemicalFiberDeliveryNotePayDetail.getCreateDate());
            map.put("支付日期", chemicalFiberDeliveryNotePayDetail.getPayDate());
            map.put("操作人员", chemicalFiberDeliveryNotePayDetail.getInputUser());
            map.put("订单号", chemicalFiberDeliveryNotePayDetail.getScanNumber());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}