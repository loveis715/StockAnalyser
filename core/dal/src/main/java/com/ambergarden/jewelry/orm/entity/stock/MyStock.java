package com.ambergarden.jewelry.orm.entity.stock;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ambergarden.jewelry.orm.entity.AbstractVersionedEntity;

@Entity
public class MyStock extends AbstractVersionedEntity {
   private Date addTime;

   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="STOCK_ID")
   private Stock stock;

   @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
   @JoinColumn(name="MY_STOCK_ID")
   private List<StockNote> stockNotes;

   public Date getAddTime() {
      return addTime;
   }

   public void setAddTime(Date addTime) {
      this.addTime = addTime;
   }

   public Stock getStock() {
      return stock;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public List<StockNote> getStockNotes() {
      return stockNotes;
   }

   public void setStockNotes(List<StockNote> stockNotes) {
      this.stockNotes = stockNotes;
   }
}