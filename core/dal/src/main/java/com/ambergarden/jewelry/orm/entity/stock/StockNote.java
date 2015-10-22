package com.ambergarden.jewelry.orm.entity.stock;

import java.util.Date;

import javax.persistence.Entity;

import com.ambergarden.jewelry.orm.entity.AbstractEntity;

@Entity
public class StockNote extends AbstractEntity {
   private Date addTime;

   private String content;

   public Date getAddTime() {
      return addTime;
   }

   public void setAddTime(Date addTime) {
      this.addTime = addTime;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }
}