package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.BonusCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-10
*/
public interface BonusCycleRepository extends JpaRepository<BonusCycle, Integer>, JpaSpecificationExecutor<BonusCycle> {
}