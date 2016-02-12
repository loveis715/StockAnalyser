package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ambergarden.jewelry.orm.entity.AbstractEntity;
import com.ambergarden.jewelry.orm.entity.stock.Stock;

@Entity
public class TestScanResult extends AbstractEntity {
   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="STOCK_ID")
   private Stock stock;

   private String tag;

   private String day;

   @Column(name="price_change", columnDefinition="numeric(6, 4)")
   private double priceChange;

   public Stock getStock() {
      return stock;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public String getTag() {
      return tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public String getDay() {
      return day;
   }

   public void setDay(String day) {
      this.day = day;
   }

   public double getPriceChange() {
      return priceChange;
   }

   public void setPriceChange(double priceChange) {
      this.priceChange = priceChange;
   }
}