package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Config;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ConfigRepository;
import me.zhengjie.uma_mes.service.ConfigService;
import me.zhengjie.uma_mes.service.dto.ConfigDTO;
import me.zhengjie.uma_mes.service.dto.ConfigQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ConfigMapper;
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
@CacheConfig(cacheNames = "config")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigServiceImpl(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ConfigQueryCriteria criteria, Pageable pageable){
        Page<Config> page = configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(configMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ConfigDTO> queryAll(ConfigQueryCriteria criteria){
        return configMapper.toDto(configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ConfigDTO findById(Integer id) {
        Config config = configRepository.findById(id).orElseGet(Config::new);
        ValidationUtil.isNull(config.getId(),"Config","id",id);
        return configMapper.toDto(config);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ConfigDTO create(Config resources) {
        return configMapper.toDto(configRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Config resources) {
        Config config = configRepository.findById(resources.getId()).orElseGet(Config::new);
        ValidationUtil.isNull( config.getId(),"Config","id",resources.getId());
        config.copy(resources);
        configRepository.save(config);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        configRepository.deleteById(id);
    }


    @Override
    public void download(List<ConfigDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConfigDTO config : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("分类id", config.getClassifyId());
            map.put("配置名称", config.getName());
            map.put("配置值", config.getValue());
            map.put("默认值", config.getIsDefault());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}