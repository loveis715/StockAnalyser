package com.ambergarden.jewelry.executor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.MarketTradingData;
import com.ambergarden.jewelry.executor.analysis.MarketTradingInfoHolder;
import com.ambergarden.jewelry.executor.analysis.StockAnalyser;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.TagCategory;
import com.ambergarden.jewelry.executor.tag.TagConverter;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockCategory;
import com.ambergarden.jewelry.schema.beans.task.ScanResult;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.schema.beans.task.ScanType;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.ScanTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class ScanTaskExecutor implements Runnable {
   public static final double FILTER_SCORE = 3;

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

      ScanTask scanTask = scanTaskService.findLast(ScanType.FULL_DAY);
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
               tags = stockAnalyser.analysis(pendingStock, marketInfoHolder.getTradingSH());
            } else {
               tags = stockAnalyser.analysis(pendingStock, marketInfoHolder.getTradingSZ());
            }

            if (tags.size() != 0) {
               double score = calculateStockScore(tags);
               if (score > FILTER_SCORE) {
                  ScanResult result = new ScanResult();
                  result.setId(-1);
                  result.setStock(pendingStock);
                  result.setTags(tagConverter.convertFrom(tags));
                  result.setScore(score);
                  scanTask.getResults().add(result);
               }
            }
         }

         List<ScanResult> results = scanTask.getResults();
         Collections.sort(results, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult result1, ScanResult result2) {
               return result1.getScore() > result2.getScore() ? -1 : 1;
            }
         });
         scanTask.setTaskState(TaskState.SUCCESS);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);
      } catch (RuntimeException ex) {
         scanTask = scanTaskService.findLast(ScanType.FULL_DAY);
         scanTask.setTaskState(TaskState.FAILED);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }

   private MarketTradingInfoHolder prepareScanInfoHolder() {
      List<TradingInfo> tradingInfoSH = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
      List<MinuteData> minuteDataSH = tradingInfoProvider.getPerMinuteTradingInfo(Constants.CODE_SH);
      MarketTradingData tradingDataSH = new MarketTradingData(tradingInfoSH, minuteDataSH);

      List<TradingInfo> tradingInfoSZ = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SZ);
      List<MinuteData> minuteDataSZ = tradingInfoProvider.getPerMinuteTradingInfo(Constants.CODE_SZ);
      MarketTradingData tradingDataSZ = new MarketTradingData(tradingInfoSZ, minuteDataSZ);

      List<Stock> pendingStocks = stockService.findAll();
      Collections.sort(pendingStocks, new Comparator<Stock>() {
         @Override
         public int compare(Stock stock1, Stock stock2) {
            return stock1.getCode().compareTo(stock2.getCode());
         }
      });
      return new MarketTradingInfoHolder(tradingDataSH, tradingDataSZ, pendingStocks);
   }

   private double calculateStockScore(List<Tag> tags) {
      double score = 0;
      for (Tag tag : tags) {
         if (tag.getTagCategory() == TagCategory.POSITIVE) {
            score += tag.getValue();
         } else if (tag.getTagCategory() == TagCategory.NEGATIVE) {
            score -= tag.getValue();
         }
      }
      return score;
   }
}