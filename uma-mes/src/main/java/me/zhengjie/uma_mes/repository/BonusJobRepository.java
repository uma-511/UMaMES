package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.BonusJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
public interface BonusJobRepository extends JpaRepository<BonusJob, Long>, JpaSpecificationExecutor<BonusJob> {
}