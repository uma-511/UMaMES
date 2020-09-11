package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.BonusJob;
import me.zhengjie.uma_mes.service.dto.BonusJobQueryCriteria;

import java.util.List;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
public interface BonusJobService {

    List<BonusJob> queryAll(BonusJobQueryCriteria criteria);
}