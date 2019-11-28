package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.utils.DownloadUtil;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNoteMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryNote")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryNoteServiceImpl implements ChemicalFiberDeliveryNoteService {

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    private final ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper;

    private final CustomerService customerService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    public ChemicalFiberDeliveryNoteServiceImpl(ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository,
                                                ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper,
                                                CustomerService customerService,
                                                ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService) {
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
        this.chemicalFiberDeliveryNoteMapper = chemicalFiberDeliveryNoteMapper;
        this.customerService = customerService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberDeliveryNote> page = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryNoteMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberDeliveryNoteDTO> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria){
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryNoteDTO findById(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull(chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",id);
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNote);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNoteDTO create(ChemicalFiberDeliveryNote resources) {
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNoteRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryNote resources) {
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull( chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",resources.getId());
        chemicalFiberDeliveryNote.copy(resources);
        chemicalFiberDeliveryNote.setCreateDate(new Timestamp(System.currentTimeMillis()));
        chemicalFiberDeliveryNote.setCreateUser(SecurityUtils.getUsername());
        chemicalFiberDeliveryNote.setCustomerId(customerDTO.getId());
        chemicalFiberDeliveryNote.setCustomerCode(customerDTO.getCode());
        chemicalFiberDeliveryNote.setCustomerName(customerDTO.getName());
        chemicalFiberDeliveryNote.setCustomerAddress(customerDTO.getAddress());
        chemicalFiberDeliveryNote.setContactPhone(customerDTO.getContactPhone());
        chemicalFiberDeliveryNote.setContacts(customerDTO.getContacts());
        chemicalFiberDeliveryNoteRepository.save(chemicalFiberDeliveryNote);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberDeliveryNoteRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberDeliveryNoteDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNote : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("出库单号", chemicalFiberDeliveryNote.getScanNumber());
            map.put("客户id", chemicalFiberDeliveryNote.getCustomerId());
            map.put("客户名称", chemicalFiberDeliveryNote.getCustomerName());
            map.put("客户编号", chemicalFiberDeliveryNote.getCustomerCode());
            map.put("客户地址", chemicalFiberDeliveryNote.getCustomerAddress());
            map.put("联系人", chemicalFiberDeliveryNote.getContacts());
            map.put("联系电话", chemicalFiberDeliveryNote.getContactPhone());
            map.put("总成本", chemicalFiberDeliveryNote.getTotalCost());
            map.put("总价", chemicalFiberDeliveryNote.getTotalPrice());
            map.put("备注", chemicalFiberDeliveryNote.getRemark());
            map.put("业务员", chemicalFiberDeliveryNote.getSeller());
            map.put("仓管员", chemicalFiberDeliveryNote.getStoreKeeper());
            map.put("制单日期", chemicalFiberDeliveryNote.getCreateDate());
            map.put("制单人", chemicalFiberDeliveryNote.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void deliveryNoteStoredProcedure(String scanNumber) {
        chemicalFiberDeliveryNoteRepository.deliveryNoteStoredProcedure(scanNumber);
    }

    @Override
    public void downloadDeliveryNote(Integer id, HttpServletResponse response) throws IOException, IllegalAccessException {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setScanNumber(chemicalFiberDeliveryNote.getScanNumber());
        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
        DownloadUtil.downloadDeliveryNoteExcel(chemicalFiberDeliveryNote, chemicalFiberDeliveryDetailDTOS, response);
    }
}