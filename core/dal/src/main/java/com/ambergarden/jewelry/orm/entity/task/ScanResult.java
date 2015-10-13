package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ambergarden.jewelry.orm.entity.AbstractEntity;
import com.ambergarden.jewelry.orm.entity.stock.Stock;

@Entity
public class ScanResult extends AbstractEntity {
   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="STOCK_ID")
   private Stock stock;

   @Column(name="score", columnDefinition="numeric(5, 2)")
   private double score;

   private String tags;

   public Stock getStock() {
      return stock;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public double getScore() {
      return score;
   }

   public void setScore(double score) {
      this.score = score;
   }

   public String getTags() {
      return tags;
   }

   public void setTags(String tags) {
      this.tags = tags;
   }
}