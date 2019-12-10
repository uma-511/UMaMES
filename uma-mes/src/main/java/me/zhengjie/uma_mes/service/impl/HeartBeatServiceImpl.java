package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.HeartBeat;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.HeartBeatRepository;
import me.zhengjie.uma_mes.service.HeartBeatService;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import me.zhengjie.uma_mes.service.dto.HeartBeatQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.HeartBeatMapper;
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
* @date 2019-12-04
*/
@Service
@CacheConfig(cacheNames = "heartBeat")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HeartBeatServiceImpl implements HeartBeatService {

    private final HeartBeatRepository heartBeatRepository;

    private final HeartBeatMapper heartBeatMapper;

    public HeartBeatServiceImpl(HeartBeatRepository heartBeatRepository, HeartBeatMapper heartBeatMapper) {
        this.heartBeatRepository = heartBeatRepository;
        this.heartBeatMapper = heartBeatMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(HeartBeatQueryCriteria criteria, Pageable pageable){
        Page<HeartBeat> page = heartBeatRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(heartBeatMapper::toDto));
    }

    @Override
    @Cacheable
    public List<HeartBeatDTO> queryAll(HeartBeatQueryCriteria criteria){
        return heartBeatMapper.toDto(heartBeatRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public HeartBeatDTO findById(Integer id) {
        HeartBeat heartBeat = heartBeatRepository.findById(id).orElseGet(HeartBeat::new);
        ValidationUtil.isNull(heartBeat.getId(),"HeartBeat","id",id);
        return heartBeatMapper.toDto(heartBeat);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public HeartBeatDTO create(HeartBeat resources) {
        return heartBeatMapper.toDto(heartBeatRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(HeartBeat resources) {
        HeartBeat heartBeat = heartBeatRepository.findById(resources.getId()).orElseGet(HeartBeat::new);
        ValidationUtil.isNull( heartBeat.getId(),"HeartBeat","id",resources.getId());
        heartBeat.copy(resources);
        heartBeatRepository.save(heartBeat);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        heartBeatRepository.deleteById(id);
    }


    @Override
    public void download(List<HeartBeatDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HeartBeatDTO heartBeat : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("终端id", heartBeat.getIp());
            map.put("终端端口", heartBeat.getPort());
            map.put("心跳发送时间", heartBeat.getSendTime());
            map.put("心跳返回时间", heartBeat.getResponseTime());
            map.put("创建时间", heartBeat.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}