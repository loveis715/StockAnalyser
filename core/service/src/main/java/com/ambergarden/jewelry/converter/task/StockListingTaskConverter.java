package com.ambergarden.jewelry.converter.task;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.StockListingTask;

@Component
public class StockListingTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.StockListingTask, StockListingTask> {

   @Override
   public StockListingTask convertFrom(com.ambergarden.jewelry.orm.entity.task.StockListingTask mo) {
      StockListingTask listingTask = new StockListingTask();
      super.convertFrom(mo, listingTask);
      return listingTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.StockListingTask convertTo(StockListingTask dto) {
      com.ambergarden.jewelry.orm.entity.task.StockListingTask listingTask
         = new com.ambergarden.jewelry.orm.entity.task.StockListingTask();
      super.convertTo(dto, listingTask);
      return listingTask;
   }
}