package com.ambergarden.jewelry.executor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.ScanType;

@Component
public class ScanTaskExecutor extends AbstractStockTaskExecutor {
   public static final double FILTER_SCORE = 3;

   @Override
   protected ScanType getScanType() {
      return ScanType.FULL_DAY;
   }

   @Override
   protected boolean isQualifiedScore(double score) {
      return score > FILTER_SCORE;
   }

   @Override
   protected final List<Stock> getStocksForScan() {
      return stockService.findAll();
   }
}