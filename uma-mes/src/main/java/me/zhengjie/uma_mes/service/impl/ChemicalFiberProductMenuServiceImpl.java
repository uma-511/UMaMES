package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductMenuRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductMenuService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductMenuMapper;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "chemicalFiberProductionMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductMenuServiceImpl implements ChemicalFiberProductMenuService {

    private final ChemicalFiberProductMenuRepository chemicalFiberProductMenuRepository;

    private final ChemicalFiberProductMenuMapper chemicalFiberProductMenuMapper;

    @Autowired
    private ChemicalFiberProductRepository chemicalFiberProductRepository;

    public ChemicalFiberProductMenuServiceImpl(ChemicalFiberProductMenuRepository chemicalFiberProductMenuRepository,
                                               ChemicalFiberProductMenuMapper chemicalFiberProductMenuMapper){
        this.chemicalFiberProductMenuRepository = chemicalFiberProductMenuRepository;
        this.chemicalFiberProductMenuMapper = chemicalFiberProductMenuMapper;
    }

    public List<ChemicalFiberProductMenuDTO> queryAll(ChemicalFiberProductMenuQueryCriteria criteria){
        //List<ChemicalFiberProductMenu> b = chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return chemicalFiberProductMenuMapper.toDto(chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductMenuDTO create(ChemicalFiberProductMenu resources) {

        ChemicalFiberProductMenuQueryCriteria criteria = new ChemicalFiberProductMenuQueryCriteria();
        criteria.setProductMenusName(resources.getProductMenusName());
        List<ChemicalFiberProductMenu> list =  chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        if (list.size() > 0) {
            throw new BadRequestException("目录名称重复");
        }

        return chemicalFiberProductMenuMapper.toDto(chemicalFiberProductMenuRepository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delect(ChemicalFiberProductMenu resources) {

        ChemicalFiberProductQueryCriteria criteria = new ChemicalFiberProductQueryCriteria();
        criteria.setMenus(resources.getProductMenusName());
        List<ChemicalFiberProduct> list = chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        if (list.size() > 0) {
            throw new BadRequestException("该目录还有产品无法删除");
        }
        ChemicalFiberProductMenuQueryCriteria criteria1 = new ChemicalFiberProductMenuQueryCriteria();
        criteria1.setProductMenusName(resources.getProductMenusName());
        List<ChemicalFiberProductMenu> menu =  chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria1,criteriaBuilder));
        chemicalFiberProductMenuRepository.deleteAll(menu);

    }




}
