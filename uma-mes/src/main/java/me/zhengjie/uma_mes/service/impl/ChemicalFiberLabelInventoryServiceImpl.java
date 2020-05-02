package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabelInventory;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberLabelInventoryRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelInventoryService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberLabelInventoryMapper;
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
* @date 2020-04-14
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberLabelInventory")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberLabelInventoryServiceImpl implements ChemicalFiberLabelInventoryService {

    private final ChemicalFiberLabelInventoryRepository chemicalFiberLabelInventoryRepository;

    private final ChemicalFiberLabelInventoryMapper chemicalFiberLabelInventoryMapper;

    public ChemicalFiberLabelInventoryServiceImpl(ChemicalFiberLabelInventoryRepository chemicalFiberLabelInventoryRepository, ChemicalFiberLabelInventoryMapper chemicalFiberLabelInventoryMapper) {
        this.chemicalFiberLabelInventoryRepository = chemicalFiberLabelInventoryRepository;
        this.chemicalFiberLabelInventoryMapper = chemicalFiberLabelInventoryMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberLabelInventoryQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberLabelInventory> page = chemicalFiberLabelInventoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberLabelInventoryMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberLabelInventoryDTO> queryAll(ChemicalFiberLabelInventoryQueryCriteria criteria){
        return chemicalFiberLabelInventoryMapper.toDto(chemicalFiberLabelInventoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberLabelInventoryDTO findById(Integer id) {
        ChemicalFiberLabelInventory chemicalFiberLabelInventory = chemicalFiberLabelInventoryRepository.findById(id).orElseGet(ChemicalFiberLabelInventory::new);
        ValidationUtil.isNull(chemicalFiberLabelInventory.getId(),"ChemicalFiberLabelInventory","id",id);
        return chemicalFiberLabelInventoryMapper.toDto(chemicalFiberLabelInventory);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberLabelInventoryDTO create(ChemicalFiberLabelInventory resources) {
        return chemicalFiberLabelInventoryMapper.toDto(chemicalFiberLabelInventoryRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberLabelInventory resources) {
        ChemicalFiberLabelInventory chemicalFiberLabelInventory = chemicalFiberLabelInventoryRepository.findById(resources.getId()).orElseGet(ChemicalFiberLabelInventory::new);
        ValidationUtil.isNull( chemicalFiberLabelInventory.getId(),"ChemicalFiberLabelInventory","id",resources.getId());
        chemicalFiberLabelInventory.copy(resources);
        chemicalFiberLabelInventoryRepository.save(chemicalFiberLabelInventory);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberLabelInventoryRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberLabelInventoryDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberLabelInventoryDTO chemicalFiberLabelInventory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标签编号（条码号）", chemicalFiberLabelInventory.getLabelNumber());
            map.put("生产单id", chemicalFiberLabelInventory.getProductionId());
            map.put("产品id", chemicalFiberLabelInventory.getProductId());
            map.put("便签状态 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货", chemicalFiberLabelInventory.getStatus());
            map.put("打印时间", chemicalFiberLabelInventory.getPrintTime());
            map.put("实际每袋个数", chemicalFiberLabelInventory.getFactPerBagNumber());
            map.put("净重", chemicalFiberLabelInventory.getNetWeight());
            map.put("皮重", chemicalFiberLabelInventory.getTare());
            map.put("毛重", chemicalFiberLabelInventory.getGrossWeight());
            map.put("班次", chemicalFiberLabelInventory.getShifts());
            map.put("包装员", chemicalFiberLabelInventory.getPacker());
            map.put("机台号", chemicalFiberLabelInventory.getMachine());
            map.put("流水号", chemicalFiberLabelInventory.getFlowNumber());
            map.put("纤度", chemicalFiberLabelInventory.getFineness());
            map.put("色号", chemicalFiberLabelInventory.getColor());
            map.put("芯重", chemicalFiberLabelInventory.getCoreWeight());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}