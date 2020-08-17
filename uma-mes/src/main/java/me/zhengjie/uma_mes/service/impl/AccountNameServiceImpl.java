package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.AccountName;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.AccountNameRepository;
import me.zhengjie.uma_mes.service.AccountNameService;
import me.zhengjie.uma_mes.service.dto.AccountNameDTO;
import me.zhengjie.uma_mes.service.dto.AccountNameQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.AccountNameMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Service
@CacheConfig(cacheNames = "accountName")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AccountNameServiceImpl implements AccountNameService {

    private final AccountNameRepository accountNameRepository;

    private final AccountNameMapper accountNameMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public AccountNameServiceImpl(AccountNameRepository accountNameRepository, AccountNameMapper accountNameMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.accountNameRepository = accountNameRepository;
        this.accountNameMapper = accountNameMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(AccountNameQueryCriteria criteria, Pageable pageable){
        Page<AccountName> page = accountNameRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(accountNameMapper::toDto));
    }

    @Override
    @Cacheable
    public List<AccountNameDTO> queryAll(AccountNameQueryCriteria criteria){
        return accountNameMapper.toDto(accountNameRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public AccountNameDTO findById(Integer id) {
        AccountName accountName = accountNameRepository.findById(id).orElseGet(AccountName::new);
        ValidationUtil.isNull(accountName.getId(),"AccountName","id",id);
        return accountNameMapper.toDto(accountName);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public AccountNameDTO create(AccountName resources) {
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setEnable(Boolean.TRUE);
        return accountNameMapper.toDto(accountNameRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(AccountName resources) {
        AccountName accountName = accountNameRepository.findById(resources.getId()).orElseGet(AccountName::new);
        ValidationUtil.isNull( accountName.getId(),"AccountName","id",resources.getId());
        accountName.copy(resources);
        accountNameRepository.save(accountName);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        accountNameRepository.deleteById(id);
    }


    @Override
    public void download(List<AccountNameDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AccountNameDTO accountName : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("账户名称", accountName.getName());
            map.put("创建时间", accountName.getCreateTime());
            map.put("创建用户", accountName.getCreateUser());
            map.put("是否可用", accountName.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}