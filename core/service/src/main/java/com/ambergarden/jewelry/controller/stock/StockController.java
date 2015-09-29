package com.ambergarden.jewelry.controller.stock;

import static com.ambergarden.jewelry.Constants.STATISTICS;
import static com.ambergarden.jewelry.Constants.STOCK_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ambergarden.jewelry.schema.beans.stock.StockStatistics;
import com.ambergarden.jewelry.service.stock.StockService;

@Controller
@RequestMapping(value = STOCK_URL)
public class StockController {
   @Autowired
   private StockService stockService;

   @RequestMapping(value = STATISTICS, method = RequestMethod.GET)
   public StockStatistics getStatistics() {
      return stockService.getStatistics();
   }
}