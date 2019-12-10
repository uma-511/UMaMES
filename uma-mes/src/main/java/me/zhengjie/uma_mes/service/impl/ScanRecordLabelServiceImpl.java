package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ScanRecordLabel;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ScanRecordLabelRepository;
import me.zhengjie.uma_mes.service.ScanRecordLabelService;
import me.zhengjie.uma_mes.service.dto.ScanRecordLabelDTO;
import me.zhengjie.uma_mes.service.dto.ScanRecordLabelQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ScanRecordLabelMapper;
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
* @date 2019-11-21
*/
@Service
@CacheConfig(cacheNames = "scanRecordLabel")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScanRecordLabelServiceImpl implements ScanRecordLabelService {

    private final ScanRecordLabelRepository scanRecordLabelRepository;

    private final ScanRecordLabelMapper scanRecordLabelMapper;

    public ScanRecordLabelServiceImpl(ScanRecordLabelRepository scanRecordLabelRepository, ScanRecordLabelMapper scanRecordLabelMapper) {
        this.scanRecordLabelRepository = scanRecordLabelRepository;
        this.scanRecordLabelMapper = scanRecordLabelMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ScanRecordLabelQueryCriteria criteria, Pageable pageable){
        Page<ScanRecordLabel> page = scanRecordLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(scanRecordLabelMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ScanRecordLabelDTO> queryAll(ScanRecordLabelQueryCriteria criteria){
        return scanRecordLabelMapper.toDto(scanRecordLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ScanRecordLabelDTO findById(Integer id) {
        ScanRecordLabel scanRecordLabel = scanRecordLabelRepository.findById(id).orElseGet(ScanRecordLabel::new);
        ValidationUtil.isNull(scanRecordLabel.getId(),"ScanRecordLabel","id",id);
        return scanRecordLabelMapper.toDto(scanRecordLabel);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ScanRecordLabelDTO create(ScanRecordLabel resources) {
        return scanRecordLabelMapper.toDto(scanRecordLabelRepository.save(resources));
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ScanRecordLabel resources) {
        ScanRecordLabel scanRecordLabel = scanRecordLabelRepository.findById(resources.getId()).orElseGet(ScanRecordLabel::new);
        ValidationUtil.isNull( scanRecordLabel.getId(),"ScanRecordLabel","id",resources.getId());
        scanRecordLabel.copy(resources);
        scanRecordLabelRepository.save(scanRecordLabel);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        scanRecordLabelRepository.deleteById(id);
    }


    @Override
    public void download(List<ScanRecordLabelDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScanRecordLabelDTO scanRecordLabel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("扫描记录id", scanRecordLabel.getScanRecordId());
            map.put("标签id", scanRecordLabel.getLabelId());
            map.put("扫描时间", scanRecordLabel.getScanTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ScanRecordLabel> create(List<ScanRecordLabel> scanRecordLabels) {
        return scanRecordLabelRepository.saveAll(scanRecordLabels);
    }
}