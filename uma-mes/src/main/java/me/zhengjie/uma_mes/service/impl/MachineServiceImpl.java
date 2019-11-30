package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Machine;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.MachineRepository;
import me.zhengjie.uma_mes.service.MachineService;
import me.zhengjie.uma_mes.service.dto.MachineDTO;
import me.zhengjie.uma_mes.service.dto.MachineQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.MachineMapper;
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
* @author Tan Jun Ming
* @date 2019-11-24
*/
@Service
@CacheConfig(cacheNames = "machine")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    public MachineServiceImpl(MachineRepository machineRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(MachineQueryCriteria criteria, Pageable pageable){
        Page<Machine> page = machineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(machineMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<MachineDTO> queryAll(MachineQueryCriteria criteria){
        return machineMapper.toDto(machineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public MachineDTO findById(Integer id) {
        Machine machine = machineRepository.findById(id).orElseGet(Machine::new);
        ValidationUtil.isNull(machine.getId(),"Machine","id",id);
        return machineMapper.toDto(machine);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public MachineDTO create(Machine resources) {
        return machineMapper.toDto(machineRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Machine resources) {
        Machine machine = machineRepository.findById(resources.getId()).orElseGet(Machine::new);
        ValidationUtil.isNull( machine.getId(),"Machine","id",resources.getId());
        machine.copy(resources);
        machineRepository.save(machine);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        machineRepository.deleteById(id);
    }


    @Override
    public void download(List<MachineDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MachineDTO machine : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("机台号", machine.getNumber());
            map.put("生产单id", machine.getProductionId());
            map.put("机台状态", machine.getStatus());
            map.put("终端id", machine.getTerminalId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}