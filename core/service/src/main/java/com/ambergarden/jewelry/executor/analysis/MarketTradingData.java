package com.ambergarden.jewelry.executor.analysis;

import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

public class MarketTradingData {
   private final List<TradingInfo> tradingInfo;
   private final List<MinuteData> minuteData;

   public MarketTradingData(List<TradingInfo> tradingInfo,
         List<MinuteData> minuteData) {
      this.tradingInfo = tradingInfo;
      this.minuteData = minuteData;
   }

   public List<TradingInfo> getTradingInfo() {
      return tradingInfo;
   }

   public List<MinuteData> getMinuteData() {
      return minuteData;
   }
}