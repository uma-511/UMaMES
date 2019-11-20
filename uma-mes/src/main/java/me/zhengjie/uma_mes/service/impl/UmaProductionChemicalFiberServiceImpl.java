package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaProductionChemicalFiber;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaProductionChemicalFiberRepository;
import me.zhengjie.uma_mes.service.UmaProductionChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaProductionChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaProductionChemicalFiberQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaProductionChemicalFiberMapper;
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
@CacheConfig(cacheNames = "umaProductionChemicalFiber")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaProductionChemicalFiberServiceImpl implements UmaProductionChemicalFiberService {

    private final UmaProductionChemicalFiberRepository umaProductionChemicalFiberRepository;

    private final UmaProductionChemicalFiberMapper umaProductionChemicalFiberMapper;

    public UmaProductionChemicalFiberServiceImpl(UmaProductionChemicalFiberRepository umaProductionChemicalFiberRepository, UmaProductionChemicalFiberMapper umaProductionChemicalFiberMapper) {
        this.umaProductionChemicalFiberRepository = umaProductionChemicalFiberRepository;
        this.umaProductionChemicalFiberMapper = umaProductionChemicalFiberMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaProductionChemicalFiberQueryCriteria criteria, Pageable pageable){
        Page<UmaProductionChemicalFiber> page = umaProductionChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaProductionChemicalFiberMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaProductionChemicalFiberDTO> queryAll(UmaProductionChemicalFiberQueryCriteria criteria){
        return umaProductionChemicalFiberMapper.toDto(umaProductionChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaProductionChemicalFiberDTO findById(Integer id) {
        UmaProductionChemicalFiber umaProductionChemicalFiber = umaProductionChemicalFiberRepository.findById(id).orElseGet(UmaProductionChemicalFiber::new);
        ValidationUtil.isNull(umaProductionChemicalFiber.getId(),"UmaProductionChemicalFiber","id",id);
        return umaProductionChemicalFiberMapper.toDto(umaProductionChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaProductionChemicalFiberDTO create(UmaProductionChemicalFiber resources) {
        return umaProductionChemicalFiberMapper.toDto(umaProductionChemicalFiberRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaProductionChemicalFiber resources) {
        UmaProductionChemicalFiber umaProductionChemicalFiber = umaProductionChemicalFiberRepository.findById(resources.getId()).orElseGet(UmaProductionChemicalFiber::new);
        ValidationUtil.isNull( umaProductionChemicalFiber.getId(),"UmaProductionChemicalFiber","id",resources.getId());
        umaProductionChemicalFiber.copy(resources);
        umaProductionChemicalFiberRepository.save(umaProductionChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaProductionChemicalFiberRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaProductionChemicalFiberDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaProductionChemicalFiberDTO umaProductionChemicalFiber : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单编号", umaProductionChemicalFiber.getNumber());
            map.put("产品id", umaProductionChemicalFiber.getProdId());
            map.put("产品型号", umaProductionChemicalFiber.getProdModel());
            map.put("产品名称", umaProductionChemicalFiber.getProdName());
            map.put("产品颜色", umaProductionChemicalFiber.getProdColor());
            map.put("产品纤度", umaProductionChemicalFiber.getProdFineness());
            map.put("客户id", umaProductionChemicalFiber.getCustomerId());
            map.put("客户名称", umaProductionChemicalFiber.getCustomerName());
            map.put("联系电话", umaProductionChemicalFiber.getCustomerContactPhone());
            map.put("联系人", umaProductionChemicalFiber.getCustomerContacts());
            map.put("客户地址", umaProductionChemicalFiber.getCustomerAddress());
            map.put("客户编号", umaProductionChemicalFiber.getCustomerCode());
            map.put("纸芯重量", umaProductionChemicalFiber.getCoreWeight());
            map.put("每袋个数", umaProductionChemicalFiber.getPerBagNumber());
            map.put("计划生产数量", umaProductionChemicalFiber.getPlanNumber());
            map.put("完成数量", umaProductionChemicalFiber.getQuantityCompeted());
            map.put("生产进度(%)", umaProductionChemicalFiber.getProductionSchedule());
            map.put("交货日期", umaProductionChemicalFiber.getDeliveryDate());
            map.put("机器编号", umaProductionChemicalFiber.getMachineNumber());
            map.put("备注", umaProductionChemicalFiber.getRemark());
            map.put("状态", umaProductionChemicalFiber.getStatus());
            map.put("创建时间", umaProductionChemicalFiber.getCreateTime());
            map.put("创建人", umaProductionChemicalFiber.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}