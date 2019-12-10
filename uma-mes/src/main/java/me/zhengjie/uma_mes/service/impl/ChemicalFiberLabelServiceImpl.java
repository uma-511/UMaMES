package me.zhengjie.uma_mes.service.impl;

import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelTotalDTO;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFiberLabelRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberLabelMapper;
import org.springframework.beans.BeanUtils;
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
@CacheConfig(cacheNames = "chemicalFiberLabel")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberLabelServiceImpl implements ChemicalFiberLabelService {

    private final ChemicalFiberLabelRepository chemicalFiberLabelRepository;

    private final ChemicalFiberLabelMapper chemicalFiberLabelMapper;

    public ChemicalFiberLabelServiceImpl(ChemicalFiberLabelRepository chemicalFiberLabelRepository, ChemicalFiberLabelMapper chemicalFiberLabelMapper) {
        this.chemicalFiberLabelRepository = chemicalFiberLabelRepository;
        this.chemicalFiberLabelMapper = chemicalFiberLabelMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberLabelQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        Page<ChemicalFiberLabel> page = chemicalFiberLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberLabelMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberLabelDTO> queryAll(ChemicalFiberLabelQueryCriteria criteria){
        return chemicalFiberLabelMapper.toDto(chemicalFiberLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberLabelDTO findById(Integer id) {
        ChemicalFiberLabel chemicalFiberLabel = chemicalFiberLabelRepository.findById(id).orElseGet(ChemicalFiberLabel::new);
        ValidationUtil.isNull(chemicalFiberLabel.getId(),"ChemicalFiberLabel","id",id);
        return chemicalFiberLabelMapper.toDto(chemicalFiberLabel);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberLabelDTO create(ChemicalFiberLabel resources) {
        return chemicalFiberLabelMapper.toDto(chemicalFiberLabelRepository.save(resources));
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberLabel resources) {
        ChemicalFiberLabel chemicalFiberLabel = chemicalFiberLabelRepository.findById(resources.getId()).orElseGet(ChemicalFiberLabel::new);
        ValidationUtil.isNull( chemicalFiberLabel.getId(),"ChemicalFiberLabel","id",resources.getId());
        chemicalFiberLabel.copy(resources);
        chemicalFiberLabelRepository.save(chemicalFiberLabel);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberLabelRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberLabelDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberLabelDTO chemicalFiberLabel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("条码号", chemicalFiberLabel.getLabelNumber());
            map.put("生产单id", chemicalFiberLabel.getProductionId());
            map.put("状态", chemicalFiberLabel.getStatus());
            map.put("打印时间", chemicalFiberLabel.getPrintTime());
            map.put("每袋个数", chemicalFiberLabel.getFactPerBagNumber());
            map.put("净重", chemicalFiberLabel.getNetWeight());
            map.put("皮重", chemicalFiberLabel.getTare());
            map.put("毛重", chemicalFiberLabel.getGrossWeight());
            map.put("班次", chemicalFiberLabel.getShifts());
            map.put("包装员", chemicalFiberLabel.getPacker());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(List<ChemicalFiberLabel> chemicalFiberLabels) {
        chemicalFiberLabelRepository.saveAll(chemicalFiberLabels);
    }

    @Override
    public ChemicalFiberLabelTotalDTO getTotalByProductionId(Integer productionId) {
        Map map = chemicalFiberLabelRepository.getTotalByProductionId(productionId);

        int factPerBagNumber=0;
        BigDecimal netWeight = new BigDecimal(0);

        if(map.containsKey("fact_per_bag_number")){
            factPerBagNumber = Integer.parseInt(map.get("fact_per_bag_number").toString());
        }

        if(map.containsKey("net_weight")){
            netWeight = new BigDecimal(map.get("net_weight").toString());
        }

        ChemicalFiberLabelTotalDTO totalDTO = new ChemicalFiberLabelTotalDTO();
        totalDTO.setTotalNumber(factPerBagNumber);
        totalDTO.setTotalWeight(netWeight);
        return totalDTO;
    }

    @Override
    public ChemicalFiberLabel getByLabelNumber(String labelNumber) {
        return chemicalFiberLabelRepository.getByLabelNumber(labelNumber);
    }
}