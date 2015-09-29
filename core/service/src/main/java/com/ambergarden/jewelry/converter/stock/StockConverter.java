package com.ambergarden.jewelry.converter.stock;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

public class StockConverter
   extends AbstractEntityListConverter<com.ambergarden.jewelry.orm.entity.stock.Stock, Stock> {

   @Override
   public Stock convertFrom(com.ambergarden.jewelry.orm.entity.stock.Stock mo) {
      Stock stock = new Stock();
      stock.setId(mo.getId());
      stock.setLockVersion(mo.getLockVersion());
      stock.setName(mo.getName());
      stock.setCode(mo.getCode());
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
      return stock;
   }
}