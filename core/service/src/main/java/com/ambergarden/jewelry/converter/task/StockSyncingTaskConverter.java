package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.StockSyncingTask;

@Component
public class StockSyncingTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask, StockSyncingTask> {

   @Autowired
   private StockListingTaskConverter listingConverter;

   @Override
   public StockSyncingTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.StockSyncingTask mo) {
      StockSyncingTask syncingTask = new StockSyncingTask();
      super.convertFrom(mo, syncingTask);
      syncingTask.setListingTask(listingConverter.convertFrom(mo.getListingTask()));
      return syncingTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.StockSyncingTask convertTo(
         StockSyncingTask dto) {
      com.ambergarden.jewelry.orm.entity.task.StockSyncingTask syncingTask
         = new com.ambergarden.jewelry.orm.entity.task.StockSyncingTask();
      super.convertTo(dto, syncingTask);
      syncingTask.setListingTask(listingConverter.convertTo(dto.getListingTask()));
      return syncingTask;
   }
}