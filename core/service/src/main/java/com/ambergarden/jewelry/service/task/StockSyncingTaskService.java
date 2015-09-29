package com.ambergarden.jewelry.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.StockSyncingTaskConverter;
import com.ambergarden.jewelry.orm.repository.task.StockSyncingTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.StockSyncingTask;

@Service
public class StockSyncingTaskService {
   @Autowired
   private StockSyncingTaskRepository stockSyncingTaskRepository;

   @Autowired
   private StockSyncingTaskConverter stockSyncingTaskConverter;

   public StockSyncingTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskRepository.findOne(id);
      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }

   public StockSyncingTask findLast() {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskRepository.findFirstByOrderByStartTimeDesc();
      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }

   public StockSyncingTask save(StockSyncingTask task) {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskConverter.convertTo(task);
      syncingTask = stockSyncingTaskRepository.save(syncingTask);
      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }
}