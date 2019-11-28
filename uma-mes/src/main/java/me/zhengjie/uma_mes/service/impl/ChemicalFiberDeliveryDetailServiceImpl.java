package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryDetailMapper;
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
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryDetailServiceImpl implements ChemicalFiberDeliveryDetailService {

    private final ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository;

    private final ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper;

    public ChemicalFiberDeliveryDetailServiceImpl(ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository, ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper) {
        this.chemicalFiberDeliveryDetailRepository = chemicalFiberDeliveryDetailRepository;
        this.chemicalFiberDeliveryDetailMapper = chemicalFiberDeliveryDetailMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberDeliveryDetail> page = chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryDetailMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberDeliveryDetailDTO> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria){
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryDetailDTO findById(Integer id) {
        ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = chemicalFiberDeliveryDetailRepository.findById(id).orElseGet(ChemicalFiberDeliveryDetail::new);
        ValidationUtil.isNull(chemicalFiberDeliveryDetail.getId(),"ChemicalFiberDeliveryDetail","id",id);
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryDetailDTO create(ChemicalFiberDeliveryDetail resources) {
        return chemicalFiberDeliveryDetailMapper.toDto(chemicalFiberDeliveryDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryDetail resources) {
        ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = chemicalFiberDeliveryDetailRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryDetail::new);
        ValidationUtil.isNull( chemicalFiberDeliveryDetail.getId(),"ChemicalFiberDeliveryDetail","id",resources.getId());
        chemicalFiberDeliveryDetail.copy(resources);
        chemicalFiberDeliveryDetailRepository.save(chemicalFiberDeliveryDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
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
}