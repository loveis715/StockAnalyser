package com.ambergarden.jewelry.executor.analysis;

import static com.ambergarden.jewelry.executor.tag.TagValueMappings.MASS_VOLUME_LEVELS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;

@Component
public class MinuteTradingAnalyser {
   public List<Tag> analyse(List<MinuteData> tradingMinuteData, List<MinuteData> marketMinuteData) {
      if (tradingMinuteData.size() <= 10) {
         // In case that we've nearly no trading
         return new ArrayList<Tag>();
      }

      return analyseVolume(tradingMinuteData, marketMinuteData);
   }

   private List<Tag> analyseVolume(List<MinuteData> tradingMinuteData, List<MinuteData> marketMinuteData) {
      List<MinuteData> adjustedVolume = adjustVolume(tradingMinuteData, marketMinuteData);
      return analyseAdjustedVolume(adjustedVolume);
   }

   private List<MinuteData> adjustVolume(List<MinuteData> tradingMinuteData,
         List<MinuteData> marketMinuteData) {
      double marketTotal = 0;
      for (MinuteData minuteData : marketMinuteData) {
         marketTotal += minuteData.getVolume();
      }
      double marketAverage = marketTotal / marketMinuteData.size();

      Map<String, MinuteData> minuteDataMap = new HashMap<String, MinuteData>();
      for (MinuteData minuteData : marketMinuteData) {
         minuteDataMap.put(minuteData.getTime(), minuteData);
      }

      List<MinuteData> adjustedData = new ArrayList<MinuteData>();
      for (int index = 0; index < tradingMinuteData.size(); index++) {
         MinuteData stockData = tradingMinuteData.get(index);
         MinuteData marketData = minuteDataMap.get(stockData.getTime());
         if (marketData == null) {
            continue;
         }

         double adjustedVolume = stockData.getVolume()
               / (marketData.getVolume() / marketAverage);
         MinuteData minuteData = new MinuteData();
         minuteData.setVolume((long)adjustedVolume);
         minuteData.setPrice(stockData.getPrice());
         minuteData.setTime(stockData.getTime());
         adjustedData.add(minuteData);
      }
      return adjustedData;
   }

   private List<Tag> analyseAdjustedVolume(List<MinuteData> adjustedVolume) {
      double stockTotal = 0;
      for (MinuteData minuteData : adjustedVolume) {
         stockTotal += minuteData.getVolume();
      }
      double stockAverage = stockTotal / adjustedVolume.size();

      int counter = 0;
      int totalScore = 0;
      double startPrice = 0;
      List<Tag> result = new ArrayList<Tag>();
      for (int index = adjustedVolume.size() - 1; index >= 0; index--) {
         MinuteData minuteData = adjustedVolume.get(index);
         int score = 0;
         for (int levelIndex = 0; levelIndex < MASS_VOLUME_LEVELS.length; levelIndex++) {
            if (minuteData.getVolume() < stockAverage * MASS_VOLUME_LEVELS[levelIndex]) {
               break;
            }
            score++;
         }

         if (score != 0) {
            if (counter == 0) {
               startPrice = minuteData.getPrice();
            }
            counter++;
            totalScore += score;
         } else {
            if (counter > 1) {
               if (minuteData.getPrice() > startPrice * 1.01) {
                  result.add(new Tags.MassPositiveTradingTag(totalScore * 0.3));
               } else if (minuteData.getPrice() < startPrice * 0.99) {
                  result.add(new Tags.MassNegativeTradingTag(totalScore * 0.3));
               }
            }
            startPrice = 0;
            totalScore = 0;
            counter = 0;
         }
      }
      return result;
   }
}