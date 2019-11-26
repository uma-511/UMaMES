package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberStockService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockMapper;
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
@CacheConfig(cacheNames = "chemicalFiberStock")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberStockServiceImpl implements ChemicalFiberStockService {

    private final ChemicalFiberStockRepository chemicalFiberStockRepository;

    private final ChemicalFiberStockMapper chemicalFiberStockMapper;

    public ChemicalFiberStockServiceImpl(ChemicalFiberStockRepository chemicalFiberStockRepository, ChemicalFiberStockMapper chemicalFiberStockMapper) {
        this.chemicalFiberStockRepository = chemicalFiberStockRepository;
        this.chemicalFiberStockMapper = chemicalFiberStockMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberStockQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberStock> page = chemicalFiberStockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberStockMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberStockDTO> queryAll(ChemicalFiberStockQueryCriteria criteria){
        return chemicalFiberStockMapper.toDto(chemicalFiberStockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberStockDTO findById(Integer id) {
        ChemicalFiberStock chemicalFiberStock = chemicalFiberStockRepository.findById(id).orElseGet(ChemicalFiberStock::new);
        ValidationUtil.isNull(chemicalFiberStock.getId(),"ChemicalFiberStock","id",id);
        return chemicalFiberStockMapper.toDto(chemicalFiberStock);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberStockDTO create(ChemicalFiberStock resources) {
        return chemicalFiberStockMapper.toDto(chemicalFiberStockRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberStock resources) {
        ChemicalFiberStock chemicalFiberStock = chemicalFiberStockRepository.findById(resources.getId()).orElseGet(ChemicalFiberStock::new);
        ValidationUtil.isNull( chemicalFiberStock.getId(),"ChemicalFiberStock","id",resources.getId());
        chemicalFiberStock.copy(resources);
        chemicalFiberStockRepository.save(chemicalFiberStock);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberStockRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberStockDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberStockDTO chemicalFiberStock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品id", chemicalFiberStock.getProdId());
            map.put("产品型号", chemicalFiberStock.getProdModel());
            map.put("产品名称", chemicalFiberStock.getProdName());
            map.put("产品色号", chemicalFiberStock.getProdColor());
            map.put("产品纤度", chemicalFiberStock.getProdFineness());
            map.put("总净重", chemicalFiberStock.getTotalNetWeight());
            map.put("总皮重", chemicalFiberStock.getTotalTare());
            map.put("总毛重", chemicalFiberStock.getTotalGrossWeight());
            map.put("总个数", chemicalFiberStock.getTotalNumber());
            map.put("总件数", chemicalFiberStock.getTotalBag());
            map.put("最大值", chemicalFiberStock.getMax());
            map.put("最小值", chemicalFiberStock.getMin());
            map.put("库存指标", chemicalFiberStock.getFlag());
            map.put("状态", chemicalFiberStock.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void stockTask() {
        System.out.println("\n\n--------------测试开始----------------\n\n");
        chemicalFiberStockRepository.stockTask();
        System.out.println("\n\n--------------测试完成----------------\n\n");
    }
}