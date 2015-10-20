package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTask;

@Component
public class StockAnalyseTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask, StockAnalyseTask> {

   @Autowired
   private StockConverter stockConverter;

   @Override
   public StockAnalyseTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask mo) {
      StockAnalyseTask task = new StockAnalyseTask();
      super.convertFrom(mo, task);
      task.setResultTags(mo.getResultTags());
      task.setStock(stockConverter.convertFrom(mo.getStock()));
      return task;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask convertTo(
         StockAnalyseTask dto) {
      com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask task
         = new com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask();
      super.convertTo(dto, task);
      task.setResultTags(dto.getResultTags());
      task.setStock(stockConverter.convertTo(dto.getStock()));
      return task;
   }
}