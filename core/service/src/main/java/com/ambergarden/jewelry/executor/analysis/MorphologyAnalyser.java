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
      result.addAll(analyseByPriceChanges(tradingInfoList));
      result.addAll(analyseByPriceMAs(tradingInfoList));
      result.addAll(analyseByVolume(stock, tradingInfoList));
      return result;
   }

   private List<Tag> analyseByPriceChanges(List<TradingInfo> tradingInfoList) {
      int tradingInfoSize = tradingInfoList.size();
      List<Tag> result = new ArrayList<Tag>();
      if (tradingInfoSize < 66) {
         return result;
      }

      double total5 = 0;
      for (int index = tradingInfoSize - 1; index > tradingInfoSize - 6; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         total5 += tradingInfo.getClose();
      }

      int counter = 0;
      for (int index = tradingInfoSize - 1; index > 0 && index > tradingInfoSize - 61; index--) {
         TradingInfo currentTrading = tradingInfoList.get(index);
         double MA5 = total5 / 5;
         if (currentTrading.getClose() > MA5 * 1.15 || currentTrading.getClose() < MA5 * 0.85) {
            counter = 0;
            break;
         }

         TradingInfo prevTrading = tradingInfoList.get(index - 1);
         if (currentTrading.getClose() > prevTrading.getClose() * 1.05
            || currentTrading.getClose() < prevTrading.getClose() * 0.95) {
            counter += 2;
         } else if (currentTrading.getClose() > prevTrading.getClose() * 1.03
            || currentTrading.getClose() < prevTrading.getClose() * 0.97) {
            counter++;
         }

         prevTrading = tradingInfoList.get(index - 6);
         total5 -= currentTrading.getClose();
         total5 += prevTrading.getClose();
      }

      if (counter >= 20) {
         result.add(new Tags.PriceUpDownTag());
      }
      return result;
   }

   private List<Tag> analyseByPriceMAs(List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      Map<String, PriceMAs> priceMAMap = AnalyserUtils.calculatePriceMAs(tradingInfoList);
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 2);
      boolean isPriceDown = lastTrading.getClose() - prevTrading.getClose() < prevTrading.getClose() * -0.01;
      if (isPriceDown) {
         result.addAll(analysePriceDownByShortTermMAs(tradingInfoList, priceMAMap));
         result.addAll(analysePriceDownByLongTermMAs(tradingInfoList, priceMAMap));
      } else {
         result.addAll(analysePriceUpByRecentTopPrice(tradingInfoList, priceMAMap));
      }
      return result;
   }

   private List<Tag> analysePriceDownByShortTermMAs(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      List<Tag> result = new ArrayList<Tag>();
      PriceMAs priceMAs = priceMAMap.get(lastTrading.getDay());
      if (priceMAs == null) {
         return result;
      }

      double lastPrice = lastTrading.getClose();
      double targetPrice = lastPrice * 0.97;
      if (priceMAs.getMA5() > targetPrice && priceMAs.getMA5() < lastPrice
         && isMA5InUpTrend(tradingInfoList, priceMAMap)) {
         result.add(new Tags.FindMA5SupportTag());
      }
      if (priceMAs.getMA10() > targetPrice && priceMAs.getMA10() < lastPrice
         && isMA10InUpTrend(tradingInfoList, priceMAMap)) {
         result.add(new Tags.FindMA10SupportTag());
      }
      if (priceMAs.getMA20() > targetPrice && priceMAs.getMA20() < lastPrice
         && isMA20InUpTrend(tradingInfoList, priceMAMap)) {
         result.add(new Tags.FindMA20SupportTag());
      }
      if (priceMAs.getMA30() > targetPrice && priceMAs.getMA30() < lastPrice
         && isMA30InUpTrend(tradingInfoList, priceMAMap)) {
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

   private boolean isMA5InUpTrend(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      if (tradingInfoList.size() - 5 < 0) {
         return false;
      }

      for (int index = tradingInfoList.size() - 1; index > tradingInfoList.size() - 5; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (priceMAs.getMA5() * 0.99 > tradingInfo.getClose()) {
            return false;
         }
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      PriceMAs currentMAs = priceMAMap.get(lastTrading.getDay());
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 5);
      PriceMAs prevMAs = priceMAMap.get(prevTrading.getDay());
      return currentMAs != null && prevMAs != null && currentMAs.getMA5() > prevMAs.getMA5() * 1.03;
   }

   private boolean isMA10InUpTrend(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      if (tradingInfoList.size() - 8 < 0) {
         return false;
      }

      for (int index = tradingInfoList.size() - 1; index > tradingInfoList.size() - 8; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (priceMAs.getMA10() * 0.99 > tradingInfo.getClose()) {
            return false;
         }
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      PriceMAs currentMAs = priceMAMap.get(lastTrading.getDay());
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 8);
      PriceMAs prevMAs = priceMAMap.get(prevTrading.getDay());
      return currentMAs != null && prevMAs != null && currentMAs.getMA10() > prevMAs.getMA10() * 1.04;
   }

   private boolean isMA20InUpTrend(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      if (tradingInfoList.size() - 13 < 0) {
         return false;
      }

      for (int index = tradingInfoList.size() - 1; index > tradingInfoList.size() - 13; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (priceMAs.getMA20() * 0.98 > tradingInfo.getClose()) {
            return false;
         }
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      PriceMAs currentMAs = priceMAMap.get(lastTrading.getDay());
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 13);
      PriceMAs prevMAs = priceMAMap.get(prevTrading.getDay());
      return currentMAs != null && prevMAs != null && currentMAs.getMA20() > prevMAs.getMA20() * 1.05;
   }

   private boolean isMA30InUpTrend(List<TradingInfo> tradingInfoList, Map<String, PriceMAs> priceMAMap) {
      if (tradingInfoList.size() - 18 < 0) {
         return false;
      }

      for (int index = tradingInfoList.size() - 1; index > tradingInfoList.size() - 18; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         PriceMAs priceMAs = priceMAMap.get(tradingInfo.getDay());
         if (priceMAs.getMA30() * 0.98 > tradingInfo.getClose()) {
            return false;
         }
      }

      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      PriceMAs currentMAs = priceMAMap.get(lastTrading.getDay());
      TradingInfo prevTrading = tradingInfoList.get(tradingInfoList.size() - 18);
      PriceMAs prevMAs = priceMAMap.get(prevTrading.getDay());
      return currentMAs != null && prevMAs != null && currentMAs.getMA30() > prevMAs.getMA30() * 1.06;
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
               && tradingInfo.getClose() * 1.03 > lastTradingInfo.getClose()
               && tradingInfo.getClose() < lastTradingInfo.getClose() * 1.05) {
               if (lowest * 1.15 < tradingInfo.getClose()) {
                  result.add(new Tags.BreakBoundaryTag());
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

   private List<Tag> analyseByVolume(Stock stock, List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      if (isVolumeIncrement(stock, tradingInfoList)) {
         result.add(new Tags.VolumeIncrementTag(1));
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
}