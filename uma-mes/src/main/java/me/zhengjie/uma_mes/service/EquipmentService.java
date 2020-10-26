package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Equipment;
import me.zhengjie.uma_mes.service.dto.EquipmentDTO;
import me.zhengjie.uma_mes.service.dto.EquipmentQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface EquipmentService {

    Map<String,Object> queryAll(EquipmentQueryCriteria criteria, Pageable pageable);

    EquipmentDTO create(Equipment resources);

    Equipment getByIp(String resources);

    void update(Equipment resources);
}
