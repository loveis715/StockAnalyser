package com.ambergarden.jewelry.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.MarketTradingInfoHolder;
import com.ambergarden.jewelry.executor.analysis.StockAnalyser;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
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

      MarketTradingInfoHolder marketInfoHolder = prepareFullDayScanInfoHolder();
      for (Stock pendingStock : marketInfoHolder.getPendingStocks()) {
         stockAnalyser.analysis(pendingStock);
      }

      fullDayScanTask.setTaskState(TaskState.SUCCESS);
      fullDayScanTask.setEndTime(new Date());
      fullDayScanTaskService.update(fullDayScanTask);
   }

   private MarketTradingInfoHolder prepareFullDayScanInfoHolder() {
      List<Stock> pendingStocks = stockService.findAll();
      List<TradingInfo> tradingInfoSH = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
      List<TradingInfo> tradingInfoSZ = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SZ);
      return new MarketTradingInfoHolder(tradingInfoSH, tradingInfoSZ, pendingStocks);
   }
}