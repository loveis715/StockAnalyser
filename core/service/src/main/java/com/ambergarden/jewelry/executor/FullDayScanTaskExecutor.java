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
import com.ambergarden.jewelry.schema.beans.task.FullDayScanResult;
import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.FullDayScanTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class FullDayScanTaskExecutor implements Runnable {

   @Autowired
   private FullDayScanTaskService fullDayScanTaskService;

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

      FullDayScanTask fullDayScanTask = fullDayScanTaskService.findLast();
      if (fullDayScanTask == null) {
         return;
      }

      try {
         MarketTradingInfoHolder marketInfoHolder = prepareFullDayScanInfoHolder();
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
               FullDayScanResult result = new FullDayScanResult();
               result.setStock(pendingStock);
               result.setTags(tagConverter.convertFrom(tags));
               fullDayScanTask.getResults().add(result);
            }
         }

         fullDayScanTask.setTaskState(TaskState.SUCCESS);
         fullDayScanTask.setEndTime(new Date());
         fullDayScanTaskService.update(fullDayScanTask);
      } catch (RuntimeException ex) {
         fullDayScanTask = fullDayScanTaskService.findLast();
         fullDayScanTask.setTaskState(TaskState.FAILED);
         fullDayScanTask.setEndTime(new Date());
         fullDayScanTaskService.update(fullDayScanTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }

   private MarketTradingInfoHolder prepareFullDayScanInfoHolder() {
      List<Stock> pendingStocks = stockService.findAll();
      List<TradingInfo> tradingInfoSH = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
      List<TradingInfo> tradingInfoSZ = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SZ);
      return new MarketTradingInfoHolder(tradingInfoSH, tradingInfoSZ, pendingStocks);
   }
}