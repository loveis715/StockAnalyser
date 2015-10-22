package com.ambergarden.jewelry.converter.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.orm.entity.stock.MyStock;

@Component
public class MyStockConverter
   extends AbstractEntityListConverter<MyStock, com.ambergarden.jewelry.schema.beans.stock.MyStock> {
   @Autowired
   private StockConverter stockConverter;

   @Autowired
   private StockNotesConverter stockNotesConverter;

   @Override
   public com.ambergarden.jewelry.schema.beans.stock.MyStock convertFrom(MyStock mo) {
      com.ambergarden.jewelry.schema.beans.stock.MyStock myStock
         = new com.ambergarden.jewelry.schema.beans.stock.MyStock();
      myStock.setId(mo.getId());
      myStock.setAddTime(mo.getAddTime());
      myStock.setLockVersion(mo.getLockVersion());
      myStock.setStock(stockConverter.convertFrom(mo.getStock()));
      if (mo.getStockNotes() != null) {
         myStock.getStockNotes().addAll(stockNotesConverter.convertListFrom(mo.getStockNotes()));
      }
      return myStock;
   }

   @Override
   public MyStock convertTo(com.ambergarden.jewelry.schema.beans.stock.MyStock dto) {
      MyStock myStock = new MyStock();
      myStock.setId(dto.getId());
      myStock.setAddTime(dto.getAddTime());
      myStock.setLockVersion(dto.getLockVersion());
      myStock.setStock(stockConverter.convertTo(dto.getStock()));
      if (dto.getStockNotes() != null) {
         myStock.setStockNotes(stockNotesConverter.convertListTo(dto.getStockNotes()));
      }
      return myStock;
   }
}