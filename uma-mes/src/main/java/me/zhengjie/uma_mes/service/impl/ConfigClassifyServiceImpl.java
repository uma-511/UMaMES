package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ConfigClassify;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ConfigClassifyRepository;
import me.zhengjie.uma_mes.service.ConfigClassifyService;
import me.zhengjie.uma_mes.service.dto.ConfigClassifyDTO;
import me.zhengjie.uma_mes.service.dto.ConfigClassifyQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ConfigClassifyMapper;
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
@CacheConfig(cacheNames = "configClassify")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigClassifyServiceImpl implements ConfigClassifyService {

    private final ConfigClassifyRepository configClassifyRepository;

    private final ConfigClassifyMapper configClassifyMapper;

    public ConfigClassifyServiceImpl(ConfigClassifyRepository configClassifyRepository, ConfigClassifyMapper configClassifyMapper) {
        this.configClassifyRepository = configClassifyRepository;
        this.configClassifyMapper = configClassifyMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ConfigClassifyQueryCriteria criteria, Pageable pageable){
        Page<ConfigClassify> page = configClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(configClassifyMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ConfigClassifyDTO> queryAll(ConfigClassifyQueryCriteria criteria){
        return configClassifyMapper.toDto(configClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ConfigClassifyDTO findById(Integer id) {
        ConfigClassify configClassify = configClassifyRepository.findById(id).orElseGet(ConfigClassify::new);
        ValidationUtil.isNull(configClassify.getId(),"ConfigClassify","id",id);
        return configClassifyMapper.toDto(configClassify);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ConfigClassifyDTO create(ConfigClassify resources) {
        return configClassifyMapper.toDto(configClassifyRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ConfigClassify resources) {
        ConfigClassify configClassify = configClassifyRepository.findById(resources.getId()).orElseGet(ConfigClassify::new);
        ValidationUtil.isNull( configClassify.getId(),"ConfigClassify","id",resources.getId());
        configClassify.copy(resources);
        configClassifyRepository.save(configClassify);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        configClassifyRepository.deleteById(id);
    }


    @Override
    public void download(List<ConfigClassifyDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConfigClassifyDTO configClassify : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("配置分类名称", configClassify.getName());
            map.put("别名", configClassify.getAlias());
            map.put(" delFlag",  configClassify.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}