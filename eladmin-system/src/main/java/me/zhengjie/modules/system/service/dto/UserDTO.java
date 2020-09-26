package me.zhengjie.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@Setter
public class UserDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String realname;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    private Boolean isWorker;

    private String permission;

    @JsonIgnore
    private String password;

    private Date lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDTO> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDTO job;

    private DeptSmallDTO dept;

    private Long deptId;

    private Timestamp createTime;
}
