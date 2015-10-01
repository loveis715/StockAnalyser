package com.ambergarden.jewelry.executor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.provider.stock.StockCategory;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.StockListingTask;
import com.ambergarden.jewelry.schema.beans.task.StockSyncingTask;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.stock.StockService;
import com.ambergarden.jewelry.service.task.StockSyncingTaskService;
import com.ambergarden.jewelry.sina.provider.StockListProvider;

@Component
public class StockSyncingTaskExecutor implements Runnable {
   public enum State {
      IDLE,
      WORKING
   }

   @Autowired
   private StockSyncingTaskService syncingTaskService;

   @Autowired
   private StockListProvider stockListProvider;

   @Autowired
   private StockService stockService;

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

      StockSyncingTask syncingTask = syncingTaskService.findLast();
      if (syncingTask == null) {
         return;
      }

      try {
         syncingTask.setTaskState(TaskState.IN_PROGRESS);
         syncingTask = syncingTaskService.update(syncingTask);

         Map<String, Stock> stockMap = getAllExistingStocks();
         syncingTask = refreshStocksForShanghai(syncingTask, stockMap);
         syncingTask = refreshStocksForShenzhen(syncingTask, stockMap);
      } catch (RuntimeException ex) {
         syncingTask = syncingTaskService.findLast();
         syncingTask.setTaskState(TaskState.FAILED);
         syncingTask.setEndTime(new Date());
         syncingTaskService.update(syncingTask);
         throw ex;
      } finally {
         state = State.IDLE;
      }
   }

   private Map<String, Stock> getAllExistingStocks() {
      List<Stock> stocks = stockService.findAll();
      Map<String, Stock> stockMap = new HashMap<String, Stock>();
      for (Stock stock : stocks) {
         String code = stock.getName();
         stockMap.put(code, stock);
      }
      return stockMap;
   }

   private StockSyncingTask refreshStocksForShanghai(StockSyncingTask syncingTask, Map<String, Stock> stockMap) {
      StockListingTask listingTask = syncingTask.getListingTaskForShanghai();
      listingTask.setStartTime(new Date());

      List<com.ambergarden.jewelry.schema.beans.provider.stock.Stock> providerStocks
         = stockListProvider.listAllStocks(StockCategory.SHANGHAI);
      for (com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock : providerStocks) {
         Stock stock = stockMap.get(providerStock.getName());
         if (stock == null) {
            createStock(providerStock, com.ambergarden.jewelry.schema.beans.stock.StockCategory.SHANGHAI);
         } else if (stock != null && needUpdate(stock, providerStock)) {
            updateStock(stock, providerStock);
         }
      }

      syncingTask.setPercentage(0.5);
      listingTask.setPercentage(1);
      listingTask.setEndTime(new Date());
      listingTask.setTaskState(TaskState.SUCCESS);
      return syncingTaskService.update(syncingTask);
   }

   private StockSyncingTask refreshStocksForShenzhen(StockSyncingTask syncingTask, Map<String, Stock> stockMap) {
      StockListingTask listingTask = syncingTask.getListingTaskForShenzhen();
      listingTask.setStartTime(new Date());

      List<com.ambergarden.jewelry.schema.beans.provider.stock.Stock> providerStocks
         = stockListProvider.listAllStocks(StockCategory.SHENZHEN);
      for (com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock : providerStocks) {
         Stock stock = stockMap.get(providerStock.getName());
         if (stock == null) {
            createStock(providerStock, com.ambergarden.jewelry.schema.beans.stock.StockCategory.SHENZHEN);
         } else if (stock != null && needUpdate(stock, providerStock)) {
            updateStock(stock, providerStock);
         }
      }

      listingTask.setPercentage(1);
      listingTask.setTaskState(TaskState.SUCCESS);
      listingTask.setEndTime(new Date());
      syncingTask.setPercentage(1);
      syncingTask.setTaskState(TaskState.SUCCESS);
      syncingTask.setEndTime(new Date());
      return syncingTaskService.update(syncingTask);
   }

   private boolean needUpdate(Stock stock, com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock) {
      return !stock.getName().equals(providerStock.getName());
   }

   private void createStock(com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock,
         com.ambergarden.jewelry.schema.beans.stock.StockCategory category) {
      com.ambergarden.jewelry.schema.beans.stock.Stock stockBean
         = new com.ambergarden.jewelry.schema.beans.stock.Stock();
      stockBean.setId(-1);
      stockBean.setCode(providerStock.getCode());
      stockBean.setName(providerStock.getName());
      stockBean.setStockCategory(category);
      stockService.create(stockBean);
   }

   private void updateStock(Stock stock, com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock) {
      stock.setName(providerStock.getName());
      stockService.update(stock.getId(), stock);
   }
}