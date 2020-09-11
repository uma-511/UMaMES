package me.zhengjie.uma_mes.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Entity
@Getter
@Setter
public class WageUser implements Serializable {

    @Id
    private Long id;

    @Column(name = "realname")
    private String realname;

    @Column(name = "permission")
    private String permission;
}