package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByRealname(String realname);

    User findByEmail(String email);

    @Modifying
    @Query(value = "update user set password = ?2 , last_password_reset_time = ?3 where username = ?1",nativeQuery = true)
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    @Modifying
    @Query(value = "update user set email = ?2 where username = ?1",nativeQuery = true)
    void updateEmail(String username, String email);

    @Query(value = "select * from user where dept_id = :id and realname like %:realname% ",nativeQuery = true)
    List<User> getUserList(Long id, String realname);

    @Query(value = "select * from user where dept_id in (:ids) and realname like %:realname% ",nativeQuery = true)
    List<User> getUserListByDept(List<Long> ids, String realname);

    @Query(value = "SELECT r.permission FROM role r WHERE r.id = (select ur.role_id  from users_roles ur where ur.user_id = :id) limit 1",nativeQuery = true)
    String getPermissionByUserId(Long id);
}
