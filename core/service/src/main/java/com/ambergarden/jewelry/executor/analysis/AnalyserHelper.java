package com.ambergarden.jewelry.executor.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

public class AnalyserHelper {
   public static Map<String, TradingInfo> constructTradingInfoMap(List<TradingInfo> marketTradingInfo) {
      Map<String, TradingInfo> tradingInfoMap = new HashMap<String, TradingInfo>();
      for (TradingInfo tradingInfo : marketTradingInfo) {
         String dayString = tradingInfo.getDay();
         tradingInfoMap.put(dayString, tradingInfo);
      }
      return tradingInfoMap;
   }
}