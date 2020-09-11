package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Entity
@Getter
@Setter
@Table(name="job")
public class BonusJob implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    @Column(name = "name",nullable = false)
    @NotBlank
    private String name;

    @Column(unique = true)
    @NotNull
    private Long sort;

    @Column(name = "basic_salary")
    @NotNull
    private BigDecimal basicSalary;

    @Column(name = "enabled",nullable = false)
    @NotNull
    private Boolean enabled;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;



    @ManyToMany(mappedBy = "bonusJobs")
    @JsonIgnore
    private Set<BonusType> bonusTypes;

    public @interface Update {}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BonusJob bonusJob = (BonusJob) o;
        return Objects.equals(id, bonusJob.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}