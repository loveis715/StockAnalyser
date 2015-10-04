package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;

@Component
public class FullDayScanTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask, FullDayScanTask> {

   @Autowired
   private FullDayScanResultConverter fullDayScanResultConverter;

   @Override
   public FullDayScanTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.FullDayScanTask mo) {
      FullDayScanTask scanTask = new FullDayScanTask();
      super.convertFrom(mo, scanTask);
      scanTask.setScanningStockName(mo.getScanningStockName());
      if (mo.getResults() != null) {
         scanTask.getResults().addAll(
               fullDayScanResultConverter.convertListFrom(mo.getResults()));
      }
      return scanTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.FullDayScanTask convertTo(
         FullDayScanTask dto) {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask scanTask
         = new com.ambergarden.jewelry.orm.entity.task.FullDayScanTask();
      super.convertTo(dto, scanTask);
      scanTask.setScanningStockName(dto.getScanningStockName());
      scanTask.setResults(fullDayScanResultConverter.convertListTo(dto.getResults()));
      return scanTask;
   }
}