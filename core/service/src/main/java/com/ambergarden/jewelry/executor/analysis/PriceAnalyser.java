package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

@Component
public class PriceAnalyser {
   public List<Tag> analyse(List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo) {
      if (tradingInfoList.size() == 0) {
         return new ArrayList<Tag>();
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo lastMarket = marketTradingInfo.get(marketTradingInfo.size() - 1);
      if (lastTrading.getDay().compareTo(lastMarket.getDay()) != 0) {
         return new ArrayList<Tag>();
      }

      Map<String, TradingInfo> tradingInfoMap = AnalyserHelper.constructTradingInfoMap(marketTradingInfo);
      double adjustedPrice = getAdjustedPrice(tradingInfoList, tradingInfoMap);
      return analysePrice(lastTrading.getClose(), adjustedPrice);
   }

   private List<Tag> analysePrice(double lastPrice, double targetPrice) {
      List<Tag> result = new ArrayList<Tag>();
      if (lastPrice < targetPrice) {
         double score = (1 - lastPrice / targetPrice) * 10 - 1;
         if (score > 0) {
            result.add(new Tags.LowPriceTag(score));
         }
      } else {
         double score = (lastPrice / targetPrice - 1) * 10 - 1;
         if (score > 0) {
            result.add(new Tags.HighPriceTag(score));
         }
      }
      return result;
   }

   private double getAdjustedPrice(List<TradingInfo> tradingInfoList, Map<String, TradingInfo> tradingInfoMap) {
      double marketSum = 0;
      double priceSum = 0;
      List<TradingInfo> validTradings = new ArrayList<TradingInfo>();
      for (TradingInfo tradingInfo : tradingInfoList) {
         // There isn't any corresponding trading in tradingInfoMap, which
         // means the trading info is for about 20 days ago, which provides
         // little value
         TradingInfo marketTrading = tradingInfoMap.get(tradingInfo.getDay());
         if (marketTrading == null) {
            continue;
         }

         marketSum += marketTrading.getClose();
         priceSum += tradingInfo.getClose();

         validTradings.add(tradingInfo);
      }

      if (validTradings.size() < 5) {
         TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
         return lastTrading.getClose();
      }

      double averageMarket = marketSum / validTradings.size();
      double averagePrice = priceSum / validTradings.size();

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo lastMarket = tradingInfoMap.get(lastTrading.getDay());
      double priceFollowsMarket = averagePrice * lastMarket.getClose() / averageMarket;

      return (averagePrice + priceFollowsMarket) / 2;
   }
}