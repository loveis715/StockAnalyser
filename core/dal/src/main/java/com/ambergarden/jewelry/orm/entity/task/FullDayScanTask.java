package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.Entity;

@Entity
public class FullDayScanTask extends AbstractTask {
   private String scanningStockName;

   public String getScanningStockName() {
      return this.scanningStockName;
   }

   public void setScanningStockName(String scanningStockName) {
      this.scanningStockName = scanningStockName;
   }
}