package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.analysis.bean.PriceMAs;
import com.ambergarden.jewelry.executor.analysis.bean.VolumeMAs;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class MorphologyAnalyser {
   public static final int MIN_TRADING_DATE = 90;
   public static final double MIN_TRADING_RATIO = 0.02;
   public static final long MAX_VOLUME = 30000000000L;

   public List<Tag> analyse(Stock stock, List<TradingInfo> tradingInfoList) {
      if (tradingInfoList.size() <= MIN_TRADING_DATE) {
         return new ArrayList<Tag>();
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      if (lastTrading.getVolume() < stock.getTotalVolume() * MIN_TRADING_RATIO) {
         // No trade. No value to play with
         return new ArrayList<Tag>();
      }
      if (lastTrading.getClose() * stock.getTotalVolume() > MAX_VOLUME) {
         // Large stock. No value to play with
         return new ArrayList<Tag>();
      }

      List<Tag> result = new ArrayList<Tag>();
      result.addAll(analyseVolumn(stock, tradingInfoList));
      if (lastTrading.getOpen() > lastTrading.getClose()) {
         result.addAll(analysePriceDown(tradingInfoList));
      } else {
         result.addAll(analysePriceUp(tradingInfoList));
      }
      result.addAll(analyseByModel(tradingInfoList));
      return result;
   }

   private List<Tag> analyseByModel(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      result.addAll(analysePriceByMAs(tradingInfoList));
      return result;
   }

   private List<Tag> analysePriceByMAs(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      Map<String, PriceMAs> priceMAMap = AnalyserUtils.calculatePriceMAs(tradingInfoList);
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 2);
      boolean isPriceDown = lastTrading.getClose() - prevTrading.getClose() < prevTrading.getClose() * -0.01;
      PriceMAs priceMAs = priceMAMap.get(lastTrading.getDay());
      if (isPriceDown && priceMAs != null) {
         result.addAll(analysePriceDownByShortTermMAs(lastTrading, priceMAs));
         result.addAll(analysePriceDownByLongTermMAs(tradingInfoList, priceMAMap));
      } else {
         result.addAll(analysePriceUpByRecentTopPrice(tradingInfoList, priceMAMap));
      }
      return result;
   }

   private List<Tag> analysePriceDownByShortTermMAs(TradingInfo lastTrading, PriceMAs priceMAs) {
      List<Tag> result = new ArrayList<Tag>();
      double lastPrice = lastTrading.getClose();
      double targetPrice = lastPrice * 0.97;
      if (priceMAs.getMA5() > targetPrice && priceMAs.getMA5() < lastPrice) {
         result.add(new Tags.FindMA5SupportTag());
      }
      if (priceMAs.getMA10() > targetPrice && priceMAs.getMA10() < lastPrice) {
         result.add(new Tags.FindMA10SupportTag());
      }
      if (priceMAs.getMA20() > targetPrice && priceMAs.getMA20() < lastPrice) {
         result.add(new Tags.FindMA20SupportTag());
      }
      if (priceMAs.getMA30() > targetPrice && priceMAs.getMA30() < lastPrice) {
         result.add(new Tags.FindMA30SupportTag());
      }
      return result;
   }

   private List<Tag> analysePriceDownByLongTermMAs(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      List<Tag> result = new ArrayList<Tag>();
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      double lastPrice = lastTrading.getClose();
      double targetPrice = lastPrice * 0.97;
      PriceMAs priceMAs = priceMAMap.get(lastTrading.getDay());
      if (priceMAs == null) {
         return result;
      }

      if (priceMAs.getMA120() > targetPrice && priceMAs.getMA120() < lastPrice
         && isRecentlyBreakThroughMA120(tradingInfoList, priceMAMap)) {
         result.add(new Tags.ConfirmMA120SupportTag());
      } else if (priceMAs.getMA250() > targetPrice && priceMAs.getMA250() < lastPrice
         && isRecentlyBreakThroughMA250(tradingInfoList, priceMAMap)) {
         result.add(new Tags.ConfirmMA250SupportTag());
      }
      return result;
   }

   private boolean isRecentlyBreakThroughMA120(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      int tradingListSize = tradingInfoList.size();
      int failCounter = Integer.MAX_VALUE;
      for (int index = tradingListSize - 1; index > tradingListSize - 11; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (tradingInfo.getClose() < priceMAs.getMA120()) {
            failCounter = 1;
         } else {
            failCounter--;
         }

         if (failCounter <= 0) {
            return false;
         }
      }
      return failCounter != Integer.MAX_VALUE;
   }

   private boolean isRecentlyBreakThroughMA250(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      int tradingListSize = tradingInfoList.size();
      int failCounter = Integer.MAX_VALUE;
      for (int index = tradingListSize - 1; index > tradingListSize - 11 && index > 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (tradingInfo.getClose() < priceMAs.getMA250()) {
            failCounter = 1;
         } else {
            failCounter--;
         }

         if (failCounter <= 0) {
            return false;
         }
      }
      return failCounter != Integer.MAX_VALUE;
   }

   private List<Tag> analysePriceUpByRecentTopPrice(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      double lowest = Double.MAX_VALUE;
      int tradingListSize = tradingInfoList.size();
      boolean needContinue = true;
      TradingInfo lastTradingInfo = tradingInfoList.get(tradingListSize - 1);
      List<Tag> result = new ArrayList<Tag>();
      for (int index = tradingListSize - 1; index > tradingListSize - 6 && index > 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         double closePrice = tradingInfo.getClose();
         if (closePrice > lastTradingInfo.getClose()) {
            needContinue = false;
         }
         if (lowest > tradingInfo.getClose()) {
            lowest = tradingInfo.getClose();
         }
      }

      if (needContinue) {
         for (int index = tradingListSize - 7; index > tradingListSize - 61 && index > 0; index--) {
            TradingInfo tradingInfo = tradingInfoList.get(index);
            if (isRegionalTopPrice(tradingInfoList, index)
               && tradingInfo.getClose() > lastTradingInfo.getClose()
               && tradingInfo.getClose() < lastTradingInfo.getClose() * 1.05) {
               if (lowest * 1.15 < tradingInfo.getClose()) {
                  result.add(new Tags.BreakBoundary());
                  break;
               }
            }
         }
      }
      return result;
   }

   private boolean isRegionalTopPrice(List<TradingInfo> tradingInfoList, int infoIndex) {
      int tradingInfoSize = tradingInfoList.size();
      TradingInfo tradingInfo = tradingInfoList.get(infoIndex);
      for (int index = infoIndex + 1; index < tradingInfoSize && index < infoIndex + 6; index++) {
         TradingInfo prevInfo = tradingInfoList.get(index);
         if (prevInfo.getClose() > tradingInfo.getClose()) {
            return false;
         }
      }

      for (int index = infoIndex - 1; index > 0 && index > infoIndex - 6; index--) {
         TradingInfo futureInfo = tradingInfoList.get(index);
         if (futureInfo.getClose() > tradingInfo.getClose()) {
            return false;
         }
      }
      return true;
   }

   private List<Tag> analyseVolumn(Stock stock, List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      if (isVolumeIncrement(stock, tradingInfoList)) {
         result.add(new Tags.VolumeIncrementTag(3));
      }
      VolumeMAs volumeMA = calculateVolumeMA(tradingInfoList);
      if (volumeMA.getMA5() < stock.getTotalVolume() * 0.05) {
         result.add(new Tags.TradingRatioLowTag());
      }
      return result;
   }

   private boolean isVolumeIncrement(Stock stock, List<TradingInfo> tradingInfoList) {
      int counter = 0;
      long totalTrading = 0;
      List<TradingInfo> subInfoList = new ArrayList<TradingInfo>();
      for (int index = tradingInfoList.size() - 1; index >= 0 && counter < 60; index--) {
         counter++;

         TradingInfo tradingInfo = tradingInfoList.get(index);
         if (tradingInfo.getVolume() < stock.getTotalVolume() * 0.02) {
            continue;
         }

         subInfoList.add(tradingInfo);
         totalTrading += tradingInfo.getVolume();
      }

      if (subInfoList.size() < 5) {
         return false;
      }

      counter = 0;
      int listSize = subInfoList.size();
      long subTotal = 0;
      for (int index = 0; index < listSize; index++) {
         TradingInfo tradingInfo = subInfoList.get(index);
         subTotal += tradingInfo.getVolume();
         counter++;

         if (counter > 5 && listSize - counter > 5) {
            long leftVolume = totalTrading - subTotal;
            if (subTotal / counter > leftVolume / (listSize - counter) * 1.5
                  && isInNormalPrice(index, subInfoList)) {
               return true;
            }
         }
      }
      return false;
   }

   private boolean isInNormalPrice(int startIndex, List<TradingInfo> tradingInfoList) {
      double priceTotal = 0;
      for (int index = 0; index < startIndex; index++) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         priceTotal += tradingInfo.getClose();
      }
      double basePrice = priceTotal / startIndex;

      int listSize = tradingInfoList.size();
      for (int index = startIndex; index < listSize; index++) {
         priceTotal += tradingInfoList.get(index).getClose();
      }
      return basePrice < priceTotal / (listSize - startIndex) * 1.5;
   }

   private VolumeMAs calculateVolumeMA(List<TradingInfo> tradingInfoList) {
      int counter = 0;
      long totalVolume = 0;
      VolumeMAs volumeMAs = new VolumeMAs();
      for (int index = tradingInfoList.size() - 1; counter < 30 && index >= 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         totalVolume += tradingInfo.getVolume();
         counter++;

         if (counter == 5) {
            volumeMAs.setMA5(totalVolume / 5);
         } else if (counter == 10) {
            volumeMAs.setMA10(totalVolume / 10);
         } else if (counter == 20) {
            volumeMAs.setMA20(totalVolume / 20);
         } else if (counter == 30) {
            volumeMAs.setMA30(totalVolume / 30);
         }
      }
      return volumeMAs;
   }

   private List<Tag> analysePriceUp(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      Tag tag = analyseByUpperBoundary(tradingInfoList);
      if (tag != null) {
         result.add(tag);
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
               && lowMin * 0.98 < lastTrading.getClose()
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
   private Tag analyseByUpperBoundary(List<TradingInfo> tradingInfoList) {
      int counter = 0;
      int topCounter = 0;
      boolean nearTop = false;
      double ratio = 0;
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
               ratio = lastTrading.getClose() / highMax;
            }
            if (counter < 30) {
               ratio = lastTrading.getClose() / highMax;
            }
         }
         counter++;
      }

      Tag tag = null;
      if (counter > 15 && nearTop && topCounter + 2 < counter && highMax > highMin * 1.1) {
         if (ratio > 1.02) {
            tag = new Tags.PriceOverUpperBoundaryTag();
         } else {
            tag = new Tags.RecentTopPriceTag();
         }
      }
      return tag;
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