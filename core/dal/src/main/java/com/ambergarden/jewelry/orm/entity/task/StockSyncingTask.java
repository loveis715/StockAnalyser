package com.ambergarden.jewelry.orm.entity.task;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StockSyncingTask extends AbstractTask {
   @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
   @JoinColumn(name="LISTING_TASK_FOR_SHANGHAI_ID")
   private StockListingTask listingTaskForShanghai;

   @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
   @JoinColumn(name="LISTING_TASK_FOR_SHENZHEN_ID")
   private StockListingTask listingTaskForShenzhen;

   public StockListingTask getListingTaskForShanghai() {
      return this.listingTaskForShanghai;
   }

   public void setListingTaskForShanghai(StockListingTask listingTaskForShanghai) {
      this.listingTaskForShanghai = listingTaskForShanghai;
   }

   public StockListingTask getListingTaskForShenzhen() {
      return this.listingTaskForShenzhen;
   }

   public void setListingTaskForShenzhen(StockListingTask listingTaskForShenzhen) {
      this.listingTaskForShenzhen = listingTaskForShenzhen;
   }
}