package com.ambergarden.jewelry.orm.entity.stock;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ambergarden.jewelry.orm.entity.AbstractVersionedEntity;

@Entity
public class Stock extends AbstractVersionedEntity {
   private String name;

   private String code;

   @Enumerated(EnumType.STRING)
   private StockCategory stockCategory;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public StockCategory getStockCategory() {
      return stockCategory;
   }

   public void setStockCategory(StockCategory stockCategory) {
      this.stockCategory = stockCategory;
   }
}