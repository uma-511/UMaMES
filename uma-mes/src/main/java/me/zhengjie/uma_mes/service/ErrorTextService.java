package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.service.dto.ErrorTextQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ErrorTextService {


    Map<String,Object> queryAll(ErrorTextQueryCriteria criteria, Pageable pageable);
}
