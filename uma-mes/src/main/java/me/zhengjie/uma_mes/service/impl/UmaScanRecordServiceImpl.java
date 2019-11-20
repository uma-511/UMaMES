package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaScanRecord;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaScanRecordRepository;
import me.zhengjie.uma_mes.service.UmaScanRecordService;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaScanRecordMapper;
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
@CacheConfig(cacheNames = "umaScanRecord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaScanRecordServiceImpl implements UmaScanRecordService {

    private final UmaScanRecordRepository umaScanRecordRepository;

    private final UmaScanRecordMapper umaScanRecordMapper;

    public UmaScanRecordServiceImpl(UmaScanRecordRepository umaScanRecordRepository, UmaScanRecordMapper umaScanRecordMapper) {
        this.umaScanRecordRepository = umaScanRecordRepository;
        this.umaScanRecordMapper = umaScanRecordMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaScanRecordQueryCriteria criteria, Pageable pageable){
        Page<UmaScanRecord> page = umaScanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaScanRecordMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaScanRecordDTO> queryAll(UmaScanRecordQueryCriteria criteria){
        return umaScanRecordMapper.toDto(umaScanRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaScanRecordDTO findById(Integer id) {
        UmaScanRecord umaScanRecord = umaScanRecordRepository.findById(id).orElseGet(UmaScanRecord::new);
        ValidationUtil.isNull(umaScanRecord.getId(),"UmaScanRecord","id",id);
        return umaScanRecordMapper.toDto(umaScanRecord);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaScanRecordDTO create(UmaScanRecord resources) {
        return umaScanRecordMapper.toDto(umaScanRecordRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaScanRecord resources) {
        UmaScanRecord umaScanRecord = umaScanRecordRepository.findById(resources.getId()).orElseGet(UmaScanRecord::new);
        ValidationUtil.isNull( umaScanRecord.getId(),"UmaScanRecord","id",resources.getId());
        umaScanRecord.copy(resources);
        umaScanRecordRepository.save(umaScanRecord);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaScanRecordRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaScanRecordDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaScanRecordDTO umaScanRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("单号", umaScanRecord.getScanNumber());
            map.put(" labelId",  umaScanRecord.getLabelId());
            map.put("扫描员", umaScanRecord.getScanUser());
            map.put("扫描时间", umaScanRecord.getScanTime());
            map.put("扫描类型", umaScanRecord.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}