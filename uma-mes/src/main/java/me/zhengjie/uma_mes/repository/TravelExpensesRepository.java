package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.TravelExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-02
*/
public interface TravelExpensesRepository extends JpaRepository<TravelExpenses, Integer>, JpaSpecificationExecutor<TravelExpenses> {
}