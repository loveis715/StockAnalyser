package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.TestScanTask;

@Component
public class TestScanTaskConverter
      extends AbstractTaskConverter<com.ambergarden.jewelry.orm.entity.task.TestScanTask, TestScanTask> {

   @Autowired
   private TestScanResultConverter testScanResultConverter;

   @Override
   public TestScanTask convertFrom(
         com.ambergarden.jewelry.orm.entity.task.TestScanTask mo) {
      TestScanTask testScanTask = new TestScanTask();
      super.convertFrom(mo, testScanTask);
      if (mo.getResults() != null) {
         testScanTask.getResults().addAll(
               testScanResultConverter.convertListFrom(mo.getResults()));
      }
      return testScanTask;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.TestScanTask convertTo(
         TestScanTask dto) {
      com.ambergarden.jewelry.orm.entity.task.TestScanTask testScanTask
         = new com.ambergarden.jewelry.orm.entity.task.TestScanTask();
      super.convertTo(dto, testScanTask);
      testScanTask.setResults(testScanResultConverter.convertListTo(dto.getResults()));
      return testScanTask;
   }
}