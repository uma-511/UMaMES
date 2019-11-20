package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaLabelChemicalFiber;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaLabelChemicalFiberRepository;
import me.zhengjie.uma_mes.service.UmaLabelChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaLabelChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaLabelChemicalFiberQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaLabelChemicalFiberMapper;
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
@CacheConfig(cacheNames = "umaLabelChemicalFiber")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaLabelChemicalFiberServiceImpl implements UmaLabelChemicalFiberService {

    private final UmaLabelChemicalFiberRepository umaLabelChemicalFiberRepository;

    private final UmaLabelChemicalFiberMapper umaLabelChemicalFiberMapper;

    public UmaLabelChemicalFiberServiceImpl(UmaLabelChemicalFiberRepository umaLabelChemicalFiberRepository, UmaLabelChemicalFiberMapper umaLabelChemicalFiberMapper) {
        this.umaLabelChemicalFiberRepository = umaLabelChemicalFiberRepository;
        this.umaLabelChemicalFiberMapper = umaLabelChemicalFiberMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaLabelChemicalFiberQueryCriteria criteria, Pageable pageable){
        Page<UmaLabelChemicalFiber> page = umaLabelChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaLabelChemicalFiberMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaLabelChemicalFiberDTO> queryAll(UmaLabelChemicalFiberQueryCriteria criteria){
        return umaLabelChemicalFiberMapper.toDto(umaLabelChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaLabelChemicalFiberDTO findById(Integer id) {
        UmaLabelChemicalFiber umaLabelChemicalFiber = umaLabelChemicalFiberRepository.findById(id).orElseGet(UmaLabelChemicalFiber::new);
        ValidationUtil.isNull(umaLabelChemicalFiber.getId(),"UmaLabelChemicalFiber","id",id);
        return umaLabelChemicalFiberMapper.toDto(umaLabelChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaLabelChemicalFiberDTO create(UmaLabelChemicalFiber resources) {
        return umaLabelChemicalFiberMapper.toDto(umaLabelChemicalFiberRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaLabelChemicalFiber resources) {
        UmaLabelChemicalFiber umaLabelChemicalFiber = umaLabelChemicalFiberRepository.findById(resources.getId()).orElseGet(UmaLabelChemicalFiber::new);
        ValidationUtil.isNull( umaLabelChemicalFiber.getId(),"UmaLabelChemicalFiber","id",resources.getId());
        umaLabelChemicalFiber.copy(resources);
        umaLabelChemicalFiberRepository.save(umaLabelChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaLabelChemicalFiberRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaLabelChemicalFiberDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaLabelChemicalFiberDTO umaLabelChemicalFiber : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("条码号", umaLabelChemicalFiber.getLabelNumber());
            map.put("生产单id", umaLabelChemicalFiber.getProductionId());
            map.put("状态", umaLabelChemicalFiber.getStatus());
            map.put("打印时间", umaLabelChemicalFiber.getPrintTime());
            map.put("每袋个数", umaLabelChemicalFiber.getFactPerBagNumber());
            map.put("净重", umaLabelChemicalFiber.getNetWeight());
            map.put("皮重", umaLabelChemicalFiber.getTare());
            map.put("毛重", umaLabelChemicalFiber.getGrossWeight());
            map.put("班次", umaLabelChemicalFiber.getShifts());
            map.put("包装员", umaLabelChemicalFiber.getPacker());
            map.put("入库单号", umaLabelChemicalFiber.getRkNumber());
            map.put("送货单号", umaLabelChemicalFiber.getShNumber());
            map.put("退库单号", umaLabelChemicalFiber.getTkNumber());
            map.put("退货单号", umaLabelChemicalFiber.getThNumber());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}