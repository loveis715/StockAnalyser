package com.ambergarden.jewelry.executor.analysis.bean;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

public class EnhancedTradingInfo extends TradingInfo {
   private double priceChange;

   public double getPriceChange() {
      return priceChange;
   }

   public void setPriceChange(double priceChange) {
      this.priceChange = priceChange;
   }
}