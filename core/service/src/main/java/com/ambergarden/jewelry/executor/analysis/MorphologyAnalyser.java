package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class MorphologyAnalyser {
   public List<Tag> analyse(Stock stock, List<TradingInfo> tradingInfoList) {
      if (tradingInfoList.size() <= 1) {
         return new ArrayList<Tag>();
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      if (lastTrading.getVolume() < stock.getTotalVolume() * 0.03) {
         // No trade. No value to play with
         return new ArrayList<Tag>();
      }
      if (lastTrading.getClose() * stock.getTotalVolume() > 30000000000L) {
         // Large stock. No value to play with
         return new ArrayList<Tag>();
      }

      List<Tag> result = new ArrayList<Tag>();
      if (lastTrading.getOpen() > lastTrading.getClose()) {
         result.addAll(analysePriceDown(tradingInfoList));
      } else {
         result.addAll(analysePriceUp(tradingInfoList));
      }
      return result;
   }

   private List<Tag> analysePriceUp(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      if (isNearRecentHighestPrice(tradingInfoList)) {
         result.add(new Tags.RecentTopPriceTag());
      }
      return result;
   }

   private List<Tag> analysePriceDown(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      if (isNearRecentLowestPrice(tradingInfoList)) {
         result.add(new Tags.RecentBottomPriceTag());
      }
      return result;
   }

   // If one stock's price has gone steady for a while and the last trading
   // price is nearly the lowest of those days, it means that the stock's
   // may get up again
   // The qualified stock should have the following characteristics:
   // 1. The last trading price is nearly the lowest price of these days.
   // 2. The stock's price has run above the lowest price for at least 10 days.
   // 3. The price should have volatility, for at least 10%
   // 4. The lowest price should in the middle of those days
   private boolean isNearRecentLowestPrice(List<TradingInfo> tradingInfoList) {
      int counter = 0;
      int bottomCounter = 0;
      boolean nearBottom = false;
      double lowMax = 0;
      double lowMin = Double.MAX_VALUE;
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      for (int index = tradingInfoList.size() - 2; index >= 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         double lowPrice = tradingInfo.getClose() < tradingInfo.getOpen()
               ? tradingInfo.getClose() : tradingInfo.getOpen();
         if (lowPrice > lowMax) {
            lowMax = lowPrice;
         }
         if (lowPrice < lowMin && isRecentLowest(index, tradingInfoList)) {
            lowMin = lowPrice;
         }

         if (lowPrice * 1.01 < lastTrading.getClose()) {
            break;
         } else if (isRecentLowest(index, tradingInfoList)
               && lowMin * 0.95 < lastTrading.getClose()
               && lowMin * 1.05 >= lastTrading.getClose()) {
            nearBottom = true;
            if (bottomCounter == 0) {
               bottomCounter = counter;
            }
         }
         counter++;
      }
      return counter > 15 && nearBottom && bottomCounter + 2 < counter && lowMax > lowMin * 1.1;
   }

   // If one stock's price has gone steady for a while and the last trading
   // price is nearly the highest of those days, it means that the stock's
   // may get out of recent boundary and get a higher price
   // The qualified stock should have the following characteristics:
   // 1. The last trading price is nearly the highest price of these days.
   // 2. The stock's price has run under the highest price for at least 10 days.
   // 3. The price should have volatility, for at least 10%
   // 4. The highest price should in the middle of those days
   private boolean isNearRecentHighestPrice(List<TradingInfo> tradingInfoList) {
      int counter = 0;
      int topCounter = 0;
      boolean nearTop = false;
      double highMax = 0;
      double highMin = Double.MAX_VALUE;
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      for (int index = tradingInfoList.size() - 2; counter < 40 && index >= 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         double highPrice = tradingInfo.getClose() > tradingInfo.getOpen()
               ? tradingInfo.getClose() : tradingInfo.getOpen();
         if (highPrice > highMax && isRecentHighest(index, tradingInfoList)) {
            highMax = highPrice;
         }
         if (highPrice < highMin) {
            highMin = highPrice;
         }

         if (highPrice * 0.99 > lastTrading.getClose()) {
            break;
         } else if (isRecentHighest(index, tradingInfoList)
               && highMax * 0.95 <= lastTrading.getClose()
               && highMax * 1.05 > lastTrading.getClose()) {
            nearTop = true;
            if (topCounter == 0) {
               topCounter = counter;
            }
         }
         counter++;
      }
      return counter > 15 && nearTop && topCounter + 2 < counter && highMax > highMin * 1.1;
   }

   private boolean isRecentHighest(int index, List<TradingInfo> tradingInfoList) {
      if (index > tradingInfoList.size() - 7 || index < 3) {
         return false;
      }

      TradingInfo tradingInfo = tradingInfoList.get(index);
      double highPrice = getHighValue(tradingInfo);
      return highPrice > getHighValue(tradingInfoList.get(index - 3))
            && highPrice > getHighValue(tradingInfoList.get(index - 2))
            && highPrice > getHighValue(tradingInfoList.get(index - 1))
            && highPrice > getHighValue(tradingInfoList.get(index + 1))
            && highPrice > getHighValue(tradingInfoList.get(index + 2))
            && highPrice > getHighValue(tradingInfoList.get(index + 3));
   }

   private boolean isRecentLowest(int index, List<TradingInfo> tradingInfoList) {
      if (index > tradingInfoList.size() - 7 || index < 3) {
         return false;
      }

      TradingInfo tradingInfo = tradingInfoList.get(index);
      double lowPrice = getLowValue(tradingInfo);
      return lowPrice > getLowValue(tradingInfoList.get(index - 3))
            && lowPrice > getLowValue(tradingInfoList.get(index - 2))
            && lowPrice > getLowValue(tradingInfoList.get(index - 1))
            && lowPrice > getLowValue(tradingInfoList.get(index + 1))
            && lowPrice > getLowValue(tradingInfoList.get(index + 2))
            && lowPrice > getLowValue(tradingInfoList.get(index + 3));
   }

   private double getHighValue(TradingInfo tradingInfo) {
      return tradingInfo.getOpen() > tradingInfo.getClose()
            ? tradingInfo.getOpen() : tradingInfo.getClose();
   }

   private double getLowValue(TradingInfo tradingInfo) {
      return tradingInfo.getOpen() < tradingInfo.getClose()
            ? tradingInfo.getOpen() : tradingInfo.getClose();
   }
}