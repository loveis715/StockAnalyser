package com.ambergarden.jewelry.executor.analysis;

import java.util.Collections;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

public class MarketTradingInfoHolder {
   private final MarketTradingData marketTradingDataSH;
   private final MarketTradingData marketTradingDataSZ;
   private final List<Stock> pendingStocks;

   public MarketTradingInfoHolder(List<TradingInfo> tradingInfoSH,
         List<TradingInfo> tradingInfoSZ, List<Stock> pendingStock) {
      this.marketTradingDataSH = new MarketTradingData(tradingInfoSH);
      this.marketTradingDataSZ = new MarketTradingData(tradingInfoSZ);
      this.pendingStocks = Collections.synchronizedList(pendingStock);
   }

   public MarketTradingData getTradingInfoSH() {
      return marketTradingDataSH;
   }

   public MarketTradingData getTradingInfoSZ() {
      return marketTradingDataSZ;
   }

   public List<Stock> getPendingStocks() {
      return pendingStocks;
   }
}