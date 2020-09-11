package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.WageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
public interface WageUserRepository extends JpaRepository<WageUser, Long>, JpaSpecificationExecutor<WageUser> {

    @Query(value = "select realname as realname,id as id,phone as permission from user where realname like %:realname% ",nativeQuery = true)
    List<WageUser> getWageUser();
}
