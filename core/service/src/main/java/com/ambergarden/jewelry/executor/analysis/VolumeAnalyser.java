package com.ambergarden.jewelry.executor.analysis;

import static com.ambergarden.jewelry.executor.tag.TagValueMappings.VOLUME_TAG_LEVELS;
import static com.ambergarden.jewelry.executor.tag.TagValueMappings.VOLUME_TAG_MAX_SCORE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.analysis.bean.EnhancedTradingInfo;
import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class VolumeAnalyser {
   public static final double NORMAL_VOLUME = 1;
   public static final int VALID_TRADING_SIZE = 10;
   // Weight array for calculating the normal volume for stock. Use 0 to filter out today
   public static final int[] WEIGHT_ARRAY = new int[] {8, 9, 10, 11, 12, 13, 15, 17, 20, 0};

   public List<Tag> analyse(Stock stock, List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo) {
      if (!isValidForAnalysing(tradingInfoList, marketTradingInfo)) {
         return new ArrayList<Tag>();
      }

      return analyzeVolumeForThreeDays(stock, tradingInfoList, marketTradingInfo);
   }

   private boolean isValidForAnalysing(List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo) {
      Map<String, TradingInfo> tradingInfoMap = AnalyserHelper.constructTradingInfoMap(marketTradingInfo);
      List<TradingInfo> validTradingInfoList = getValidTradingInfoList(tradingInfoList, tradingInfoMap);
      if (validTradingInfoList.size() <= 5) {
         // Too little trading info
         return false;
      }

      // Whether the stock has trading today?
      TradingInfo lastTrading = validTradingInfoList.get(validTradingInfoList.size() - 1);
      TradingInfo lastMarketTrading = marketTradingInfo.get(marketTradingInfo.size() - 1);
      if (lastTrading.getDay().compareTo(lastMarketTrading.getDay()) != 0) {
         return false;
      }
      return true;
   }

   private List<Tag> analyzeVolumeForThreeDays(Stock stock,
         List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo) {
      double continousValue = 0;
      int continousStreak = 0;

      // Day 1: We will only include continuous incremental volume
      double baseVolume = getBaseVolume(tradingInfoList, marketTradingInfo, 2);
      TradingInfo targetTrading = tradingInfoList.get(tradingInfoList.size() - 3);
      double relativeVolume = targetTrading.getVolume() / baseVolume;
      if (relativeVolume > 1.3) {
         boolean found = false;
         List<Tag> tags = analyzeVolume(baseVolume, tradingInfoList, 2);
         for (Tag tag : tags) {
            if (Tags.VolumeIncrementTag.instanceOf(tag)) {
               continousValue = ((Tags.VolumeIncrementTag)tag).getValue();
               continousStreak++;
               found = true;
               break;
            }
         }

         if (!found) {
            baseVolume = getBaseVolume(tradingInfoList, marketTradingInfo, 1);
         }
      } else {
         baseVolume = getBaseVolume(tradingInfoList, marketTradingInfo, 1);
      }

      // Day 2: We will only include continuous incremental volume
      targetTrading = tradingInfoList.get(tradingInfoList.size() - 2);
      relativeVolume = targetTrading.getVolume() / baseVolume;
      if (relativeVolume > 1.3) {
         boolean found = false;
         List<Tag> tags = analyzeVolume(baseVolume, tradingInfoList, 1);
         for (Tag tag : tags) {
            if (Tags.VolumeIncrementTag.instanceOf(tag)) {
               continousValue += ((Tags.VolumeIncrementTag)tag).getValue();
               found = true;
               continousStreak++;
               break;
            }
         }

         if (!found) {
            continousStreak = 0;
            continousValue = 0;
            baseVolume = getBaseVolume(tradingInfoList, marketTradingInfo, 0);
         }
      } else {
         continousStreak = 0;
         continousValue = 0;
         baseVolume = getBaseVolume(tradingInfoList, marketTradingInfo, 0);
      }

      // Day 3
      List<Tag> result = new ArrayList<Tag>();
      targetTrading = tradingInfoList.get(tradingInfoList.size() - 1);
      relativeVolume = targetTrading.getVolume() / baseVolume;
      if (relativeVolume > 1.3) {
         boolean found = false;
         result = analyzeVolume(baseVolume, tradingInfoList, 0);
         for (Tag tag : result) {
            if (Tags.VolumeIncrementTag.instanceOf(tag)) {
               result.addAll(analyzeTradingRatio(stock, tradingInfoList.get(tradingInfoList.size() - 1)));
               continousValue += ((Tags.VolumeIncrementTag)tag).getValue();
               found = true;
               break;
            }
         }

         if (found && continousStreak > 0) {
            result.add(new Tags.ContinousVolumeIncrementTag(continousValue / 3));
         }
      }

      return result;
   }

   private List<Tag> analyzeVolume(double baseVolume, List<TradingInfo> tradingInfoList, int offset) {
      TradingInfo lastTrading = tradingInfoList.get(tradingInfoList.size() - 1 - offset);
      double relativeVolume = lastTrading.getVolume() / baseVolume;
      if (relativeVolume > 1.3) {
         return analyzeIncrementalVolume(relativeVolume, tradingInfoList, offset);
      } else if (relativeVolume < 0.7) {
         return analyzeDecrementalVolume(relativeVolume, tradingInfoList, offset);
      } else {
         // The stock is in normal trading state
         return new ArrayList<Tag>();
      }
   }

   private List<Tag> analyzeIncrementalVolume(double relativeVolume, List<TradingInfo> tradingInfoList, int offset) {
      List<Tag> result = new ArrayList<Tag>();
      List<EnhancedTradingInfo> tradingInfos = convertToEnhancedTradingInfo(tradingInfoList);
      EnhancedTradingInfo prevTradingInfo = tradingInfos.get(tradingInfoList.size() - 2 - offset);
      EnhancedTradingInfo lastTradingInfo = tradingInfos.get(tradingInfoList.size() - 1 - offset);
      if (prevTradingInfo.getPriceChange() > 0.095) {
         // Nearly no trade and reaches upper limit in previous day
         result.add(new Tags.VolumeIncrementWithHighPrice());
      } else if (prevTradingInfo.getPriceChange() < -0.095) {
         // Nearly no trade and reaches lower limit in previous day
         result.add(new Tags.VolumeIncrementWithLowPrice());
      } else if (lastTradingInfo.getPriceChange() < -0.04) {
         // Decrement with increased volume. Avoid from it
         result.add(new Tags.VolumeIncrementWithPriceDownTag());
      } else {
         // Volume increase with normal price. Need evaluate it
         result.add(calculateVolumeIncrementalTag(relativeVolume));
      }
      return result;
   }

   private List<Tag> analyzeTradingRatio(Stock stock, TradingInfo tradingInfo) {
      List<Tag> result = new ArrayList<Tag>();
      double ratio = (double)tradingInfo.getVolume() / stock.getTotalVolume();
      if (ratio < 0.05) {
         result.add(new Tags.TradingRatioLowTag());
      }
      return result;
   }

   private List<Tag> analyzeDecrementalVolume(double relativeVolume, List<TradingInfo> tradingInfoList, int offset) {
      List<Tag> result = new ArrayList<Tag>();
      List<EnhancedTradingInfo> tradingInfos = convertToEnhancedTradingInfo(tradingInfoList);
      EnhancedTradingInfo lastTradingInfo = tradingInfos.get(tradingInfoList.size() - 1 - offset);
      if (lastTradingInfo.getPriceChange() < -0.095) {
         // Nearly no trade and reaches lower limit
         result.add(new Tags.VolumeDecrementWithLowPrice());
      }
      return result;
   }

   private Tag calculateVolumeIncrementalTag(double relativeVolume) {
      int count = 0;
      for (double level : VOLUME_TAG_LEVELS) {
         if (relativeVolume < level) {
            break;
         }
         count++;
      }

      double value = (double)VOLUME_TAG_MAX_SCORE * count / VOLUME_TAG_LEVELS.length;
      return new Tags.VolumeIncrementTag(value);
   }

   private double getBaseVolume(List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo, int offset) {
      // Get corresponding tradings for each valid trading date
      Map<String, TradingInfo> tradingInfoMap = AnalyserHelper.constructTradingInfoMap(marketTradingInfo);
      List<TradingInfo> marketTradings = new ArrayList<TradingInfo>();
      List<TradingInfo> validTradingInfoList = new ArrayList<TradingInfo>();
      for (int index = 0; index < tradingInfoList.size() - offset; index++) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         TradingInfo marketInfo = tradingInfoMap.get(tradingInfo.getDay());
         if (marketInfo != null) {
            marketTradings.add(marketInfo);
            validTradingInfoList.add(tradingInfo);
         }
      }

      TradingInfo lastMarketTrading = marketTradingInfo.get(marketTradingInfo.size() - 1);
      double marketAverageVolume = getAverageVolume(marketTradings);
      double stockAverageVolume = getAverageVolume(validTradingInfoList);
      double ratio = lastMarketTrading.getVolume() / marketAverageVolume;
      ratio = ratio * 2 - 1;
      return stockAverageVolume * ratio;
   }

   private double getAverageVolume(List<TradingInfo> tradingInfoList) {
      if (tradingInfoList.size() > WEIGHT_ARRAY.length) {
         tradingInfoList = tradingInfoList.subList(
               tradingInfoList.size() - WEIGHT_ARRAY.length,
               tradingInfoList.size() - 1);
      }

      double totalVolume = 0;
      int totalWeight = 0;
      int offset = WEIGHT_ARRAY.length - tradingInfoList.size();
      for (int index = 0; index < tradingInfoList.size(); index++) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         int weight = WEIGHT_ARRAY[index + offset];
         totalWeight += weight;
         totalVolume += weight * tradingInfo.getVolume();
      }
      return totalVolume / totalWeight;
   }

   private List<TradingInfo> getValidTradingInfoList(List<TradingInfo> tradingInfoList, Map<String, TradingInfo> tradingInfoMap) {
      double prevClose = 0;
      List<TradingInfo> validTradings = new ArrayList<TradingInfo>();
      for (TradingInfo tradingInfo : tradingInfoList) {
         if (prevClose < 0.1) {
            // The first item, and we will use it for filtering
            prevClose = tradingInfo.getClose();
            continue;
         }
         prevClose = tradingInfo.getClose();

         // There isn't any corresponding trading in tradingInfoMap, which
         // means the trading info is for about 20 days ago, which provides
         // little value
         if (!tradingInfoMap.containsKey(tradingInfo.getDay())) {
            continue;
         }

         // Deal with the case that the price reaches up/down to limit.
         // That means this stock has reached to an abnormal state
         if (prevClose * 0.905 >= tradingInfo.getLow()) {
            continue;
         } else if (prevClose * 1.095 <= tradingInfo.getHigh()) {
            continue;
         } else {
            validTradings.add(tradingInfo);
         }
      }

      // We'd only want a list with max length VALID_TRADING_SIZE
      if (validTradings.size() > VALID_TRADING_SIZE) {
         validTradings.subList(validTradings.size() - VALID_TRADING_SIZE, validTradings.size());
      }
      return validTradings;
   }

   private List<EnhancedTradingInfo> convertToEnhancedTradingInfo(List<TradingInfo> tradingInfos) {
      double prevPrice = 0;
      List<EnhancedTradingInfo> enhancedTradingInfos = new ArrayList<EnhancedTradingInfo>();
      for (TradingInfo tradingInfo : tradingInfos) {
         EnhancedTradingInfo enhancedTradingInfo = new EnhancedTradingInfo();
         enhancedTradingInfo.setDay(tradingInfo.getDay());
         enhancedTradingInfo.setOpen(tradingInfo.getOpen());
         enhancedTradingInfo.setClose(tradingInfo.getClose());
         enhancedTradingInfo.setHigh(tradingInfo.getHigh());
         enhancedTradingInfo.setLow(tradingInfo.getLow());
         enhancedTradingInfo.setVolume(tradingInfo.getVolume());
         if (prevPrice > 0.1) {
            // Exclude the first item
            enhancedTradingInfo.setPriceChange(tradingInfo.getClose() / prevPrice - 1);
         }
         prevPrice = tradingInfo.getClose();

         enhancedTradingInfos.add(enhancedTradingInfo);
      }
      return enhancedTradingInfos;
   }
}