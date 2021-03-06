package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.CycleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-09
*/
public interface CycleLabelRepository extends JpaRepository<CycleLabel, Long>, JpaSpecificationExecutor<CycleLabel> {
}