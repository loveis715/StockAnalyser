package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.schema.beans.task.ScanResult;

@Component
public class ScanResultConverter
   extends AbstractEntityListConverter<
      com.ambergarden.jewelry.orm.entity.task.ScanResult, ScanResult> {

   @Autowired
   private StockConverter stockConverter;

   @Override
   public ScanResult convertFrom(
         com.ambergarden.jewelry.orm.entity.task.ScanResult mo) {
      ScanResult scanResult = new ScanResult();
      scanResult.setId(mo.getId());
      scanResult.setScore(mo.getScore());
      scanResult.setTags(mo.getTags());
      scanResult.setStock(stockConverter.convertFrom(mo.getStock()));
      return scanResult;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.ScanResult convertTo(
         ScanResult dto) {
      com.ambergarden.jewelry.orm.entity.task.ScanResult scanResult
         = new com.ambergarden.jewelry.orm.entity.task.ScanResult();
      scanResult.setId(dto.getId());
      scanResult.setScore(dto.getScore());
      scanResult.setTags(dto.getTags());
      scanResult.setStock(stockConverter.convertTo(dto.getStock()));
      return scanResult;
   }
}