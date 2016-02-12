package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class ModelAnalyser {
   public List<Tag> analyseFullDay(Stock stock, List<TradingInfo> tradingInfoList) {
      List<Tag> result = new ArrayList<Tag>();
      if (tradingInfoList.size() < 30) {
         return result;
      }

      // Price should go higher but not so high
      TradingInfo lastDay = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo prevDay = tradingInfoList.get(tradingInfoList.size() - 2);
      if (lastDay.getClose() > prevDay.getClose() * 1.09
            || lastDay.getClose() < prevDay.getClose() * 1.02) {
         return result;
      }

      // We have already went too high yesterday
      TradingInfo thirdDay = tradingInfoList.get(tradingInfoList.size() - 3);
      if (thirdDay.getClose() * 1.09 < prevDay.getClose()) {
         return result;
      }

      // The volume should increase
      if (lastDay.getVolume() < prevDay.getVolume() * 1.3) {
         return result;
      }

      int topIndex = findRegionalTop(tradingInfoList, tradingInfoList.size() - 2, tradingInfoList.size() - 31);
      while (topIndex != -1 && topIndex != 0) {
         // All prices between topIndex and the last price is in a reasonable boundary
         double basePrice = lastDay.getClose() * 0.75;
         if (!regionAbovePrice(tradingInfoList, tradingInfoList.size() - 1, topIndex, basePrice)) {
            break;
         }

         TradingInfo tradingInfo = tradingInfoList.get(topIndex);
         double topPrice = tradingInfo.getClose() > tradingInfo.getOpen() ? tradingInfo.getClose() : tradingInfo.getOpen();
         if (!regionBelowPrice(tradingInfoList, topIndex, topIndex - 8, topPrice * 1.03)) {
            break;
         }

         if (topPrice > lastDay.getClose() * 1.02) {
            // We have more higher price
            break;
         }
         if (topPrice * 1.03 > lastDay.getClose()) {
            if (topIndex > tradingInfoList.size() - 11
                  && (!regionAbovePrice(tradingInfoList, topIndex, tradingInfoList.size() - 11, basePrice)
                        || !regionAbovePrice(tradingInfoList, topIndex + 3, topIndex - 4, topPrice * 0.9))) {
               break;
            }
            result.add(new Tags.ModelBreakBoundaryTag(Tags.ModelBreakBoundaryTag.Type.HIGH_RATIO));
            break;
         }
         topIndex = findRegionalTop(tradingInfoList, topIndex - 1, tradingInfoList.size() - 31);
      }
      return result;
   }

   private boolean regionAbovePrice(List<TradingInfo> tradingInfoList, int startIndex, int endIndex, double price) {
      if (startIndex > tradingInfoList.size() - 1) {
         startIndex = tradingInfoList.size() - 1;
      }
      if (endIndex < 0) {
         endIndex = 0;
      }

      for (int index = startIndex; index > endIndex; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         if (tradingInfo.getClose() < price) {
            return false;
         }
      }
      return true;
   }

   private boolean regionBelowPrice(List<TradingInfo> tradingInfoList, int startIndex, int endIndex, double price) {
      if (startIndex > tradingInfoList.size() - 1) {
         startIndex = tradingInfoList.size() - 1;
      }
      if (endIndex < 0) {
         endIndex = 0;
      }

      for (int index = startIndex; index > endIndex; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         if (tradingInfo.getClose() > price) {
            return false;
         }
      }
      return true;
   }

   private int findRegionalTop(List<TradingInfo> tradingInfoList, int startIndex, int endIndex) {
      for (int index = startIndex; index > endIndex; index--) {
         boolean isHighest = true;
         TradingInfo tradingInfo = tradingInfoList.get(index);
         double highPrice = tradingInfo.getOpen() > tradingInfo.getClose() ? tradingInfo.getOpen() : tradingInfo.getClose();
         int startRegionIndex = index + 3;
         if (startRegionIndex > tradingInfoList.size() - 1) {
            startRegionIndex = tradingInfoList.size() - 1;
         }
         for (int regionIndex = startRegionIndex; regionIndex >= index - 3
               && regionIndex >= 0; regionIndex--) {
            TradingInfo regionInfo = tradingInfoList.get(regionIndex);
            if (regionInfo.getOpen() > highPrice || regionInfo.getClose() > highPrice) {
               isHighest = false;
            }
         }

         if (isHighest) {
            return index;
         }
      }
      return -1;
   }

   public List<Tag> analyseHalfDay(Stock stock, List<TradingInfo> tradingInfoList, List<MinuteData> minuteDataList) {
      List<Tag> result = new ArrayList<Tag>();
      if (tradingInfoList.size() < 5 || minuteDataList.size() < 32) {
         return result;
      }

      TradingInfo lastDay = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo prevDay = tradingInfoList.get(tradingInfoList.size() - 2);
      if (lastDay.getClose() > prevDay.getClose() * 1.09) {
         return result;
      }

      long volumeMA5 = AnalyserUtils.getAverageVolume(tradingInfoList, 5);
      long averageVolume = volumeMA5 / 240;
      double preHighest = AnalyserUtils.getHighest(tradingInfoList, 10);
      double lowest = AnalyserUtils.getLowest(tradingInfoList, 10);

      MinuteData firstMinute = minuteDataList.get(minuteDataList.size() - 3);
      MinuteData lastMinute = minuteDataList.get(minuteDataList.size() - 32);
      double open = firstMinute.getPrice();
      double close = lastMinute.getPrice();

      double high = 0;
      long totalVolume = 0;
      long maxAverageVolume = 0;
      int maxAverageVolumeIndex = 0;
      int counter = 1;
      for (int index = minuteDataList.size() - 1; index >= minuteDataList.size() - 32; index--) {
         MinuteData minuteData = minuteDataList.get(index);
         if (minuteData.getTime().compareTo("09:30:00") < 0) {
            continue;
         }

         totalVolume += minuteData.getVolume();
         if (high < minuteData.getPrice()) {
            high = minuteData.getPrice();
         }

         if (totalVolume / counter > maxAverageVolume) {
            maxAverageVolume = totalVolume / counter;
            maxAverageVolumeIndex = index;
         }
         counter++;
      }

      long averageVolume30 = totalVolume / counter;
      if (close - open < (high - open) * 3 / 4
            || lastDay.getClose() * 1.06 < close
            || lastDay.getClose() * 0.97 > open
            || lastDay.getClose() * 1.02 > high
            || averageVolume30 < averageVolume * 2
            || maxAverageVolumeIndex >= minuteDataList.size() - 10) {
         return result;
      }

      boolean breakOut = high > preHighest * 0.97
            && high < preHighest * 1.03;
      if (averageVolume30 > averageVolume * 3) {
         if (breakOut) {
            if (preHighest * 0.7 < lowest) {
               result.add(new Tags.ModelBreakBoundaryTag(Tags.ModelBreakBoundaryTag.Type.HIGH_RATIO));
            } else {
               result.add(new Tags.ModelBreakPreviousHighestTag());
            }
         } else {
            if (close < lowest * 1.15) {
               result.add(new Tags.ModelStartAtBottomTag());
            } else {
               result.add(new Tags.ModelTradingRatioHighTag());
            }
         }
      } else if (averageVolume30 > averageVolume * 2) {
         if (breakOut && preHighest * 0.7 < lowest) {
            result.add(new Tags.ModelBreakBoundaryTag(Tags.ModelBreakBoundaryTag.Type.LOW_RATIO));
         }
      }
      return result;
   }
}