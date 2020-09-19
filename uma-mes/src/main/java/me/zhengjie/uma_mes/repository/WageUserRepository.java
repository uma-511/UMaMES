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

    @Query(value = "SELECT u.id as id,u.realname as realName,(select j.`name` from job j where j.id=u.job_id) as job,(select d.`name` from dept d where d.id=u.dept_id) as dept,(select ifnull(j.basic_salary,0) from job j where j.id=u.job_id) as basicsalary FROM `user` u where u.enabled = :enable",nativeQuery = true)
    List<WageUser> getWageUser(Boolean enable);
}
