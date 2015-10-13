package com.ambergarden.jewelry.executor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.ScanType;

@Component
public class StockAnalysisExecutor extends AbstractStockTaskExecutor {
   private List<Integer> targetStocks;

   public List<Integer> getTargetStocks() {
      return targetStocks;
   }

   public void setTargetStocks(List<Integer> targetStocks) {
      this.targetStocks = targetStocks;
   }

   @Override
   protected ScanType getScanType() {
      return ScanType.SINGLE_STOCK;
   }

   @Override
   protected boolean isQualifiedScore(double score) {
      return true;
   }

   @Override
   protected final List<Stock> getStocksForScan() {
      List<Stock> pendingStocks = new ArrayList<Stock>();
      for (Integer stockId : targetStocks) {
         Stock stock = stockService.findById(stockId);
         if (stock != null) {
            pendingStocks.add(stock);
         }
      }
      return pendingStocks;
   }
}