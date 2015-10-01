package com.ambergarden.jewelry.converter.stock;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.stock.StockCategory;

@Component
public class StockCategoryConverter {
   public StockCategory convertFrom(com.ambergarden.jewelry.orm.entity.stock.StockCategory category) {
      switch (category) {
      case SHANGHAI: return StockCategory.SHANGHAI;
      case SHENZHEN: return StockCategory.SHENZHEN;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }

   public com.ambergarden.jewelry.orm.entity.stock.StockCategory convertTo(StockCategory category) {
      switch (category) {
      case SHANGHAI: return com.ambergarden.jewelry.orm.entity.stock.StockCategory.SHANGHAI;
      case SHENZHEN: return com.ambergarden.jewelry.orm.entity.stock.StockCategory.SHENZHEN;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }
}