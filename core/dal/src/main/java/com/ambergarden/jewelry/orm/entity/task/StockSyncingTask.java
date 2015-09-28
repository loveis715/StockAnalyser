package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StockSyncingTask extends AbstractTask {
   @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
   @JoinColumn(name="LISTING_TASK_ID")
   private StockListingTask listingTask;

   public StockListingTask getListingTask() {
      return this.listingTask;
   }

   public void setListingTask(StockListingTask listingTask) {
      this.listingTask = listingTask;
   }
}