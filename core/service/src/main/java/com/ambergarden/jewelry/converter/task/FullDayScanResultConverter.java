package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.schema.beans.task.FullDayScanResult;

@Component
public class FullDayScanResultConverter
   extends AbstractEntityListConverter<
      com.ambergarden.jewelry.orm.entity.task.FullDayScanResult, FullDayScanResult> {

   @Autowired
   private StockConverter stockConverter;

   @Override
   public FullDayScanResult convertFrom(
         com.ambergarden.jewelry.orm.entity.task.FullDayScanResult mo) {
      FullDayScanResult scanResult = new FullDayScanResult();
      scanResult.setId(mo.getId());
      scanResult.setTags(mo.getTags());
      scanResult.setStock(stockConverter.convertFrom(mo.getStock()));
      return scanResult;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.task.FullDayScanResult convertTo(
         FullDayScanResult dto) {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanResult scanResult
         = new com.ambergarden.jewelry.orm.entity.task.FullDayScanResult();
      scanResult.setId(dto.getId());
      scanResult.setTags(dto.getTags());
      scanResult.setStock(stockConverter.convertTo(dto.getStock()));
      return scanResult;
   }
}