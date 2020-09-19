package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-08-29
*/
public interface CarRepository extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {
}