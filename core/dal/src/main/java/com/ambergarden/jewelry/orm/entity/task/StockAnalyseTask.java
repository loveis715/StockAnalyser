package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ambergarden.jewelry.orm.entity.stock.Stock;

@Entity
public class StockAnalyseTask extends AbstractTask {
   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="STOCK_ID")
   private Stock stock;

   private String resultTags;

   public Stock getStock() {
      return stock;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public String getResultTags() {
      return resultTags;
   }

   public void setResultTags(String resultTags) {
      this.resultTags = resultTags;
   }
}