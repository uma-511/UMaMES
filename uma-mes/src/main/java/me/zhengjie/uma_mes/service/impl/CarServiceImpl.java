package me.zhengjie.uma_mes.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import me.zhengjie.uma_mes.domain.Car;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.CarRepository;
import me.zhengjie.uma_mes.service.CarService;
import me.zhengjie.uma_mes.service.dto.CarDTO;
import me.zhengjie.uma_mes.service.dto.CarQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CarMapper;
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

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-08-29
*/
@Service
@CacheConfig(cacheNames = "car")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(CarQueryCriteria criteria, Pageable pageable){
        Page<Car> page = carRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(carMapper::toDto));
    }

    @Override
    @Cacheable
    public List<CarDTO> queryAll(CarQueryCriteria criteria){
        return carMapper.toDto(carRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CarDTO findById(Integer id) {
        Car car = carRepository.findById(id).orElseGet(Car::new);
        ValidationUtil.isNull(car.getId(),"Car","id",id);
        return carMapper.toDto(car);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CarDTO create(Car resources) {
        Integer offSetInt = 0;
        if (null != resources.getTrialCycle()) {
            if (resources.getTrialCycle().equals("三个月")) {
                offSetInt = 3;
            }
            if (resources.getTrialCycle().equals("六个月")) {
                offSetInt = 6;
            }
            if (resources.getTrialCycle().equals("十二个月")) {
                offSetInt = 12;
            }
            Date lastTrialDate = new Date(resources.getLastTrial().getTime());
            Timestamp afterLastTrial = new Timestamp(DateUtil.offsetMonth(lastTrialDate, offSetInt).getTime());
            resources.setExpectDate(afterLastTrial);
        }
        resources.setEnable(Boolean.TRUE);
        return carMapper.toDto(carRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Car resources) {
        Car car = carRepository.findById(resources.getId()).orElseGet(Car::new);
        ValidationUtil.isNull( car.getId(),"Car","id",resources.getId());
        car.copy(resources);
        Integer offSetInt = 0;
        if (null != car.getTrialCycle()){
            if(car.getTrialCycle().equals("三个月")){
                offSetInt = 3;
            }
            if(car.getTrialCycle().equals("六个月")){
                offSetInt = 6;
            }
            if(car.getTrialCycle().equals("十二个月")){
                offSetInt = 12;
            }
            Date lastTrialDate = new Date(car.getLastTrial().getTime());
            Timestamp afterLastTrial = new Timestamp(DateUtil.offsetMonth(lastTrialDate,offSetInt).getTime());
            car.setExpectDate(afterLastTrial);
        }
        carRepository.save(car);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Car car = carRepository.findById(id).orElseGet(Car::new);
        car.setEnable(Boolean.FALSE);
        carRepository.save(car);
    }


    @Override
    public void download(List<CarDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CarDTO car : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("车牌号", car.getCarNumber());
            map.put("车辆类型", car.getCarType());
            map.put("负责人", car.getCarDirector());
            map.put("审理周期", car.getTrialCycle());
            map.put("上次审核日期", car.getLastTrial());
            map.put("预计审核日期", car.getExpectDate());
            map.put("是否可用", car.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}