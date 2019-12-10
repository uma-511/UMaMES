package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.HeartBeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2019-12-04
*/
public interface HeartBeatRepository extends JpaRepository<HeartBeat, Integer>, JpaSpecificationExecutor<HeartBeat> {
}