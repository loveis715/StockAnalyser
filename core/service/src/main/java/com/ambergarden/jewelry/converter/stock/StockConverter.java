package com.ambergarden.jewelry.converter.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class StockConverter
   extends AbstractEntityListConverter<com.ambergarden.jewelry.orm.entity.stock.Stock, Stock> {
   @Autowired
   private StockCategoryConverter categoryConverter;

   @Override
   public Stock convertFrom(com.ambergarden.jewelry.orm.entity.stock.Stock mo) {
      Stock stock = new Stock();
      stock.setId(mo.getId());
      stock.setLockVersion(mo.getLockVersion());
      stock.setName(mo.getName());
      stock.setCode(mo.getCode());
      stock.setTotalVolume(mo.getTotalVolume());
      stock.setStockCategory(categoryConverter.convertFrom(mo.getStockCategory()));
      return stock;
   }

   @Override
   public com.ambergarden.jewelry.orm.entity.stock.Stock convertTo(Stock dto) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stock
         = new com.ambergarden.jewelry.orm.entity.stock.Stock();
      stock.setId(dto.getId());
      stock.setLockVersion(dto.getLockVersion());
      stock.setName(dto.getName());
      stock.setCode(dto.getCode());
      stock.setTotalVolume(dto.getTotalVolume());
      stock.setStockCategory(categoryConverter.convertTo(dto.getStockCategory()));
      return stock;
   }
}