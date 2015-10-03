package com.ambergarden.jewelry.converter.task;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;

@Component
public class FullDayScanTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask, FullDayScanTask> {

   @Override
   public FullDayScanTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.FullDayScanTask mo) {
      FullDayScanTask scanTask = new FullDayScanTask();
      super.convertFrom(mo, scanTask);
      scanTask.setScanningStockName(mo.getScanningStockName());
      return scanTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.FullDayScanTask convertTo(
         FullDayScanTask dto) {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask scanTask
         = new com.ambergarden.jewelry.orm.entity.task.FullDayScanTask();
      super.convertTo(dto, scanTask);
      scanTask.setScanningStockName(dto.getScanningStockName());
      return scanTask;
   }
}