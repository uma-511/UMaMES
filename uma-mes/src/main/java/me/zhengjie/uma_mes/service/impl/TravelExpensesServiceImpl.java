package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.TravelExpenses;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.TravelExpensesRepository;
import me.zhengjie.uma_mes.service.TravelExpensesService;
import me.zhengjie.uma_mes.service.dto.TravelExpensesDTO;
import me.zhengjie.uma_mes.service.dto.TravelExpensesQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.TravelExpensesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author wave
* @date 2020-09-02
*/
@Service
@CacheConfig(cacheNames = "travelExpenses")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TravelExpensesServiceImpl implements TravelExpensesService {

    private final TravelExpensesRepository travelExpensesRepository;

    private final TravelExpensesMapper travelExpensesMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public TravelExpensesServiceImpl(TravelExpensesRepository travelExpensesRepository, TravelExpensesMapper travelExpensesMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.travelExpensesRepository = travelExpensesRepository;
        this.travelExpensesMapper = travelExpensesMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    public Map<String,Object> queryAll(TravelExpensesQueryCriteria criteria, Pageable pageable){
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<TravelExpenses> page = travelExpensesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(travelExpensesMapper::toDto));
    }

    @Override
    public List<TravelExpensesDTO> queryAll(TravelExpensesQueryCriteria criteria){
        return travelExpensesMapper.toDto(travelExpensesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public TravelExpensesDTO findById(Integer id) {
        TravelExpenses travelExpenses = travelExpensesRepository.findById(id).orElseGet(TravelExpenses::new);
        ValidationUtil.isNull(travelExpenses.getId(),"TravelExpenses","id",id);
        return travelExpensesMapper.toDto(travelExpenses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TravelExpensesDTO create(TravelExpenses resources) {
        TravelExpensesQueryCriteria travelExpensesQueryCriteria = new TravelExpensesQueryCriteria();
        travelExpensesQueryCriteria.setStartPlaceAccurate(resources.getStartPlace());
        travelExpensesQueryCriteria.setEndPlaceAccurate(resources.getEndPlace());
        List<TravelExpenses> travelExpensesList = travelExpensesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,travelExpensesQueryCriteria,criteriaBuilder));
        if ( null != travelExpensesList ) {
            for ( TravelExpenses t:travelExpensesList ) {
                if( null != resources.getTractorPrice() && !resources.getTractorPrice().equals(new BigDecimal(0))){
                    t.setTractorPrice(resources.getTractorPrice());
                }
                if( null != resources.getTankPrice() && !resources.getTankPrice().equals(new BigDecimal(0))){
                    t.setTankPrice(resources.getTankPrice());
                }
                if( null != resources.getVanPrice() && !resources.getVanPrice().equals(new BigDecimal(0))){
                    t.setVanPrice(resources.getVanPrice());
                }
                t.setEnable(Boolean.TRUE);
                travelExpensesRepository.save(t);
                return travelExpensesMapper.toDto(t);
            }
        }
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setEnable(Boolean.TRUE);
        initPrice(resources);
        return travelExpensesMapper.toDto(travelExpensesRepository.save(resources));
    }

    private void initPrice(TravelExpenses resources){
        if ( null == resources.getVanPrice() ) {
            resources.setVanPrice(new BigDecimal(0));
        }
        if ( null == resources.getTankPrice() ) {
            resources.setTankPrice(new BigDecimal(0));
        }
        if ( null == resources.getTractorPrice() ) {
            resources.setTractorPrice(new BigDecimal(0));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TravelExpenses resources) {
        TravelExpenses travelExpenses = travelExpensesRepository.findById(resources.getId()).orElseGet(TravelExpenses::new);
        ValidationUtil.isNull( travelExpenses.getId(),"TravelExpenses","id",resources.getId());
        initPrice(resources);
        travelExpenses.copy(resources);
        travelExpensesRepository.save(travelExpenses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        TravelExpenses travelExpenses = travelExpensesRepository.findById(id).orElseGet(TravelExpenses::new);
        travelExpenses.setEnable(Boolean.FALSE);
        travelExpensesRepository.save(travelExpenses);
    }

}