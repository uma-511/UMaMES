package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaDeliveryDetailChemicalFiber;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaDeliveryDetailChemicalFiberRepository;
import me.zhengjie.uma_mes.service.UmaDeliveryDetailChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryDetailChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryDetailChemicalFiberQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaDeliveryDetailChemicalFiberMapper;
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
@CacheConfig(cacheNames = "umaDeliveryDetailChemicalFiber")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaDeliveryDetailChemicalFiberServiceImpl implements UmaDeliveryDetailChemicalFiberService {

    private final UmaDeliveryDetailChemicalFiberRepository umaDeliveryDetailChemicalFiberRepository;

    private final UmaDeliveryDetailChemicalFiberMapper umaDeliveryDetailChemicalFiberMapper;

    public UmaDeliveryDetailChemicalFiberServiceImpl(UmaDeliveryDetailChemicalFiberRepository umaDeliveryDetailChemicalFiberRepository, UmaDeliveryDetailChemicalFiberMapper umaDeliveryDetailChemicalFiberMapper) {
        this.umaDeliveryDetailChemicalFiberRepository = umaDeliveryDetailChemicalFiberRepository;
        this.umaDeliveryDetailChemicalFiberMapper = umaDeliveryDetailChemicalFiberMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaDeliveryDetailChemicalFiberQueryCriteria criteria, Pageable pageable){
        Page<UmaDeliveryDetailChemicalFiber> page = umaDeliveryDetailChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaDeliveryDetailChemicalFiberMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaDeliveryDetailChemicalFiberDTO> queryAll(UmaDeliveryDetailChemicalFiberQueryCriteria criteria){
        return umaDeliveryDetailChemicalFiberMapper.toDto(umaDeliveryDetailChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaDeliveryDetailChemicalFiberDTO findById(Integer id) {
        UmaDeliveryDetailChemicalFiber umaDeliveryDetailChemicalFiber = umaDeliveryDetailChemicalFiberRepository.findById(id).orElseGet(UmaDeliveryDetailChemicalFiber::new);
        ValidationUtil.isNull(umaDeliveryDetailChemicalFiber.getId(),"UmaDeliveryDetailChemicalFiber","id",id);
        return umaDeliveryDetailChemicalFiberMapper.toDto(umaDeliveryDetailChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaDeliveryDetailChemicalFiberDTO create(UmaDeliveryDetailChemicalFiber resources) {
        return umaDeliveryDetailChemicalFiberMapper.toDto(umaDeliveryDetailChemicalFiberRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaDeliveryDetailChemicalFiber resources) {
        UmaDeliveryDetailChemicalFiber umaDeliveryDetailChemicalFiber = umaDeliveryDetailChemicalFiberRepository.findById(resources.getId()).orElseGet(UmaDeliveryDetailChemicalFiber::new);
        ValidationUtil.isNull( umaDeliveryDetailChemicalFiber.getId(),"UmaDeliveryDetailChemicalFiber","id",resources.getId());
        umaDeliveryDetailChemicalFiber.copy(resources);
        umaDeliveryDetailChemicalFiberRepository.save(umaDeliveryDetailChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaDeliveryDetailChemicalFiberRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaDeliveryDetailChemicalFiberDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaDeliveryDetailChemicalFiberDTO umaDeliveryDetailChemicalFiber : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品id", umaDeliveryDetailChemicalFiber.getProdId());
            map.put("产品型号", umaDeliveryDetailChemicalFiber.getProdModel());
            map.put("产品名称", umaDeliveryDetailChemicalFiber.getProdName());
            map.put("产品色号", umaDeliveryDetailChemicalFiber.getProdColor());
            map.put("产品纤度", umaDeliveryDetailChemicalFiber.getProdFineness());
            map.put("成本单价", umaDeliveryDetailChemicalFiber.getCost());
            map.put("销售单价", umaDeliveryDetailChemicalFiber.getSellingPrice());
            map.put("单位", umaDeliveryDetailChemicalFiber.getUnit());
            map.put("总成本", umaDeliveryDetailChemicalFiber.getTotalCost());
            map.put("总金额", umaDeliveryDetailChemicalFiber.getTotalPrice());
            map.put("总件数", umaDeliveryDetailChemicalFiber.getTotalBag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}