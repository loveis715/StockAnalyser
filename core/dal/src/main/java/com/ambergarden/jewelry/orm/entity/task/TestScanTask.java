package com.ambergarden.jewelry.orm.entity.task;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class TestScanTask extends AbstractTask {
   @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
   @JoinColumn(name="TEST_SCAN_TASK_ID")
   private List<TestScanResult> results;

   public List<TestScanResult> getResults() {
      return results;
   }

   public void setResults(List<TestScanResult> results) {
      this.results = results;
   }
}