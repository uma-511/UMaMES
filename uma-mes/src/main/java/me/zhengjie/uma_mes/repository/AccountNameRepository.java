package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.AccountName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface AccountNameRepository extends JpaRepository<AccountName, Integer>, JpaSpecificationExecutor<AccountName> {
}