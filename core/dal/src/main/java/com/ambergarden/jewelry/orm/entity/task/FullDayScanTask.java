package com.ambergarden.jewelry.orm.entity.task;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class FullDayScanTask extends AbstractTask {
   private String scanningStockName;

   @OneToMany(fetch=FetchType.EAGER, orphanRemoval=true)
   @JoinColumn(name="FULL_DAY_SCAN_TASK_ID")
   private List<FullDayScanResult> results;

   public String getScanningStockName() {
      return scanningStockName;
   }

   public void setScanningStockName(String scanningStockName) {
      this.scanningStockName = scanningStockName;
   }

   public List<FullDayScanResult> getResults() {
      return results;
   }

   public void setResults(List<FullDayScanResult> results) {
      this.results = results;
   }
}