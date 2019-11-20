package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaConfig;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaConfigRepository;
import me.zhengjie.uma_mes.service.UmaConfigService;
import me.zhengjie.uma_mes.service.dto.UmaConfigDTO;
import me.zhengjie.uma_mes.service.dto.UmaConfigQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaConfigMapper;
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
@CacheConfig(cacheNames = "umaConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaConfigServiceImpl implements UmaConfigService {

    private final UmaConfigRepository umaConfigRepository;

    private final UmaConfigMapper umaConfigMapper;

    public UmaConfigServiceImpl(UmaConfigRepository umaConfigRepository, UmaConfigMapper umaConfigMapper) {
        this.umaConfigRepository = umaConfigRepository;
        this.umaConfigMapper = umaConfigMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaConfigQueryCriteria criteria, Pageable pageable){
        Page<UmaConfig> page = umaConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaConfigMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaConfigDTO> queryAll(UmaConfigQueryCriteria criteria){
        return umaConfigMapper.toDto(umaConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaConfigDTO findById(Integer id) {
        UmaConfig umaConfig = umaConfigRepository.findById(id).orElseGet(UmaConfig::new);
        ValidationUtil.isNull(umaConfig.getId(),"UmaConfig","id",id);
        return umaConfigMapper.toDto(umaConfig);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaConfigDTO create(UmaConfig resources) {
        return umaConfigMapper.toDto(umaConfigRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaConfig resources) {
        UmaConfig umaConfig = umaConfigRepository.findById(resources.getId()).orElseGet(UmaConfig::new);
        ValidationUtil.isNull( umaConfig.getId(),"UmaConfig","id",resources.getId());
        umaConfig.copy(resources);
        umaConfigRepository.save(umaConfig);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaConfigRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaConfigDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaConfigDTO umaConfig : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("分类id", umaConfig.getClassifyId());
            map.put("配置名称", umaConfig.getName());
            map.put("配置值", umaConfig.getValue());
            map.put("默认值", umaConfig.getIsDefault());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}