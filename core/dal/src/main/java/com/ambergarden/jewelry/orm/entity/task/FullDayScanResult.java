package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ambergarden.jewelry.orm.entity.AbstractEntity;
import com.ambergarden.jewelry.orm.entity.stock.Stock;

@Entity
public class FullDayScanResult extends AbstractEntity {
   @OneToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="STOCK_ID")
   private Stock stock;

   private String tags;

   public Stock getStock() {
      return stock;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public String getTags() {
      return tags;
   }

   public void setTags(String tags) {
      this.tags = tags;
   }
}