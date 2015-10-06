package com.ambergarden.jewelry.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.MarketTradingInfoHolder;
import com.ambergarden.jewelry.executor.analysis.StockAnalyser;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.TagConverter;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockCategory;
import com.ambergarden.jewelry.schema.beans.task.ScanResult;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.ScanTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class ScanTaskExecutor implements Runnable {

   @Autowired
   private ScanTaskService scanTaskService;

   @Autowired
   private StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   private StockService stockService;

   @Autowired
   private StockAnalyser stockAnalyser;

   @Autowired
   private TagConverter tagConverter;

   private State state;

   public synchronized boolean changeToWorkingState() {
      if (state == State.WORKING) {
         return false;
      } else {
         state = State.WORKING;
         return true;
      }
   }

   @Override
   public void run() {
      if (!changeToWorkingState()) {
         return;
      }

      ScanTask scanTask = scanTaskService.findLast();
      if (scanTask == null) {
         return;
      }

      try {
         MarketTradingInfoHolder marketInfoHolder = prepareScanInfoHolder();
         for (Stock pendingStock : marketInfoHolder.getPendingStocks()) {
            // TODO: During each analysis, use a try catch to add a log, and then
            // we can continue with other stocks
            List<Tag> tags = null;
            if (pendingStock.getStockCategory() == StockCategory.SHANGHAI) {
               tags = stockAnalyser.analysis(pendingStock, marketInfoHolder.getTradingInfoSH());
            } else {
               tags = stockAnalyser.analysis(pendingStock, marketInfoHolder.getTradingInfoSZ());
            }

            if (tags.size() != 0) {
               ScanResult result = new ScanResult();
               result.setStock(pendingStock);
               result.setTags(tagConverter.convertFrom(tags));
               scanTask.getResults().add(result);
            }
         }

         scanTask.setTaskState(TaskState.SUCCESS);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);
      } catch (RuntimeException ex) {
         scanTask = scanTaskService.findLast();
         scanTask.setTaskState(TaskState.FAILED);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }

   private MarketTradingInfoHolder prepareScanInfoHolder() {
      List<Stock> pendingStocks = stockService.findAll();
      List<TradingInfo> tradingInfoSH = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
      List<TradingInfo> tradingInfoSZ = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SZ);
      return new MarketTradingInfoHolder(tradingInfoSH, tradingInfoSZ, pendingStocks);
   }
}