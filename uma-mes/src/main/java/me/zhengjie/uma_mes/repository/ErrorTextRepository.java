package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ErrorText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ErrorTextRepository extends JpaRepository<ErrorText, Integer>, JpaSpecificationExecutor<ErrorText> {
}
