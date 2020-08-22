package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardQueryCriteria;

import java.util.List;

public interface ChemicalFibeDashboardService {

    ChemicalFibeDashboardDTO queryAll(ChemicalFibeDashboardQueryCriteria criteria);
}
