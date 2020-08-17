package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberStockWarehousingDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockWarehousingDetailMapper;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "chemicalFiberStockWarehousingDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberStockWarehousingDetailServiceImpl implements ChemicalFiberStockWarehousingDetailService {

    final ChemicalFiberStockWarehousingDetailMapper chemicalFiberStockWarehousingDetailMapper;

    final ChemicalFiberStockWarehousingDetailRepository chemicalFiberStockWarehousingDetailRepository;

    final ChemicalFiberStockRepository chemicalFiberStockRepository;

    public ChemicalFiberStockWarehousingDetailServiceImpl(ChemicalFiberStockWarehousingDetailMapper chemicalFiberStockWarehousingDetailMapper,
                                                          ChemicalFiberStockWarehousingDetailRepository chemicalFiberStockWarehousingDetailRepository,
                                                          ChemicalFiberStockRepository chemicalFiberStockRepository) {
        this.chemicalFiberStockWarehousingDetailMapper = chemicalFiberStockWarehousingDetailMapper;
        this.chemicalFiberStockWarehousingDetailRepository = chemicalFiberStockWarehousingDetailRepository;
        this.chemicalFiberStockRepository = chemicalFiberStockRepository;

    }

    public List<ChemicalFiberStockWarehousingDetailDTO> queryAll(ChemicalFiberStockWarehousingDetailQueryCriteria criteria) {
        return chemicalFiberStockWarehousingDetailMapper.toDto(chemicalFiberStockWarehousingDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }


    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberStockWarehousingDetailDTO create(ChemicalFiberStockWarehousingDetail resources) {
        return chemicalFiberStockWarehousingDetailMapper.toDto(chemicalFiberStockWarehousingDetailRepository.save(resources));
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(List<ChemicalFiberStockWarehousingDetail> resources) {
        for (ChemicalFiberStockWarehousingDetail detail : resources) {
            /*ChemicalFiberStock chemicalFiberStock = chemicalFiberStockRepository.findById(detail.getStockId()).orElseGet(ChemicalFiberStock::new);
            ValidationUtil.isNull( chemicalFiberStock.getId(),"chemicalFiberStock","id",detail.getStockId());
            String unit = detail.getUnit();
            if (unit.equals("吨")) {
                int ton = 0;
                if (chemicalFiberStock.getTonNumber() != null) {
                    ton = chemicalFiberStock.getTonNumber();
                }
                int ton1 = detail.getWarehousingNumber();
                ton += ton1;
                chemicalFiberStock.setTonNumber(ton);
                chemicalFiberStockRepository.save(chemicalFiberStock);
            } else {
                int branch = 0;
                if (chemicalFiberStock.getBranchNumber() != null) {
                    branch = chemicalFiberStock.getBranchNumber();
                }
                int branch1 = detail.getWarehousingNumber();
                branch += branch1;
                chemicalFiberStock.setBranchNumber(branch);
                chemicalFiberStockRepository.save(chemicalFiberStock);
            }*/
            ChemicalFiberStockWarehousingDetail chemicalFiberStockWarehousingDetail = chemicalFiberStockWarehousingDetailRepository.findById(detail.getId()).orElseGet(ChemicalFiberStockWarehousingDetail::new);
            ValidationUtil.isNull( chemicalFiberStockWarehousingDetail.getId(),"chemicalFiberStockWarehousingDetail","id",detail.getId());
            chemicalFiberStockWarehousingDetailRepository.save(detail);

        }
    }


    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberStockWarehousingDetailRepository.deleteById(id);
    }

}
