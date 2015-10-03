package com.ambergarden.jewelry.executor.analysis;

import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

public class MarketTradingData {
   private final List<TradingInfo> tradingInfos;
   private long averageVolume;

   public MarketTradingData(List<TradingInfo> tradingInfos) {
      this.tradingInfos = tradingInfos;

      long totalVolume = 0;
      for (TradingInfo tradingInfo : tradingInfos) {
         totalVolume += tradingInfo.getVolume();
      }
      averageVolume = totalVolume / tradingInfos.size();
   }

   public List<TradingInfo> getTradingInfos() {
      return tradingInfos;
   }

   public long getAverageVolume() {
      return averageVolume;
   }
}