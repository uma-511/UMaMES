package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ErrorText;
import me.zhengjie.uma_mes.repository.ErrorTextRepository;
import me.zhengjie.uma_mes.service.ErrorTextService;
import me.zhengjie.uma_mes.service.dto.ErrorTextQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ErrorTextMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@CacheConfig(cacheNames = "errorText")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ErrorTextServiceImpl implements ErrorTextService {


    @Autowired
    private ErrorTextRepository errorTextRepository;

    @Autowired
    private ErrorTextMapper errorTextMapper;

    public Map<String, Object> queryAll(ErrorTextQueryCriteria criteria,  Pageable pageable) {
        Page<ErrorText> page = errorTextRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(errorTextMapper::toDto));
    }
}
