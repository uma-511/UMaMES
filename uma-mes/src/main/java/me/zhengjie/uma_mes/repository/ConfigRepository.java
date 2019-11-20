package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ConfigRepository extends JpaRepository<Config, Integer>, JpaSpecificationExecutor<Config> {
}