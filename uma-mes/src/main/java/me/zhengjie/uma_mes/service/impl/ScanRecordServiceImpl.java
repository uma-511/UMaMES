package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ScanRecord;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ScanRecordRepository;
import me.zhengjie.uma_mes.service.ScanRecordService;
import me.zhengjie.uma_mes.service.dto.ScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.ScanRecordQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ScanRecordMapper;
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
@CacheConfig(cacheNames = "scanRecord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScanRecordServiceImpl implements ScanRecordService {

    private final ScanRecordRepository scanRecordRepository;

    private final ScanRecordMapper scanRecordMapper;

    public ScanRecordServiceImpl(ScanRecordRepository scanRecordRepository, ScanRecordMapper scanRecordMapper) {
        this.scanRecordRepository = scanRecordRepository;
        this.scanRecordMapper = scanRecordMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ScanRecordQueryCriteria criteria, Pageable pageable){
        Page<ScanRecord> page = scanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(scanRecordMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ScanRecordDTO> queryAll(ScanRecordQueryCriteria criteria){
        return scanRecordMapper.toDto(scanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ScanRecordDTO findById(Integer id) {
        ScanRecord scanRecord = scanRecordRepository.findById(id).orElseGet(ScanRecord::new);
        ValidationUtil.isNull(scanRecord.getId(),"ScanRecord","id",id);
        return scanRecordMapper.toDto(scanRecord);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ScanRecordDTO create(ScanRecord resources) {
        return scanRecordMapper.toDto(scanRecordRepository.save(resources));
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ScanRecord resources) {
        ScanRecord scanRecord = scanRecordRepository.findById(resources.getId()).orElseGet(ScanRecord::new);
        ValidationUtil.isNull( scanRecord.getId(),"ScanRecord","id",resources.getId());
        scanRecord.copy(resources);
        scanRecordRepository.save(scanRecord);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        scanRecordRepository.deleteById(id);
    }


    @Override
    public void download(List<ScanRecordDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScanRecordDTO scanRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("扫描单号", scanRecord.getScanNumber());
            map.put("扫描员", scanRecord.getScanUser());
            map.put("扫描时间", scanRecord.getScanTime());
            map.put("扫描类型", scanRecord.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}