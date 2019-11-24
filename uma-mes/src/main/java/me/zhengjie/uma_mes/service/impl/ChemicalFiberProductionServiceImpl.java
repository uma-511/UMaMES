package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductionMapper;
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
@CacheConfig(cacheNames = "chemicalFiberProduction")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductionServiceImpl implements ChemicalFiberProductionService {

    private final ChemicalFiberProductionRepository chemicalFiberProductionRepository;

    private final ChemicalFiberProductionMapper chemicalFiberProductionMapper;

    public ChemicalFiberProductionServiceImpl(ChemicalFiberProductionRepository chemicalFiberProductionRepository, ChemicalFiberProductionMapper chemicalFiberProductionMapper) {
        this.chemicalFiberProductionRepository = chemicalFiberProductionRepository;
        this.chemicalFiberProductionMapper = chemicalFiberProductionMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberProductionQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberProduction> page = chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberProductionMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberProductionDTO> queryAll(ChemicalFiberProductionQueryCriteria criteria){
        return chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberProductionDTO findById(Integer id) {
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(id).orElseGet(ChemicalFiberProduction::new);
        ValidationUtil.isNull(chemicalFiberProduction.getId(),"ChemicalFiberProduction","id",id);
        return chemicalFiberProductionMapper.toDto(chemicalFiberProduction);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductionDTO create(ChemicalFiberProduction resources) {
        return chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberProduction resources) {
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(resources.getId()).orElseGet(ChemicalFiberProduction::new);
        ValidationUtil.isNull( chemicalFiberProduction.getId(),"ChemicalFiberProduction","id",resources.getId());
        chemicalFiberProduction.copy(resources);
        chemicalFiberProductionRepository.save(chemicalFiberProduction);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberProductionRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberProductionDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberProductionDTO chemicalFiberProduction : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单编号", chemicalFiberProduction.getNumber());
            map.put("产品id", chemicalFiberProduction.getProdId());
            map.put("产品型号", chemicalFiberProduction.getProdModel());
            map.put("产品名称", chemicalFiberProduction.getProdName());
            map.put("产品颜色", chemicalFiberProduction.getProdColor());
            map.put("产品纤度", chemicalFiberProduction.getProdFineness());
            map.put("客户id", chemicalFiberProduction.getCustomerId());
            map.put("客户名称", chemicalFiberProduction.getCustomerName());
            map.put("联系电话", chemicalFiberProduction.getCustomerContactPhone());
            map.put("联系人", chemicalFiberProduction.getCustomerContacts());
            map.put("客户地址", chemicalFiberProduction.getCustomerAddress());
            map.put("客户编号", chemicalFiberProduction.getCustomerCode());
            map.put("纸芯重量", chemicalFiberProduction.getCoreWeight());
            map.put("每袋个数", chemicalFiberProduction.getPerBagNumber());
            map.put("计划生产数量", chemicalFiberProduction.getPlanNumber());
            map.put("完成数量", chemicalFiberProduction.getQuantityCompeted());
            map.put("生产进度(%)", chemicalFiberProduction.getProductionSchedule());
            map.put("交货日期", chemicalFiberProduction.getDeliveryDate());
            map.put("机器编号", chemicalFiberProduction.getMachineNumber());
            map.put("备注", chemicalFiberProduction.getRemark());
            map.put("状态", chemicalFiberProduction.getStatus());
            map.put("制单时间", chemicalFiberProduction.getCreateTime());
            map.put("制单人", chemicalFiberProduction.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}