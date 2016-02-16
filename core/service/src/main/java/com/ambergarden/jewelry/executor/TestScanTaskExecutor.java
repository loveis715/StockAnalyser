package com.ambergarden.jewelry.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.Constants;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor.State;
import com.ambergarden.jewelry.executor.analysis.ModelAnalyser;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.TagConverter;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.schema.beans.task.TestScanResult;
import com.ambergarden.jewelry.schema.beans.task.TestScanTask;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.TestScanTaskService;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class TestScanTaskExecutor implements Runnable {
   private State state;

   @Autowired
   protected TestScanTaskService testScanTaskService;

   @Autowired
   protected StockService stockService;

   @Autowired
   protected TagConverter tagConverter;

   @Autowired
   protected StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   protected ModelAnalyser modelAnalyser;

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
      TestScanTask testScanTask = testScanTaskService.findLast();
      if (testScanTask == null) {
         return;
      }

      if (!changeToWorkingState()) {
         return;
      }

      try {
         List<Stock> stocks = stockService.findAll();
         for (Stock pendingStock : stocks) {
            if (pendingStock.getCode().startsWith(Constants.CODE_SZ300)) {
               continue;
            }
            if (pendingStock.getTotalVolume() > 1000000000L) {
               continue;
            }

            List<TradingInfo> tradingInfoList = tradingInfoProvider.getDailyTraidingInfoFor300Days(pendingStock.getCode());
            for (int endIndex = tradingInfoList.size() - 4; endIndex > 31; endIndex--) {
               // For each day, find the highest price.
               double highest = 0;
               int counter = 0;
               while (counter < 3) {
                  counter++;
                  TradingInfo tradingInfo = tradingInfoList.get(endIndex + counter);
                  if (tradingInfo.getHigh() > highest) {
                     highest = tradingInfo.getHigh();
                  }
               }

               List<TradingInfo> tradingInfoListForTest = tradingInfoList.subList(0, endIndex);
               TradingInfo lastTradingForTest = tradingInfoListForTest.get(tradingInfoListForTest.size() - 1);
               String dayString = lastTradingForTest.getDay();
               if (dayString.compareTo("2016-01-03") > 0 && dayString.compareTo("2016-01-28") < 0
                     || dayString.compareTo("2015-08-17") > 0 && dayString.compareTo("2015-08-25") < 0
                     || dayString.compareTo("2015-06-15") > 0 && dayString.compareTo("2015-07-09") < 0) {
                  continue;
               }
               List<Tag> tags = modelAnalyser.analyseFullDay(pendingStock, tradingInfoListForTest);
               for (Tag tag : tags) {
                  TestScanResult result = new TestScanResult();
                  result.setId(-1);
                  result.setStock(pendingStock);
                  result.setTag(tag.getTagName());
                  result.setDay(lastTradingForTest.getDay());
                  result.setPriceChange(highest/lastTradingForTest.getClose());
                  testScanTask.getResults().add(result);
               }
            }

            // Temporary short circuit for debugging
            if (testScanTask.getResults().size() > 200) {
               break;
            }
         }

         testScanTask.setTaskState(TaskState.SUCCESS);
         testScanTask.setEndTime(new Date());
         testScanTaskService.update(testScanTask);
      }  catch (RuntimeException ex) {
         testScanTask = testScanTaskService.findLast();
         testScanTask.setTaskState(TaskState.FAILED);
         testScanTask.setEndTime(new Date());
         testScanTaskService.update(testScanTask);

         throw ex;
      } finally {
         state = State.IDLE;
      }
   }
}