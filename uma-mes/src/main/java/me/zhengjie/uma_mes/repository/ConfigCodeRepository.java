package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.domain.ConfigCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author wave
* @date 2020-10-09
*/
public interface ConfigCodeRepository extends JpaRepository<ConfigCode, Integer>, JpaSpecificationExecutor<ConfigCode> {
    @Query(value = "select c.company_name FROM uma_config_code c limit 1",nativeQuery = true)
    String getCompanyName();

    @Query(value = "select c.serial_code FROM uma_config_code c limit 1",nativeQuery = true)
    String getSerialCode();

    @Query(value = "select c.address FROM uma_config_code c limit 1",nativeQuery = true)
    String getAddress();
}