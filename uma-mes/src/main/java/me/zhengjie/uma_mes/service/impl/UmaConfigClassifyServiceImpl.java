package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaConfigClassify;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaConfigClassifyRepository;
import me.zhengjie.uma_mes.service.UmaConfigClassifyService;
import me.zhengjie.uma_mes.service.dto.UmaConfigClassifyDTO;
import me.zhengjie.uma_mes.service.dto.UmaConfigClassifyQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaConfigClassifyMapper;
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
@CacheConfig(cacheNames = "umaConfigClassify")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaConfigClassifyServiceImpl implements UmaConfigClassifyService {

    private final UmaConfigClassifyRepository umaConfigClassifyRepository;

    private final UmaConfigClassifyMapper umaConfigClassifyMapper;

    public UmaConfigClassifyServiceImpl(UmaConfigClassifyRepository umaConfigClassifyRepository, UmaConfigClassifyMapper umaConfigClassifyMapper) {
        this.umaConfigClassifyRepository = umaConfigClassifyRepository;
        this.umaConfigClassifyMapper = umaConfigClassifyMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaConfigClassifyQueryCriteria criteria, Pageable pageable){
        Page<UmaConfigClassify> page = umaConfigClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaConfigClassifyMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaConfigClassifyDTO> queryAll(UmaConfigClassifyQueryCriteria criteria){
        return umaConfigClassifyMapper.toDto(umaConfigClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaConfigClassifyDTO findById(Integer id) {
        UmaConfigClassify umaConfigClassify = umaConfigClassifyRepository.findById(id).orElseGet(UmaConfigClassify::new);
        ValidationUtil.isNull(umaConfigClassify.getId(),"UmaConfigClassify","id",id);
        return umaConfigClassifyMapper.toDto(umaConfigClassify);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaConfigClassifyDTO create(UmaConfigClassify resources) {
        return umaConfigClassifyMapper.toDto(umaConfigClassifyRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaConfigClassify resources) {
        UmaConfigClassify umaConfigClassify = umaConfigClassifyRepository.findById(resources.getId()).orElseGet(UmaConfigClassify::new);
        ValidationUtil.isNull( umaConfigClassify.getId(),"UmaConfigClassify","id",resources.getId());
        umaConfigClassify.copy(resources);
        umaConfigClassifyRepository.save(umaConfigClassify);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaConfigClassifyRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaConfigClassifyDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaConfigClassifyDTO umaConfigClassify : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("配置分类名称", umaConfigClassify.getName());
            map.put("别名", umaConfigClassify.getAlias());
            map.put(" delFlag",  umaConfigClassify.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}