package com.ambergarden.jewelry.executor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.ModelAnalyser;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.TagCategory;
import com.ambergarden.jewelry.executor.tag.TagConverter;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.ScanResult;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.schema.beans.task.ScanType;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.ScanTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class HalfDayScanTaskExecutor implements Runnable {
   @Autowired
   protected ScanTaskService scanTaskService;

   @Autowired
   protected StockService stockService;

   @Autowired
   protected StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   protected TagConverter tagConverter;

   @Autowired
   protected ModelAnalyser modelAnalyser;

   private State state;

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
      ScanTask scanTask = scanTaskService.findLast(ScanType.HALF_DAY);
      if (scanTask == null) {
         return;
      }

      if (!changeToWorkingState()) {
         return;
      }

      try {
         List<TradingInfo> marketTradings = tradingInfoProvider.getDailyTraidingInfo(Constants.CODE_SH);
         TradingInfo lastMarketTrading = marketTradings.get(marketTradings.size() - 1);

         List<Stock> stocks = stockService.findAll();
         for (Stock pendingStock : stocks) {
            if (pendingStock.getCode().startsWith(Constants.CODE_SZ300)) {
               continue;
            }
            if (pendingStock.getTotalVolume() > 1000000000L) {
               continue;
            }

            List<TradingInfo> tradingInfoList = tradingInfoProvider.getDailyTraidingInfo(pendingStock.getCode());
            TradingInfo lastStockTrading = tradingInfoList.get(tradingInfoList.size() - 1);
            if (lastMarketTrading.getDay().compareTo(lastStockTrading.getDay()) != 0) {
               continue;
            }

            List<MinuteData> minuteDataList = tradingInfoProvider.getPerMinuteTradingInfo(pendingStock.getCode());
            List<Tag> tags = modelAnalyser.analyseHalfDay(pendingStock, tradingInfoList.subList(0, tradingInfoList.size() - 1), minuteDataList);
            double score = calculateStockScore(tags);
            if (score >= 1) {
               ScanResult result = new ScanResult();
               result.setId(-1);
               result.setStock(pendingStock);
               result.setTags(tagConverter.convertFrom(tags));
               result.setScore(score);
               scanTask.getResults().add(result);
            }
         }

         List<ScanResult> results = scanTask.getResults();
         Collections.sort(results, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult result1, ScanResult result2) {
               int codeCompareResult = result1.getStock().getCode().compareTo(result2.getStock().getCode());
               return result1.getScore() > result2.getScore() ? -1 : result1.getScore() == result2.getScore() ? codeCompareResult : 1;
            }
         });
         scanTask.setTaskState(TaskState.SUCCESS);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);
      } catch (RuntimeException ex) {
         scanTask = scanTaskService.findLast(ScanType.HALF_DAY);
         scanTask.setTaskState(TaskState.FAILED);
         scanTask.setEndTime(new Date());
         scanTaskService.update(scanTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }

   protected double calculateStockScore(List<Tag> tags) {
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