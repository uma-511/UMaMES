package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.BonusType;
import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author wave
* @date 2020-09-09
*/
public interface BonusTypeRepository extends JpaRepository<BonusType, Long>, JpaSpecificationExecutor<BonusType> {

    @Query(value = "SELECT id,name FROM uma_cycle_label", nativeQuery = true)
    List<CycleLabel> getCycleMenusTree();
}