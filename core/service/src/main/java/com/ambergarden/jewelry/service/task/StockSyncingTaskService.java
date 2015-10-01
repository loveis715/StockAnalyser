package com.ambergarden.jewelry.service.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.StockSyncingTaskConverter;
import com.ambergarden.jewelry.executor.StockSyncingTaskExecutor;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.StockSyncingTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.StockSyncingTask;

@Service
public class StockSyncingTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private StockSyncingTaskExecutor stockSyncingTaskExecutor;

   @Autowired
   private StockSyncingTaskRepository stockSyncingTaskRepository;

   @Autowired
   private StockSyncingTaskConverter stockSyncingTaskConverter;

   public StockSyncingTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskRepository.findOne(id);
      // TODO: Check with NULL
      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }

   public StockSyncingTask findLast() {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskRepository.findFirstByOrderByStartTimeDesc();
      if (syncingTask != null) {
         return stockSyncingTaskConverter.convertFrom(syncingTask);
      } else {
         return null;
      }
   }

   public StockSyncingTask create() {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = new com.ambergarden.jewelry.orm.entity.task.StockSyncingTask();
      syncingTask.setStartTime(new Date());
      syncingTask.setPercentage(0);
      syncingTask.setTaskState(TaskState.SCHEDULED);

      com.ambergarden.jewelry.orm.entity.task.StockListingTask listingTaskSH
         = new com.ambergarden.jewelry.orm.entity.task.StockListingTask();
      listingTaskSH.setPercentage(0);
      listingTaskSH.setTaskState(TaskState.SCHEDULED);
      syncingTask.setListingTaskForShanghai(listingTaskSH);

      com.ambergarden.jewelry.orm.entity.task.StockListingTask listingTaskSZ
         = new com.ambergarden.jewelry.orm.entity.task.StockListingTask();
      listingTaskSZ.setPercentage(0);
      listingTaskSZ.setTaskState(TaskState.SCHEDULED);
      syncingTask.setListingTaskForShenzhen(listingTaskSZ);

      syncingTask = stockSyncingTaskRepository.save(syncingTask);
      taskExecutor.execute(stockSyncingTaskExecutor);

      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }

   public StockSyncingTask update(StockSyncingTask task) {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = stockSyncingTaskConverter.convertTo(task);
      syncingTask = stockSyncingTaskRepository.save(syncingTask);
      return stockSyncingTaskConverter.convertFrom(syncingTask);
   }
}