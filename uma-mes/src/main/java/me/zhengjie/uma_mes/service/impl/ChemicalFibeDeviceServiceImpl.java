package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFibeDevice;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFibeDeviceRepository;
import me.zhengjie.uma_mes.service.ChemicalFibeDeviceService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFibeDeviceMapper;
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
* @date 2020-04-16
*/
@Service
@CacheConfig(cacheNames = "chemicalFibeDevice")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFibeDeviceServiceImpl implements ChemicalFibeDeviceService {

    private final ChemicalFibeDeviceRepository chemicalFibeDeviceRepository;

    private final ChemicalFibeDeviceMapper chemicalFibeDeviceMapper;

    public ChemicalFibeDeviceServiceImpl(ChemicalFibeDeviceRepository chemicalFibeDeviceRepository, ChemicalFibeDeviceMapper chemicalFibeDeviceMapper) {
        this.chemicalFibeDeviceRepository = chemicalFibeDeviceRepository;
        this.chemicalFibeDeviceMapper = chemicalFibeDeviceMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFibeDeviceQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFibeDevice> page = chemicalFibeDeviceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFibeDeviceMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFibeDeviceDTO> queryAll(ChemicalFibeDeviceQueryCriteria criteria){
        return chemicalFibeDeviceMapper.toDto(chemicalFibeDeviceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFibeDeviceDTO findById(Integer id) {
        ChemicalFibeDevice chemicalFibeDevice = chemicalFibeDeviceRepository.findById(id).orElseGet(ChemicalFibeDevice::new);
        ValidationUtil.isNull(chemicalFibeDevice.getId(),"ChemicalFibeDevice","id",id);
        return chemicalFibeDeviceMapper.toDto(chemicalFibeDevice);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFibeDeviceDTO create(ChemicalFibeDevice resources) {
        return chemicalFibeDeviceMapper.toDto(chemicalFibeDeviceRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFibeDevice resources) {
        ChemicalFibeDevice chemicalFibeDevice = chemicalFibeDeviceRepository.findById(resources.getId()).orElseGet(ChemicalFibeDevice::new);
        ValidationUtil.isNull( chemicalFibeDevice.getId(),"ChemicalFibeDevice","id",resources.getId());
        chemicalFibeDevice.copy(resources);
        chemicalFibeDeviceRepository.save(chemicalFibeDevice);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFibeDeviceRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFibeDeviceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFibeDeviceDTO chemicalFibeDevice : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("设备ip", chemicalFibeDevice.getIp());
            map.put("设备名称", chemicalFibeDevice.getName());
            map.put("设备类型：0：打印机 1：触摸屏 2：扫描抢 3：秤 4：标签打印机 5：手持机", chemicalFibeDevice.getType());
            map.put("使用状态：1：正常，0：停用", chemicalFibeDevice.getStatus());
            map.put("设备分组id", chemicalFibeDevice.getGroupId());
            map.put("设备分组名称", chemicalFibeDevice.getGroupName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}