package me.zhengjie.uma_mes.service.impl;

import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductMenuRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.ChemicalFiberStockService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductMenuMapper;
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
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberProduct")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductServiceImpl implements ChemicalFiberProductService {

    private final ChemicalFiberProductRepository chemicalFiberProductRepository;

    private final ChemicalFiberProductMapper chemicalFiberProductMapper;

    private final ChemicalFiberStockService chemicalFiberStockService;



    public ChemicalFiberProductServiceImpl(
            ChemicalFiberProductRepository chemicalFiberProductRepository,
            ChemicalFiberProductMapper chemicalFiberProductMapper,
            ChemicalFiberStockService chemicalFiberStockService,
            ChemicalFiberProductMenuMapper chemicalFiberProductMenuMapper,
            ChemicalFiberProductMenuRepository chemicalFiberProductMenuRepository) {
        this.chemicalFiberProductRepository = chemicalFiberProductRepository;
        this.chemicalFiberProductMapper = chemicalFiberProductMapper;
        this.chemicalFiberStockService = chemicalFiberStockService;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberProductQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        Page<ChemicalFiberProduct> page = chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        BigDecimal zero = new BigDecimal(0.00);
        for(ChemicalFiberProduct c : page){
            if(null != c.getMachining() && c.getMachining().compareTo(zero) == 0){
                c.setMachining(null);
            }
        }
        return PageUtil.toPage(page.map(chemicalFiberProductMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberProductDTO> queryAll(ChemicalFiberProductQueryCriteria criteria){
        criteria.setDelFlag(0);
        List<ChemicalFiberProductDTO> product = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        return product;
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFiberProductDTO findById(Integer id) {
        ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(id).orElseGet(ChemicalFiberProduct::new);
        ValidationUtil.isNull(chemicalFiberProduct.getId(),"ChemicalFiberProduct","id",id);
        return chemicalFiberProductMapper.toDto(chemicalFiberProduct);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductDTO create(ChemicalFiberProduct resources) {
        ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria.setModelAccurate(resources.getModel());
        chemicalFiberProductQueryCriteria.setDelFlag(0);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, chemicalFiberProductQueryCriteria, criteriaBuilder)));
        if (chemicalFiberProductDTOS.size() > 0) {
            throw new BadRequestException("请确保产品型号唯一");
        }
        /*ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria2 = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria2.setNameAccurate(resources.getName());
        chemicalFiberProductQueryCriteria2.setDelFlag(0);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS2 = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, chemicalFiberProductQueryCriteria2, criteriaBuilder)));
        if (chemicalFiberProductDTOS2.size() > 0) {
            throw new BadRequestException("请确保产品名称唯一");
        }
        else {*/

        resources.setCreateUser(SecurityUtils.getUsername());
        resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resources.setDelFlag(0);
        ChemicalFiberProductDTO chemicalFiberProductDTO = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.save(resources));
        return chemicalFiberProductDTO;

            /*// 添加库存
            ChemicalFiberStock chemicalFiberStock = new ChemicalFiberStock();
            chemicalFiberStock.setProdId(chemicalFiberProductDTO.getId());
            chemicalFiberStock.setProdModel(chemicalFiberProductDTO.getModel());
            chemicalFiberStock.setProdName(chemicalFiberProductDTO.getName());
            chemicalFiberStock.setProdColor(chemicalFiberProductDTO.getColor());
            chemicalFiberStock.setProdFineness(chemicalFiberProductDTO.getFineness());
            chemicalFiberStockService.create(chemicalFiberStock);
            chemicalFiberStockService.stockTask();*/
        /*}*/
    }

    @Override
    public ChemicalFiberProductDTO createForTerminal(ChemicalFiberProduct resources) {
        return chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberProduct resources) {
        ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria.setModelAccurate(resources.getModel());
        chemicalFiberProductQueryCriteria.setDelFlag(0);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS = chemicalFiberProductMapper.toDto(chemicalFiberProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, chemicalFiberProductQueryCriteria, criteriaBuilder)));
        if (chemicalFiberProductDTOS.size() > 0 && chemicalFiberProductDTOS.get(0).getModel().equals(resources.getModel()) && !resources.getId().equals(chemicalFiberProductDTOS.get(0).getId())) {
            throw new BadRequestException("请确保产品型号唯一");
        } else {
            ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(resources.getId()).orElseGet(ChemicalFiberProduct::new);
            ValidationUtil.isNull( chemicalFiberProduct.getId(),"ChemicalFiberProduct","id",resources.getId());
            chemicalFiberProduct.copy(resources);
            resources.setCreateUser(SecurityUtils.getUsername());
            ChemicalFiberProduct upDateChemicalFiberProduct = chemicalFiberProductRepository.save(chemicalFiberProduct);

            ChemicalFiberStockQueryCriteria chemicalFiberStockQueryCriteria = new ChemicalFiberStockQueryCriteria();
            chemicalFiberStockQueryCriteria.setProdId(upDateChemicalFiberProduct.getId());
            List<ChemicalFiberStockDTO> chemicalFiberStockDTOS = chemicalFiberStockService.queryAll(chemicalFiberStockQueryCriteria);
            if (chemicalFiberStockDTOS.size() > 0) {
                ChemicalFiberStockDTO chemicalFiberStockDTO = chemicalFiberStockDTOS.get(0);
                chemicalFiberStockDTO.setProdId(upDateChemicalFiberProduct.getId());
                chemicalFiberStockDTO.setProdModel(upDateChemicalFiberProduct.getModel());
                chemicalFiberStockDTO.setProdName(upDateChemicalFiberProduct.getName());
                chemicalFiberStockDTO.setProdColor(upDateChemicalFiberProduct.getColor());
                chemicalFiberStockDTO.setProdFineness(upDateChemicalFiberProduct.getFineness());

                ChemicalFiberStock chemicalFiberStock = new ChemicalFiberStock();
                ObjectTransfer.transValue(chemicalFiberStockDTO, chemicalFiberStock);
                chemicalFiberStockService.update(chemicalFiberStock);
            }
        }
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberProduct chemicalFiberProduct = chemicalFiberProductRepository.findById(id).orElseGet(ChemicalFiberProduct::new);
        Double countTotalStock = 0.00;
        if(null != chemicalFiberProductRepository.countStock(chemicalFiberProduct.getModel())){
            countTotalStock = Double.parseDouble(chemicalFiberProductRepository.countStock(chemicalFiberProduct.getModel()));
        }
        if (countTotalStock == 0){
            //chemicalFiberProduct.setDelFlag(1);
            chemicalFiberProductRepository.delete(chemicalFiberProduct);
        }else{
            throw new BadRequestException("当前产品库存数量不为0，无法删除");
        }
    }


    @Override
    public void download(List<ChemicalFiberProductDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberProductDTO chemicalFiberProduct : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品型号", chemicalFiberProduct.getModel());
            map.put("产品名称", chemicalFiberProduct.getName());
            map.put("色号", chemicalFiberProduct.getColor());
            map.put("纤度", chemicalFiberProduct.getFineness());
            map.put("创建日期", chemicalFiberProduct.getCreateDate());
            map.put("创建人", chemicalFiberProduct.getCreateUser());
            map.put("删除标识", chemicalFiberProduct.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}