package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.schema.beans.task.TestScanResult;


@Component
public class TestScanResultConverter
   extends AbstractEntityListConverter<
      com.ambergarden.jewelry.orm.entity.task.TestScanResult, TestScanResult> {

   @Autowired
   private StockConverter stockConverter;

   @Override
   public TestScanResult convertFrom(
         com.ambergarden.jewelry.orm.entity.task.TestScanResult mo) {
      TestScanResult testScanResult = new TestScanResult();
      testScanResult.setId(mo.getId());
      testScanResult.setTag(mo.getTag());
      testScanResult.setDay(mo.getDay());
      testScanResult.setPriceChange(mo.getPriceChange());
      testScanResult.setStock(stockConverter.convertFrom(mo.getStock()));
      return testScanResult;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.TestScanResult convertTo(
         TestScanResult dto) {
      com.ambergarden.jewelry.orm.entity.task.TestScanResult testScanResult
         = new com.ambergarden.jewelry.orm.entity.task.TestScanResult();
      testScanResult.setId(dto.getId());
      testScanResult.setTag(dto.getTag());
      testScanResult.setDay(dto.getDay());
      testScanResult.setPriceChange(dto.getPriceChange());
      testScanResult.setStock(stockConverter.convertTo(dto.getStock()));
      return testScanResult;
   }
}