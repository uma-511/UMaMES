package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNotePayDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNotePayDetailService;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailQueryCriteria;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryDetailMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNoteMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNotePayDetailMapper;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.utils.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Xie Ji Biao
 * @date 2020-08-08
 */
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryNotePayDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryNotePayDetailServiceImpl implements ChemicalFiberDeliveryNotePayDetailService {

    private final ChemicalFiberDeliveryNotePayDetailRepository chemicalFiberDeliveryNotePayDetailRepository;

    private final ChemicalFiberDeliveryNotePayDetailMapper chemicalFiberDeliveryNotePayDetailMapper;

    private final CustomerService customerService;

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    private final CustomerMapper customerMapper;

    private final ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper;

    public ChemicalFiberDeliveryNotePayDetailServiceImpl(ChemicalFiberDeliveryNotePayDetailRepository chemicalFiberDeliveryNotePayDetailRepository, ChemicalFiberDeliveryNotePayDetailMapper chemicalFiberDeliveryNotePayDetailMapper, CustomerService customerService, ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository, CustomerMapper customerMapper, ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper, ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper) {
        this.chemicalFiberDeliveryNotePayDetailRepository = chemicalFiberDeliveryNotePayDetailRepository;
        this.chemicalFiberDeliveryNotePayDetailMapper = chemicalFiberDeliveryNotePayDetailMapper;
        this.customerService = customerService;
        this.chemicalFiberDeliveryNoteService = chemicalFiberDeliveryNoteService;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
        this.customerMapper = customerMapper;
        this.chemicalFiberDeliveryNoteMapper = chemicalFiberDeliveryNoteMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFiberDeliveryNotePayDetail> page = chemicalFiberDeliveryNotePayDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryNotePayDetailMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberDeliveryNotePayDetailDTO> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria){
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable
    public List<ChemicalFiberDeliveryNotePayDetailDTO> findListByScanNumber(String scanNumber){
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.getListByScanNumber(scanNumber));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryNotePayDetailDTO findById(Integer id) {
        ChemicalFiberDeliveryNotePayDetail chemicalFiberDeliveryNotePayDetail = chemicalFiberDeliveryNotePayDetailRepository.findById(id).orElseGet(ChemicalFiberDeliveryNotePayDetail::new);
        ValidationUtil.isNull(chemicalFiberDeliveryNotePayDetail.getId(),"ChemicalFiberDeliveryNotePayDetail","id",id);
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNotePayDetailDTO create(ChemicalFiberDeliveryNotePayDetail resources) {

        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNotePayDetailDTO doPay(ChemicalFiberDeliveryNotePayDetail resources) {
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNoteDTO = chemicalFiberDeliveryNoteService.findByScanNumber(resources.getScanNumber());
        if(null != customerDTO.getAccount() ) {
           if(customerDTO.getAccount().compareTo(resources.getAmount()) == -1) {
               throw new BadRequestException("客户账号余额不足，无法结款");
           }else{
               if(null != chemicalFiberDeliveryNoteDTO && chemicalFiberDeliveryNoteDTO.getBalance().compareTo(resources.getAmount()) == -1) {
                   throw new BadRequestException("支付金额超过待付金额，无法结款");
               }else {
                   customerDTO.setAccount(customerDTO.getAccount().subtract(resources.getAmount()));
                   customerService.updateAccount(customerMapper.toEntity(customerDTO));
                   if(null != chemicalFiberDeliveryNoteDTO && chemicalFiberDeliveryNoteDTO.getBalance().compareTo(resources.getAmount()) == 0) {
                       //完结
                       chemicalFiberDeliveryNoteDTO.setNoteStatus(6);
                   }else
                   {
                       //结款中
                       chemicalFiberDeliveryNoteDTO.setNoteStatus(5);
                   }
                   chemicalFiberDeliveryNoteDTO.setBalance(chemicalFiberDeliveryNoteDTO.getBalance().subtract(resources.getAmount()));
                   chemicalFiberDeliveryNoteService.updateBalance(chemicalFiberDeliveryNoteMapper.toEntity(chemicalFiberDeliveryNoteDTO));
               }
           }
        }
        else {
            throw new BadRequestException("客户账号余额不足，无法结款");
        }
        resources.setCreateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        resources.setInputUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNotePayDetailDTO finalPay(ChemicalFiberDeliveryNotePayDetail resources) {
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNoteDTO = chemicalFiberDeliveryNoteService.findByScanNumber(resources.getScanNumber());
        if(null != customerDTO.getAccount() ) {
            if(customerDTO.getAccount().compareTo(resources.getAmount()) == -1) {
                throw new BadRequestException("客户账号余额不足，无法结款");
            }else{
                if(null != chemicalFiberDeliveryNoteDTO && chemicalFiberDeliveryNoteDTO.getBalance().compareTo(resources.getAmount()) == -1) {
                    throw new BadRequestException("支付金额超过待付金额，无法结款");
                }else {
                    customerDTO.setAccount(customerDTO.getAccount().subtract(resources.getAmount()));
                    customerService.updateAccount(customerMapper.toEntity(customerDTO));
                    chemicalFiberDeliveryNoteDTO.setNoteStatus(6);
                    //计算余数并赋值
                    chemicalFiberDeliveryNoteDTO.setRemainder(chemicalFiberDeliveryNoteDTO.getBalance().subtract(resources.getAmount()));
                    chemicalFiberDeliveryNoteDTO.setBalance(new BigDecimal(0));
                    chemicalFiberDeliveryNoteService.updateBalance(chemicalFiberDeliveryNoteMapper.toEntity(chemicalFiberDeliveryNoteDTO));
                }
            }
        }
        resources.setCreateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        resources.setInputUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        return chemicalFiberDeliveryNotePayDetailMapper.toDto(chemicalFiberDeliveryNotePayDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryNotePayDetail resources) {
        ChemicalFiberDeliveryNotePayDetail chemicalFiberDeliveryNotePayDetail = chemicalFiberDeliveryNotePayDetailRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryNotePayDetail::new);
        ValidationUtil.isNull( chemicalFiberDeliveryNotePayDetail.getId(),"ChemicalFiberDeliveryNotePayDetail","id",resources.getId());
        chemicalFiberDeliveryNotePayDetail.copy(resources);
        chemicalFiberDeliveryNotePayDetailRepository.save(chemicalFiberDeliveryNotePayDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberDeliveryNotePayDetailRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberDeliveryNotePayDetailDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberDeliveryNotePayDetailDTO chemicalFiberDeliveryNotePayDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户id", chemicalFiberDeliveryNotePayDetail.getCustomerId());
            map.put("客户名称", chemicalFiberDeliveryNotePayDetail.getCustomerName());
            map.put("创建日期", chemicalFiberDeliveryNotePayDetail.getCreateDate());
            map.put("支付日期", chemicalFiberDeliveryNotePayDetail.getPayDate());
            map.put("操作人员", chemicalFiberDeliveryNotePayDetail.getInputUser());
            map.put("订单号", chemicalFiberDeliveryNotePayDetail.getScanNumber());
            map.put("本次结账金额", chemicalFiberDeliveryNotePayDetail.getAmount());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}