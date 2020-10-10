package me.zhengjie.uma_mes.service.impl;

import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberStockWarehousingService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockWarehousingMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
@CacheConfig(cacheNames = "chemicalFiberStockWarehousing")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberStockWarehousingServiceImpl implements ChemicalFiberStockWarehousingService {

    final ChemicalFiberStockWarehousingRepository chemicalFiberStockWarehousingRepository;

    final ChemicalFiberStockWarehousingMapper chemicalFiberStockWarehousingMapper;

    final ChemicalFiberStockRepository chemicalFiberStockRepository;

    final ChemicalFiberStockWarehousingDetailRepository chemicalFiberStockWarehousingDetailRepository;

    public ChemicalFiberStockWarehousingServiceImpl(ChemicalFiberStockWarehousingRepository chemicalFiberStockWarehousingRepository,
                                                    ChemicalFiberStockWarehousingMapper chemicalFiberStockWarehousingMapper,
                                                    ChemicalFiberStockRepository chemicalFiberStockRepository,
                                                    ChemicalFiberStockWarehousingDetailRepository chemicalFiberStockWarehousingDetailRepository) {
        this.chemicalFiberStockWarehousingRepository = chemicalFiberStockWarehousingRepository;
        this.chemicalFiberStockWarehousingMapper = chemicalFiberStockWarehousingMapper;
        this.chemicalFiberStockRepository = chemicalFiberStockRepository;
        this.chemicalFiberStockWarehousingDetailRepository = chemicalFiberStockWarehousingDetailRepository;

    }


    public Map<String, Object> queryAll(ChemicalFiberStockWarehousingQueryCriteria criteria, Pageable pageable) {
        //String name = criteria.getDriverMain();
        /*if (name != null) {
            criteria.setDriverDeputy(name);
            criteria.setEscortOne(name);
            criteria.setEscortTwo(name);
            Page<ChemicalFiberStockWarehousing> page = chemicalFiberStockWarehousingRepository.
        }*/
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Integer> invalidList = new ArrayList<>();
        invalidList.add(0);
        if (null != criteria.getQueryWithInvalid() && criteria.getQueryWithInvalid())
        {
            invalidList.add(1);
        }
        criteria.setInvalidList(invalidList);
        Page<ChemicalFiberStockWarehousing> page = chemicalFiberStockWarehousingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<ChemicalFiberStockWarehousing> list = page.getContent();
        List<Map<String, Object>> tonAndBranch = chemicalFiberStockWarehousingDetailRepository.getTonAndBranch();
        Map<Integer, Map<String, BigDecimal>> tonanBranchMap = new HashMap<>();
        for (Map<String, Object> dto : tonAndBranch) {
            Map<String, BigDecimal> dig = new HashMap<>();
            if (null == dto.get("ton")) {
                dig.put("吨", new BigDecimal(0));
            } else {
                dig.put("吨", new BigDecimal(dto.get("ton").toString()));
            }
            if (null == dto.get("branch")) {
                dig.put("支", new BigDecimal(0));
            } else {
                dig.put("支", new BigDecimal(dto.get("branch").toString()));
            }
            String tonAndBranchStr = dto.get("ton") + "吨" + "/" + dto.get("branch") + "支";
            tonanBranchMap.put((Integer)dto.get("warehousing_id"), dig);
        }
        List<ChemicalFiberStockWarehousingDTO> warehousingDTO = new ArrayList<>();
        for (ChemicalFiberStockWarehousing dto : list) {
            ChemicalFiberStockWarehousingDTO war = new ChemicalFiberStockWarehousingDTO();
            ObjectTransfer.transValue(dto, war);
            if (tonanBranchMap.containsKey(dto.getId())) {
                war.setTon(tonanBranchMap.get(dto.getId()).get("吨"));
                war.setBranch(tonanBranchMap.get(dto.getId()).get("支"));
            } else {
                war.setTon(new BigDecimal(0));
                war.setBranch(new BigDecimal(0));
            }
            if ( war.getTon() != null && war.getTon().compareTo(new BigDecimal(0.00)) == 0) {
                war.setTon(null);
            }
            if ( war.getBranch() != null && war.getBranch().compareTo(new BigDecimal(0.00)) == 0) {
                war.setBranch(null);
            }
            warehousingDTO.add(war);
        }
        /*return PageUtil.toPage(page.map(chemicalFiberStockWarehousingMapper::toDto));*/
        return PageUtil.toPage(new PageImpl(warehousingDTO, pageable, page.getTotalElements()));
    }


    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberStockWarehousingDTO create(ChemicalFiberStockWarehousing resources) {
        resources.setCreateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        if (resources.getWarehousingDate() == null) {
            resources.setWarehousingDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        }
        resources.setCreateUser(chemicalFiberStockWarehousingRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setWarehousingStatus(1);
        resources.setScanNumber(getScanNumber());
        resources.setInvalid(0);
        return chemicalFiberStockWarehousingMapper.toDto(chemicalFiberStockWarehousingRepository.save(resources));
    }

    public String getScanNumber () {
        String scanNumber;
        String type = "YQ";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        Integer currenCount = chemicalFiberStockWarehousingRepository.getCurrenNoteCount(year+"-"+month);
        String scanNumberMax = chemicalFiberStockWarehousingRepository.getScanNumberMax();
        Integer numberMax = 0;
        if (scanNumberMax != null) {
            String scan[] = scanNumberMax.split("", 7);
            numberMax = Integer.valueOf(scan[6]);
        }
        if (currenCount == 0) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer number = numberMax+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
    }

    public Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberStockWarehousing resources) {
        ChemicalFiberStockWarehousing chemicalFiberStockWarehousing = chemicalFiberStockWarehousingRepository.findById(resources.getId()).orElseGet(ChemicalFiberStockWarehousing::new);
        ValidationUtil.isNull( chemicalFiberStockWarehousing.getId(),"chemicalFiberStockWarehousing","id",resources.getId());
        chemicalFiberStockWarehousingRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void warehousing(List<ChemicalFiberStockWarehousingDetail> resources) {
        ChemicalFiberStockWarehousing chemicalFiberStockWarehousing = chemicalFiberStockWarehousingRepository.findById(resources.get(0).getWarehousingId()).orElseGet(ChemicalFiberStockWarehousing::new);
        ValidationUtil.isNull( chemicalFiberStockWarehousing.getId(),"chemicalFiberStockWarehousing","id",resources.get(0).getWarehousingId());
        chemicalFiberStockWarehousing.setWarehousingStatus(2);
        chemicalFiberStockWarehousingRepository.save(chemicalFiberStockWarehousing);
        for (ChemicalFiberStockWarehousingDetail detail : resources) {
            ChemicalFiberStock chemicalFiberStock = chemicalFiberStockRepository.findByProdId(detail.getProdId(), detail.getUnit());
            //ValidationUtil.isNull( chemicalFiberStock.getId(),"chemicalFiberStock","id",detail.getStockId());
            //String unit = detail.getUnit();
            ChemicalFiberStock Stock = new ChemicalFiberStock();
            if (chemicalFiberStock == null) {
                Stock.setProdId(detail.getProdId());
                Stock.setProdModel(detail.getProdModel());
                Stock.setProdName(detail.getProdName());
                Stock.setTotalNumber(detail.getWarehousingNumber());
                Stock.setProdUnit(detail.getUnit());
                Stock = chemicalFiberStockRepository.save(Stock);
                detail.setStockId(Stock.getId());
                detail.setCreateDate(chemicalFiberStockWarehousing.getCreateDate());
                chemicalFiberStockWarehousingDetailRepository.save(detail);
            } else {
                BigDecimal number = new BigDecimal(0);
                if (chemicalFiberStock.getTotalNumber() != null) {
                    number = chemicalFiberStock.getTotalNumber();
                }
                BigDecimal number1 = detail.getWarehousingNumber();
                number = number.add(number1);
                chemicalFiberStock.setTotalNumber(number);
                Stock = chemicalFiberStockRepository.save(chemicalFiberStock);
                detail.setStockId(Stock.getId());
                detail.setCreateDate(chemicalFiberStockWarehousing.getCreateDate());
                chemicalFiberStockWarehousingDetailRepository.save(detail);
            }

            /*if (unit.equals("吨")) {
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

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberStockWarehousingDetailQueryCriteria criteria = new ChemicalFiberStockWarehousingDetailQueryCriteria();
        criteria.setWarehousingId(id);
        List<ChemicalFiberStockWarehousingDetail> chemicalFiberStockWarehousingDetail =
                chemicalFiberStockWarehousingDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        ChemicalFiberStockWarehousing Warehousing = chemicalFiberStockWarehousingRepository.findById(id).orElseGet(ChemicalFiberStockWarehousing::new);;
        if (chemicalFiberStockWarehousingDetail.size() > 0) {
            for (ChemicalFiberStockWarehousingDetail Detail : chemicalFiberStockWarehousingDetail) {
                if (Warehousing.getWarehousingStatus() == 2) {
                    ChemicalFiberStock chemicalFiberStock = chemicalFiberStockRepository.findById(Detail.getStockId()).orElseGet(ChemicalFiberStock::new);
                    ValidationUtil.isNull( chemicalFiberStock.getId(),"chemicalFiberStock","id",Detail.getStockId());
                    BigDecimal number = new BigDecimal(0);
                    BigDecimal number1 = new BigDecimal(0);
                    if (chemicalFiberStock.getTotalNumber() != null) {
                        number = chemicalFiberStock.getTotalNumber();
                    }
                    if (Detail.getWarehousingNumber() != null) {
                        number1 = Detail.getWarehousingNumber();
                    }
                    number = number.subtract(number1);
                    chemicalFiberStock.setTotalNumber(number);
                    chemicalFiberStockRepository.save(chemicalFiberStock);

                   /* if (unit.equals("吨")) {
                        int ton = 0;
                        int ton1 = 0;
                        if (chemicalFiberStock.getTonNumber() != null) {
                            ton = chemicalFiberStock.getTonNumber();
                        }
                        if (Detail.getWarehousingNumber() != null) {
                            ton1 = Detail.getWarehousingNumber();
                        }
                        ton -= ton1;
                        chemicalFiberStock.setTonNumber(ton);
                        chemicalFiberStockRepository.save(chemicalFiberStock);
                    } else {
                        int branch = 0;
                        int branch1 = 0;
                        if (chemicalFiberStock.getBranchNumber() != null) {
                            branch = chemicalFiberStock.getBranchNumber();
                        }
                        if (Detail.getWarehousingNumber() != null) {
                            branch1 = Detail.getWarehousingNumber();
                        }
                        branch1 = Detail.getWarehousingNumber();
                        branch -= branch1;
                        chemicalFiberStock.setBranchNumber(branch);
                        chemicalFiberStockRepository.save(chemicalFiberStock);
                    }*/
                }
                //chemicalFiberStockWarehousingDetailRepository.deleteById(Detail.getId());
            }
            Warehousing.setInvalid(1);
            chemicalFiberStockWarehousingRepository.save(Warehousing);
        } else {
            Warehousing.setInvalid(1);
            chemicalFiberStockWarehousingRepository.save(Warehousing);
        }
    }

    public Result getWarehousingSummaries(ChemicalFiberStockWarehousingQueryCriteria criteria) {

        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Integer> invalidList = new ArrayList<>();
        invalidList.add(0);
        if (null != criteria.getQueryWithInvalid() && criteria.getQueryWithInvalid())
        {
            invalidList.add(1);
        }
        criteria.setInvalidList(invalidList);
        List<ChemicalFiberStockWarehousing> list  = chemicalFiberStockWarehousingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        List<Map<String, Object>> tonAndBranch = chemicalFiberStockWarehousingDetailRepository.getTonAndBranch();
        Map<Integer, BigDecimal> tonMap = new HashMap<>();
        Map<Integer, BigDecimal> branchMap = new HashMap<>();
        for (Map<String, Object> dto : tonAndBranch) {
            if (null == dto.get("ton")) {
                BigDecimal ton = new BigDecimal(0);
                tonMap.put((Integer)dto.get("warehousing_id"), ton);
            } else {
                BigDecimal ton = new BigDecimal(dto.get("ton").toString());
                tonMap.put((Integer)dto.get("warehousing_id"), ton);
            }
        }
        for (Map<String, Object> dto : tonAndBranch) {
            if (null == dto.get("branch")) {
                BigDecimal branch = new BigDecimal(0);
                branchMap.put((Integer)dto.get("warehousing_id"), branch);
            } else {
                BigDecimal branch = new BigDecimal(dto.get("branch").toString());
                branchMap.put((Integer)dto.get("warehousing_id"), branch);
            }
        }
        List<ChemicalFiberStockWarehousingDTO> warehousingDTO = new ArrayList<>();
        BigDecimal sumTon = new BigDecimal(0.00);
        BigDecimal sumBranch = new BigDecimal(0.00);
        BigDecimal sumTotal = new BigDecimal(0.00);
        for (ChemicalFiberStockWarehousing dto : list) {
            if (tonMap.get(dto.getId()) != null) {
                sumTon = sumTon.add(tonMap.get(dto.getId()));
                sumBranch = sumBranch.add(branchMap.get(dto.getId()));
            }
            if (dto.getTotalPrice() != null) {
                sumTotal = sumTotal.add(dto.getTotalPrice());
            }
        }
        String sumTonAndBranch = sumTon.toString() + "吨/" + sumBranch.toString() + "支";
        List<Object> lists = new ArrayList<>();

        lists.add("合计");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add(sumTon);
        lists.add(sumBranch);
        lists.add(sumTotal);
        return Result.success(lists);
    }
}
