package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Revenue;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.RevenueRepository;
import me.zhengjie.uma_mes.service.RevenueService;
import me.zhengjie.uma_mes.service.dto.RevenueDTO;
import me.zhengjie.uma_mes.service.dto.RevenueQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.RevenueMapper;
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
* @date 2020-08-14
*/
@Service
@CacheConfig(cacheNames = "revenue")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;

    private final RevenueMapper revenueMapper;

    public RevenueServiceImpl(RevenueRepository revenueRepository, RevenueMapper revenueMapper) {
        this.revenueRepository = revenueRepository;
        this.revenueMapper = revenueMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(RevenueQueryCriteria criteria, Pageable pageable){
        Page<Revenue> page = revenueRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(revenueMapper::toDto));
    }

    @Override
    @Cacheable
    public List<RevenueDTO> queryAll(RevenueQueryCriteria criteria){
        return revenueMapper.toDto(revenueRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public RevenueDTO findById(Integer id) {
        Revenue revenue = revenueRepository.findById(id).orElseGet(Revenue::new);
        ValidationUtil.isNull(revenue.getId(),"Revenue","id",id);
        return revenueMapper.toDto(revenue);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RevenueDTO create(Revenue resources) {
        return revenueMapper.toDto(revenueRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Revenue resources) {
        Revenue revenue = revenueRepository.findById(resources.getId()).orElseGet(Revenue::new);
        ValidationUtil.isNull( revenue.getId(),"Revenue","id",resources.getId());
        revenue.copy(resources);
        revenueRepository.save(revenue);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        revenueRepository.deleteById(id);
    }


    @Override
    public void download(List<RevenueDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RevenueDTO revenue : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("收款分类", revenue.getRecivedType());
            map.put("收款明细", revenue.getRecivedDetail());
            map.put("收款金额", revenue.getMoney());
            map.put("创建日期", revenue.getCreateDate());
            map.put("收款日期", revenue.getRecivedDate());
            map.put("操作人员", revenue.getCreateUser());
            map.put("客户id", revenue.getCustomerId());
            map.put("客户名称", revenue.getCustomerName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}