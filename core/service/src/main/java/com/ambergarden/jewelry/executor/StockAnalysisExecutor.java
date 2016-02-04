package com.ambergarden.jewelry.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.MarketTradingData;
import com.ambergarden.jewelry.executor.analysis.ModelAnalyser;
import com.ambergarden.jewelry.executor.analysis.MorphologyAnalyser;
import com.ambergarden.jewelry.executor.analysis.StockAnalyser;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.TagConverter;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockCategory;
import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTask;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.StockAnalyseTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class StockAnalysisExecutor implements Runnable {
   @Autowired
   protected StockAnalyseTaskService stockAnalyseTaskService;

   @Autowired
   protected StockService stockService;

   @Autowired
   protected StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   protected TagConverter tagConverter;

   @Autowired
   protected StockAnalyser stockAnalyser;

   @Autowired
   protected MorphologyAnalyser morphologyAnalyser;

   @Autowired
   protected ModelAnalyser modelAnalyser;

   private State state;

   private String stockCode;

   public String getStockCode() {
      return stockCode;
   }

   public void setStockCode(String stockCode) {
      this.stockCode = stockCode;
   }

   protected synchronized boolean changeToWorkingState() {
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

      StockAnalyseTask stockAnalyseTask = stockAnalyseTaskService.findLast();
      if (stockAnalyseTask == null) {
         return;
      }

      try {
         MarketTradingData tradingData = null;
         Stock stock = stockService.getByAlias(stockCode);
         if (stock.getStockCategory() == StockCategory.SHANGHAI) {
            List<TradingInfo> tradingInfoSH = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
            List<MinuteData> minuteDataSH = tradingInfoProvider.getPerMinuteTradingInfo(Constants.CODE_SH);
            tradingData = new MarketTradingData(tradingInfoSH, minuteDataSH);
         } else {
            List<TradingInfo> tradingInfoSZ = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SZ);
            List<MinuteData> minuteDataSZ = tradingInfoProvider.getPerMinuteTradingInfo(Constants.CODE_SZ);
            tradingData = new MarketTradingData(tradingInfoSZ, minuteDataSZ);
         }

         List<TradingInfo> tradingInfoList = tradingInfoProvider.getDailyTraidingInfoFor300Days(stock.getCode());
         List<MinuteData> minuteDataList = tradingInfoProvider.getPerMinuteTradingInfo(stock.getCode());
         List<Tag> tags = stockAnalyser.analysis(stock, tradingData);
         tags.addAll(morphologyAnalyser.analyse(stock, tradingInfoList));
         tags.addAll(modelAnalyser.analyseHalfDay(stock, tradingInfoList.subList(0, tradingInfoList.size() - 1), minuteDataList));
         stockAnalyseTask.setResultTags(tagConverter.convertFrom(tags));
         stockAnalyseTask.setTaskState(TaskState.SUCCESS);
         stockAnalyseTask.setEndTime(new Date());
         stockAnalyseTaskService.update(stockAnalyseTask);
      } catch (RuntimeException ex) {
         stockAnalyseTask = stockAnalyseTaskService.findLast();
         stockAnalyseTask.setTaskState(TaskState.FAILED);
         stockAnalyseTask.setEndTime(new Date());
         stockAnalyseTaskService.update(stockAnalyseTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }
}