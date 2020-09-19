package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Receipt;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ReceiptRepository;
import me.zhengjie.uma_mes.service.ReceiptService;
import me.zhengjie.uma_mes.service.dto.ReceiptDTO;
import me.zhengjie.uma_mes.service.dto.ReceiptQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ReceiptMapper;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Service
@CacheConfig(cacheNames = "receipt")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    private final CustomerService customerService;

    private final CustomerMapper customerMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    @Value("${globalCompanyName}")
    private String globalCompanyName;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper, CustomerService customerService, CustomerMapper customerMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    public Map<String,Object> queryAll(ReceiptQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<Receipt> page = receiptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(receiptMapper::toDto));
    }

    @Override
    public List<ReceiptDTO> queryAll(ReceiptQueryCriteria criteria){
        return receiptMapper.toDto(receiptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ReceiptDTO findById(Integer id) {
        Receipt receipt = receiptRepository.findById(id).orElseGet(Receipt::new);
        ValidationUtil.isNull(receipt.getId(),"Receipt","id",id);
        return receiptMapper.toDto(receipt);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ReceiptDTO create(Receipt resources) {
        resources.setReceiptNumber(getScanNumberWithMaxNumber());
        resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setStatus(2);
        resources.setEnable(Boolean.TRUE);
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        BigDecimal currentAccount = null == customerDTO.getAccount()? new BigDecimal(0):customerDTO.getAccount();
        customerDTO.setAccount(currentAccount.add(resources.getAmountOfMoney()));
        customerService.save(customerMapper.toEntity(customerDTO));
        return receiptMapper.toDto(receiptRepository.save(resources));

    }

    public String getScanNumber () {
        String scanNumber;
        String type = "SK";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        Integer currenCount=receiptRepository.getCurrenReceiptCount(year.substring(2,4)+"-"+month);

        if (currenCount == 0) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer number = currenCount+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
    }

    public String getScanNumberWithMaxNumber () {
        String scanNumber;
        String type = "SK";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        String currenNumber=receiptRepository.getCurrenReceiptCountWithMaxNumber(year.substring(2,4)+"-"+month);

        if (null == currenNumber || currenNumber.equals("")) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer lastNumber = Integer.parseInt(currenNumber.substring(currenNumber.length()-5,currenNumber.length()));
            Integer number = lastNumber+ 1;
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

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Receipt resources) {
        Receipt receipt = receiptRepository.findById(resources.getId()).orElseGet(Receipt::new);
        ValidationUtil.isNull( receipt.getId(),"Receipt","id",resources.getId());
        receipt.copy(resources);
        receiptRepository.save(receipt);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Receipt receipt = receiptRepository.findById(id).orElseGet(Receipt::new);
        receipt.setEnable(Boolean.FALSE);
        receiptRepository.save(receipt);
    }

    @Override
    public void download(List<ReceiptDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReceiptDTO receipt : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("流水号", receipt.getReceiptNumber());
            map.put("客户名称", receipt.getCustomerName());
            map.put("收入类型", receipt.getType());
            map.put("收款账号", receipt.getRecivedAccount());
            map.put("单据日期", receipt.getRecivedDate());
            map.put("项目类型", receipt.getProjectType());
            map.put("经办人", receipt.getOperator());
            map.put("金额", receipt.getAmountOfMoney());
            map.put("单据编号", receipt.getRecivedNumber());
            map.put("备注", receipt.getRemark());
            map.put("创单人", receipt.getCreateUser());
            map.put("创单日期", receipt.getCreateDate());
            map.put("状态：0-失效 1-编辑 2-完结", receipt.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void doFinish(Integer id) {
        Receipt receipt = receiptRepository.findById(id).orElseGet(Receipt::new);
        CustomerDTO customerDTO = customerService.findById(receipt.getCustomerId());
        customerDTO.setAccount(customerDTO.getAccount().add(receipt.getAmountOfMoney()));
        customerService.save(customerMapper.toEntity(customerDTO));
        receipt.setStatus(2);
        receiptRepository.save(receipt);
    }
}