package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventory;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockLnventoryDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockLnventoryRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberStockLnventoryDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockLnventoryDetailMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockMapper;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "chemicalFiberStockLnventoryDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberStockLnventoryDetailServiceImpl implements ChemicalFiberStockLnventoryDetailService {

    final ChemicalFiberStockMapper chemicalFiberStockMapper;

    final ChemicalFiberStockRepository chemicalFiberStockRepository;

    final ChemicalFiberStockLnventoryDetailMapper chemicalFiberStockLnventoryDetailMapper;

    final ChemicalFiberStockLnventoryDetailRepository chemicalFiberStockLnventoryDetailRepository;

    final ChemicalFiberStockLnventoryRepository chemicalFiberStockLnventoryRepository;

    public ChemicalFiberStockLnventoryDetailServiceImpl(ChemicalFiberStockMapper chemicalFiberStockMapper,
                                                        ChemicalFiberStockRepository chemicalFiberStockRepository,
                                                        ChemicalFiberStockLnventoryDetailMapper chemicalFiberStockLnventoryDetailMapper,
                                                        ChemicalFiberStockLnventoryRepository chemicalFiberStockLnventoryRepository,
                                                        ChemicalFiberStockLnventoryDetailRepository chemicalFiberStockLnventoryDetailRepository) {
        this.chemicalFiberStockMapper = chemicalFiberStockMapper;
        this.chemicalFiberStockRepository = chemicalFiberStockRepository;
        this.chemicalFiberStockLnventoryDetailMapper = chemicalFiberStockLnventoryDetailMapper;
        this.chemicalFiberStockLnventoryDetailRepository = chemicalFiberStockLnventoryDetailRepository;
        this.chemicalFiberStockLnventoryRepository = chemicalFiberStockLnventoryRepository;
    }

    public List<ChemicalFiberStockLnventoryDetailDTO> queryAll() {
        List<ChemicalFiberStockLnventoryDetailDTO> DetailDTO = new ArrayList<>();
        List<ChemicalFiberStockDTO> stock = chemicalFiberStockMapper.toDto(chemicalFiberStockRepository.findAll());

        for (ChemicalFiberStockDTO dto : stock) {
            ChemicalFiberStockLnventoryDetailDTO dto1 = new ChemicalFiberStockLnventoryDetailDTO();
            dto1.setProdModel(dto.getProdModel());
            dto1.setStockId(dto.getId());
            dto1.setProdName(dto.getProdName());
            dto1.setUnit(dto.getProdUnit());
            dto1.setProdNumber(dto.getTotalNumber());
            DetailDTO.add(dto1);
        }
        return DetailDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ChemicalFiberStockLnventoryDetailDTO> create(List<ChemicalFiberStockLnventoryDetail> resources) {
        /*List<ChemicalFiberStockLnventoryDetail> lnventory = resources.get(0);
        Integer lnventoryId = resources.get(1);
        for (ChemicalFiberStockLnventoryDetail dto : lnventory) {
            dto.setlnventoryId(lnventoryId);
            lnventoryDTO.add(chemicalFiberStockLnventoryDetailMapper.toDto(chemicalFiberStockLnventoryDetailRepository.save(resources)));
        }*/
        Integer lnventorySurplus = 0;
        Integer lnventoryLoss = 0;
        for (ChemicalFiberStockLnventoryDetail dto : resources) {
            Integer a = 0;
            Integer b = 0;
            if (dto.getLnventorySurplus() != null) {
                a =  dto.getLnventorySurplus();
                lnventorySurplus += a;
            }
            if (dto.getLnventoryLoss() != null) {
                b = dto.getLnventoryLoss();
                lnventoryLoss += b;
            }
        }
        ChemicalFiberStockLnventory Stock = chemicalFiberStockLnventoryRepository.findById(resources.get(0).getLnventoryId()).orElseGet(ChemicalFiberStockLnventory::new);
        ValidationUtil.isNull( Stock.getId(),"chemicalFiberStockWarehousingDetail","id",resources.get(0).getLnventoryId());
        Stock.setLnventorySurplus(lnventorySurplus);
        Stock.setLnventoryLoss(lnventoryLoss);
        chemicalFiberStockLnventoryRepository.save(Stock);
        return  chemicalFiberStockLnventoryDetailMapper.toDto(chemicalFiberStockLnventoryDetailRepository.saveAll(resources));
    }

    public List<ChemicalFiberStockLnventoryDetailDTO> queryAllList(ChemicalFiberStockLnventory resources) {
        ChemicalFiberStockLnventoryDetailQueryCriteria criteria = new ChemicalFiberStockLnventoryDetailQueryCriteria();
        criteria.setLnventoryId(resources.getId());
        return chemicalFiberStockLnventoryDetailMapper.toDto(chemicalFiberStockLnventoryDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));

    }

    @Transactional(rollbackFor = Exception.class)
    public void update(List<ChemicalFiberStockLnventoryDetail> resources) {
        /*Map<Integer, List<ChemicalFiberStockLnventoryDetail>> a = new HashMap<>();

        for (ChemicalFiberStockLnventoryDetail dto : resources) {
            Integer StockiId = dto.getStockId();
            List<ChemicalFiberStockLnventoryDetail> cataLogParentIdList = a.get(StockiId);
            if (cataLogParentIdList == null) {
                cataLogParentIdList = new ArrayList<>();
                a.put(StockiId, cataLogParentIdList);
            }
            cataLogParentIdList.add(dto);
        }*/
        Integer lnventorySurplus = 0;
        Integer lnventoryLoss = 0;
        for (ChemicalFiberStockLnventoryDetail dto : resources) {
            Integer a = 0;
            Integer b = 0;
            if (dto.getLnventorySurplus() != null) {
                a =  dto.getLnventorySurplus();
                lnventorySurplus += a;
            }
            if (dto.getLnventoryLoss() != null) {
                b = dto.getLnventoryLoss();
                lnventoryLoss += b;
            }
        }
        ChemicalFiberStockLnventory Stock = chemicalFiberStockLnventoryRepository.findById(resources.get(0).getLnventoryId()).orElseGet(ChemicalFiberStockLnventory::new);
        ValidationUtil.isNull( Stock.getId(),"chemicalFiberStockWarehousingDetail","id",resources.get(0).getLnventoryId());
        Stock.setLnventorySurplus(lnventorySurplus);
        Stock.setLnventoryLoss(lnventoryLoss);
        chemicalFiberStockLnventoryDetailRepository.saveAll(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void balance(List<ChemicalFiberStockLnventoryDetail> resources) {
        for (ChemicalFiberStockLnventoryDetail dto : resources) {
            ChemicalFiberStock stock = chemicalFiberStockRepository.findById(dto.getStockId()).orElseGet(ChemicalFiberStock::new);
            int surplus = dto.getLnventorySurplus();
            int loss = dto.getLnventoryLoss();
            int number = stock.getTotalNumber();
            int stockNumber = 0;
            number += surplus;
            number -= loss;
            stock.setTotalNumber(number);
            chemicalFiberStockRepository.save(stock);
        }
        ChemicalFiberStockLnventory lnverntory = chemicalFiberStockLnventoryRepository.findById(resources.get(0).getLnventoryId()).orElseGet(ChemicalFiberStockLnventory::new);
        lnverntory.setLnventoryStatus(2);
        chemicalFiberStockLnventoryRepository.save(lnverntory);

    }
}
