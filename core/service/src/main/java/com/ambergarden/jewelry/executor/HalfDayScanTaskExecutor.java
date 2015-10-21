package com.ambergarden.jewelry.executor;

import java.util.List;

import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.ScanType;

public class HalfDayScanTaskExecutor extends AbstractStockTaskExecutor {
   public static final double FILTER_SCORE = 3;

   @Override
   protected ScanType getScanType() {
      return ScanType.HALF_DAY;
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