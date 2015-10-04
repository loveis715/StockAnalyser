package com.ambergarden.jewelry.executor.analysis;

import java.util.Collections;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

public class MarketTradingInfoHolder {
   private final List<TradingInfo> marketTradingDataSH;
   private final List<TradingInfo> marketTradingDataSZ;
   private final List<Stock> pendingStocks;

   public MarketTradingInfoHolder(List<TradingInfo> tradingInfoSH,
         List<TradingInfo> tradingInfoSZ, List<Stock> pendingStock) {
      this.marketTradingDataSH = tradingInfoSH;
      this.marketTradingDataSZ = tradingInfoSZ;
      this.pendingStocks = Collections.synchronizedList(pendingStock);
   }

   public List<TradingInfo> getTradingInfoSH() {
      return marketTradingDataSH;
   }

   public List<TradingInfo> getTradingInfoSZ() {
      return marketTradingDataSZ;
   }

   public List<Stock> getPendingStocks() {
      return pendingStocks;
   }
}