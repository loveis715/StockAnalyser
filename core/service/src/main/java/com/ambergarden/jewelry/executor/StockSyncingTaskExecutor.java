package com.ambergarden.jewelry.executor;

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

      // 1. Construct map for all existing stocks
      List<Stock> stocks = stockService.findAll();
      Map<String, Stock> stockMap = new HashMap<String, Stock>();
      for (Stock stock : stocks) {
         String code = stock.getCode();
         stockMap.put(code, stock);
      }

      // 2. Retrieve all stocks for Shanghai and update stock information if required
      List<com.ambergarden.jewelry.schema.beans.provider.stock.Stock> providerStocks
         = stockListProvider.listAllStocks(StockCategory.SHANGHAI);
      for (com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock : providerStocks) {
         Stock stock = stockMap.get(providerStock.getName());
         if (needUpdate(stock, providerStock)) {
            fillStock(stock, providerStock);
         }
      }

      // 3. Set sub task "Shanghai stock listing" to success and mark total
      // progress to 50%
      syncingTask.setPercentage(0.5);
      StockListingTask listingTask = syncingTask.getListingTaskForShanghai();
      listingTask.setTaskState(TaskState.SUCCESS);
      syncingTaskService.save(syncingTask);

      // 4. Retrieve all stocks for Shenzhen and update stock information if required
      providerStocks = stockListProvider.listAllStocks(StockCategory.SHENZHEN);
      for (com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock : providerStocks) {
         Stock stock = stockMap.get(providerStock.getName());
         if (needUpdate(stock, providerStock)) {
            fillStock(stock, providerStock);
         }
      }

      // 5. Set sub task "Shenzhen stock listing" to success and mark the syncing
      // task as complete
      syncingTask.setPercentage(1);
      syncingTask.setTaskState(TaskState.SUCCESS);
      listingTask = syncingTask.getListingTaskForShenzhen();
      listingTask.setTaskState(TaskState.SUCCESS);
      syncingTaskService.save(syncingTask);
   }

   private boolean needUpdate(Stock stock, com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock) {
      return !stock.getName().equals(providerStock.getName());
   }

   private void fillStock(Stock stock, com.ambergarden.jewelry.schema.beans.provider.stock.Stock providerStock) {
      stock.setName(providerStock.getName());
   }
}