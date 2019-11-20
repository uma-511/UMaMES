package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaDeliveryNote;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.UmaDeliveryNoteService;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteDTO;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaDeliveryNoteMapper;
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
@CacheConfig(cacheNames = "umaDeliveryNote")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaDeliveryNoteServiceImpl implements UmaDeliveryNoteService {

    private final UmaDeliveryNoteRepository umaDeliveryNoteRepository;

    private final UmaDeliveryNoteMapper umaDeliveryNoteMapper;

    public UmaDeliveryNoteServiceImpl(UmaDeliveryNoteRepository umaDeliveryNoteRepository, UmaDeliveryNoteMapper umaDeliveryNoteMapper) {
        this.umaDeliveryNoteRepository = umaDeliveryNoteRepository;
        this.umaDeliveryNoteMapper = umaDeliveryNoteMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaDeliveryNoteQueryCriteria criteria, Pageable pageable){
        Page<UmaDeliveryNote> page = umaDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaDeliveryNoteMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaDeliveryNoteDTO> queryAll(UmaDeliveryNoteQueryCriteria criteria){
        return umaDeliveryNoteMapper.toDto(umaDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaDeliveryNoteDTO findById(Integer id) {
        UmaDeliveryNote umaDeliveryNote = umaDeliveryNoteRepository.findById(id).orElseGet(UmaDeliveryNote::new);
        ValidationUtil.isNull(umaDeliveryNote.getId(),"UmaDeliveryNote","id",id);
        return umaDeliveryNoteMapper.toDto(umaDeliveryNote);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaDeliveryNoteDTO create(UmaDeliveryNote resources) {
        return umaDeliveryNoteMapper.toDto(umaDeliveryNoteRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaDeliveryNote resources) {
        UmaDeliveryNote umaDeliveryNote = umaDeliveryNoteRepository.findById(resources.getId()).orElseGet(UmaDeliveryNote::new);
        ValidationUtil.isNull( umaDeliveryNote.getId(),"UmaDeliveryNote","id",resources.getId());
        umaDeliveryNote.copy(resources);
        umaDeliveryNoteRepository.save(umaDeliveryNote);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaDeliveryNoteRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaDeliveryNoteDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaDeliveryNoteDTO umaDeliveryNote : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("出库单号", umaDeliveryNote.getScanNumber());
            map.put("客户id", umaDeliveryNote.getCustomerId());
            map.put("客户名称", umaDeliveryNote.getCustomerName());
            map.put("客户编号", umaDeliveryNote.getCustomerCode());
            map.put("客户地址", umaDeliveryNote.getCustomerAddress());
            map.put("联系人", umaDeliveryNote.getContacts());
            map.put("联系电话", umaDeliveryNote.getContactPhone());
            map.put("总成本", umaDeliveryNote.getTotalCost());
            map.put("总价", umaDeliveryNote.getTotalPrice());
            map.put("备注", umaDeliveryNote.getRemark());
            map.put("业务员", umaDeliveryNote.getSeller());
            map.put("仓管员", umaDeliveryNote.getStoreKeeper());
            map.put("制单日期", umaDeliveryNote.getCreateDate());
            map.put("制单人", umaDeliveryNote.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}