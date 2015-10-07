package com.ambergarden.jewelry.orm.entity.task;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class ScanTask extends AbstractTask {
   private String scanningStockName;

   @Enumerated(EnumType.STRING)
   private ScanType scanType;

   @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST, orphanRemoval=true)
   @JoinColumn(name="SCAN_TASK_ID")
   private List<ScanResult> results;

   public String getScanningStockName() {
      return scanningStockName;
   }

   public void setScanningStockName(String scanningStockName) {
      this.scanningStockName = scanningStockName;
   }

   public ScanType getScanType() {
      return scanType;
   }

   public void setScanType(ScanType scanType) {
      this.scanType = scanType;
   }

   public List<ScanResult> getResults() {
      return results;
   }

   public void setResults(List<ScanResult> results) {
      this.results = results;
   }
}