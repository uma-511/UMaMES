package me.zhengjie.uma_mes.task;

import me.zhengjie.uma_mes.service.ChemicalFiberStockService;
import org.springframework.stereotype.Component;

@Component
public class StockTask {
    private final ChemicalFiberStockService chemicalFiberStockService;

    public StockTask(ChemicalFiberStockService chemicalFiberStockService) {
        this.chemicalFiberStockService = chemicalFiberStockService;
    }

    public void run(){
        chemicalFiberStockService.stockTask();
    }
}
