package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
@CacheConfig(cacheNames = "chemicalFiberProduct")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductServiceImpl implements ChemicalFiberProductService {

    private final ChemicalFiberProductRepository chemicalFiberProductRepository;

    private final ChemicalFiberProductMapper chemicalFiberProductMapper;

    public ChemicalFiberProductServiceImpl(ChemicalFiberProductRepository chemicalFiberProductRepository, ChemicalFiberProductMapper chemicalFiberProductMapper) {
        this.chemicalFiberProductRepository = chemicalFiberProductRepository;
        this.chemicalFiberProductMapper = chemicalFiberProductMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberProductQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        Page<ChemicalFiberProduct> page = chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberProductMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberProductDTO> queryAll(ChemicalFiberProductQueryCriteria criteria){
        criteria.setDelFlag(0);
        return chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberProductDTO findById(Integer id) {
        ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(id).orElseGet(ChemicalFiberProduct::new);
        ValidationUtil.isNull(chemicalFiberProduct.getId(),"ChemicalFiberProduct","id",id);
        return chemicalFiberProductMapper.toDto(chemicalFiberProduct);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductDTO create(ChemicalFiberProduct resources) {
        ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria.setModelAccurate(resources.getModel());
        chemicalFiberProductQueryCriteria.setDelFlag(0);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, chemicalFiberProductQueryCriteria, criteriaBuilder)));
        if (chemicalFiberProductDTOS.size() > 0) {
            throw new BadRequestException("请确保产品型号唯一");
        } else {
            resources.setCreateUser(SecurityUtils.getUsername());
            resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
            resources.setDelFlag(0);
            return chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.save(resources));
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberProduct resources) {
        ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria.setModelAccurate(resources.getModel());
        chemicalFiberProductQueryCriteria.setDelFlag(0);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, chemicalFiberProductQueryCriteria, criteriaBuilder)));
        if (chemicalFiberProductDTOS.size() > 0 && chemicalFiberProductDTOS.get(0).getModel().equals(resources.getModel()) && resources.getId() != chemicalFiberProductDTOS.get(0).getId()) {
            throw new BadRequestException("请确保产品型号唯一");
        } else {
            ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(resources.getId()).orElseGet(ChemicalFiberProduct::new);
            ValidationUtil.isNull( chemicalFiberProduct.getId(),"ChemicalFiberProduct","id",resources.getId());
            chemicalFiberProduct.copy(resources);
            resources.setCreateUser(SecurityUtils.getUsername());
            chemicalFiberProductRepository.save(chemicalFiberProduct);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(id).orElseGet(ChemicalFiberProduct::new);
        chemicalFiberProduct.setDelFlag(1);
        chemicalFiberProductRepository.save(chemicalFiberProduct);
    }


    @Override
    public void download(List<ChemicalFiberProductDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberProductDTO chemicalFiberProduct : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品型号", chemicalFiberProduct.getModel());
            map.put("产品名称", chemicalFiberProduct.getName());
            map.put("色号", chemicalFiberProduct.getColor());
            map.put("纤度", chemicalFiberProduct.getFineness());
            map.put("创建日期", chemicalFiberProduct.getCreateDate());
            map.put("创建人", chemicalFiberProduct.getCreateUser());
            map.put("删除标识", chemicalFiberProduct.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}