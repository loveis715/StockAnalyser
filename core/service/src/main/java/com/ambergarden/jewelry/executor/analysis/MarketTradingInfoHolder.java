package com.ambergarden.jewelry.executor.analysis;

import java.util.Collections;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.stock.Stock;

public class MarketTradingInfoHolder {
   private final MarketTradingData tradingSH;
   private final MarketTradingData tradingSZ;
   private final List<Stock> pendingStocks;

   public MarketTradingInfoHolder(MarketTradingData tradingSH,
         MarketTradingData tradingSZ, List<Stock> pendingStock) {
      this.tradingSH = tradingSH;
      this.tradingSZ = tradingSZ;
      this.pendingStocks = Collections.synchronizedList(pendingStock);
   }

   public MarketTradingData getTradingSH() {
      return tradingSH;
   }

   public MarketTradingData getTradingSZ() {
      return tradingSZ;
   }

   public List<Stock> getPendingStocks() {
      return pendingStocks;
   }
}