package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ConfigCode;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ConfigCodeRepository;
import me.zhengjie.uma_mes.service.ConfigCodeService;
import me.zhengjie.uma_mes.service.dto.ConfigCodeDTO;
import me.zhengjie.uma_mes.service.dto.ConfigCodeQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ConfigCodeMapper;
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
* @author wave
* @date 2020-10-09
*/
@Service
@CacheConfig(cacheNames = "configCode")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigCodeServiceImpl implements ConfigCodeService {

    private final ConfigCodeRepository configCodeRepository;

    private final ConfigCodeMapper configCodeMapper;

    public ConfigCodeServiceImpl(ConfigCodeRepository configCodeRepository, ConfigCodeMapper configCodeMapper) {
        this.configCodeRepository = configCodeRepository;
        this.configCodeMapper = configCodeMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ConfigCodeQueryCriteria criteria, Pageable pageable){
        Page<ConfigCode> page = configCodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(configCodeMapper::toDto));
    }

    @Override
    public List<ConfigCodeDTO> queryAll(ConfigCodeQueryCriteria criteria){
        return configCodeMapper.toDto(configCodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ConfigCodeDTO findById(Integer id) {
        ConfigCode configCode = configCodeRepository.findById(id).orElseGet(ConfigCode::new);
        ValidationUtil.isNull(configCode.getId(),"ConfigCode","id",id);
        return configCodeMapper.toDto(configCode);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ConfigCodeDTO create(ConfigCode resources) {
        return configCodeMapper.toDto(configCodeRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ConfigCode resources) {
        ConfigCode configCode = configCodeRepository.findById(resources.getId()).orElseGet(ConfigCode::new);
        ValidationUtil.isNull( configCode.getId(),"ConfigCode","id",resources.getId());
        configCode.copy(resources);
        configCodeRepository.save(configCode);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ConfigCode configCode = configCodeRepository.findById(id).orElseGet(ConfigCode::new);
        configCodeRepository.save(configCode);
    }


    @Override
    public void download(List<ConfigCodeDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConfigCodeDTO configCode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("公司名", configCode.getCompanyName());
            map.put("流水号编码", configCode.getSerialCode());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}