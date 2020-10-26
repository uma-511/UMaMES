package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface EquipmentRepository  extends JpaRepository<Equipment, Integer>, JpaSpecificationExecutor<Equipment> {

    @Query(value = "SELECT * FROM uma_equipment where ip = ?1",nativeQuery = true)
    Equipment getByIp(String ip);
}
