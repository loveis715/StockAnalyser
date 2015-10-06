package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.ScanTask;

@Component
public class ScanTaskConverter
   extends AbstractTaskConverter<
      com.ambergarden.jewelry.orm.entity.task.ScanTask, ScanTask> {

   @Autowired
   private ScanResultConverter scanResultConverter;

   @Autowired
   private ScanTypeConverter scanTypeConverter;

   @Override
   public ScanTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.ScanTask mo) {
      ScanTask scanTask = new ScanTask();
      super.convertFrom(mo, scanTask);
      scanTask.setScanningStockName(mo.getScanningStockName());
      scanTask.setScanType(scanTypeConverter.convertFrom(mo.getScanType()));
      if (mo.getResults() != null) {
         scanTask.getResults().addAll(
               scanResultConverter.convertListFrom(mo.getResults()));
      }
      return scanTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.ScanTask convertTo(
         ScanTask dto) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = new com.ambergarden.jewelry.orm.entity.task.ScanTask();
      super.convertTo(dto, scanTask);
      scanTask.setScanningStockName(dto.getScanningStockName());
      scanTask.setScanType(scanTypeConverter.convertTo(dto.getScanType()));
      scanTask.setResults(scanResultConverter.convertListTo(dto.getResults()));
      return scanTask;
   }
}