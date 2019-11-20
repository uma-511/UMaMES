package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaStock;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaStockRepository;
import me.zhengjie.uma_mes.service.UmaStockService;
import me.zhengjie.uma_mes.service.dto.UmaStockDTO;
import me.zhengjie.uma_mes.service.dto.UmaStockQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaStockMapper;
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
@CacheConfig(cacheNames = "umaStock")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaStockServiceImpl implements UmaStockService {

    private final UmaStockRepository umaStockRepository;

    private final UmaStockMapper umaStockMapper;

    public UmaStockServiceImpl(UmaStockRepository umaStockRepository, UmaStockMapper umaStockMapper) {
        this.umaStockRepository = umaStockRepository;
        this.umaStockMapper = umaStockMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaStockQueryCriteria criteria, Pageable pageable){
        Page<UmaStock> page = umaStockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaStockMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaStockDTO> queryAll(UmaStockQueryCriteria criteria){
        return umaStockMapper.toDto(umaStockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaStockDTO findById(Integer id) {
        UmaStock umaStock = umaStockRepository.findById(id).orElseGet(UmaStock::new);
        ValidationUtil.isNull(umaStock.getId(),"UmaStock","id",id);
        return umaStockMapper.toDto(umaStock);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaStockDTO create(UmaStock resources) {
        return umaStockMapper.toDto(umaStockRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaStock resources) {
        UmaStock umaStock = umaStockRepository.findById(resources.getId()).orElseGet(UmaStock::new);
        ValidationUtil.isNull( umaStock.getId(),"UmaStock","id",resources.getId());
        umaStock.copy(resources);
        umaStockRepository.save(umaStock);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaStockRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaStockDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaStockDTO umaStock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品id", umaStock.getProdId());
            map.put("产品型号", umaStock.getProdModel());
            map.put("产品名称", umaStock.getProdName());
            map.put("产品色号", umaStock.getProdColor());
            map.put("产品纤度", umaStock.getProdFineness());
            map.put("总净重", umaStock.getTotalNetWeight());
            map.put("总皮重", umaStock.getTotalTare());
            map.put("总毛重", umaStock.getTotalGrossWeight());
            map.put("总个数", umaStock.getTotalNumber());
            map.put("总件数", umaStock.getTotalBag());
            map.put("最大值", umaStock.getMax());
            map.put("最小值", umaStock.getMin());
            map.put("库存指标", umaStock.getFlag());
            map.put("状态", umaStock.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}