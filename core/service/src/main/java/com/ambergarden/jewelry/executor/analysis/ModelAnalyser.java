package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class ModelAnalyser {
   public List<Tag> analyseHalfDay(Stock stock, List<TradingInfo> tradingInfoList, List<TradingInfo> halfHourTradingList) {
      int counter = 0;
      long totalVolume = 0;
      for (int index = tradingInfoList.size() - 1; index >= 0 && counter < 5; index--) {
         counter++;

         TradingInfo tradingInfo = tradingInfoList.get(index);
         totalVolume += tradingInfo.getVolume();
      }

      counter = 0;
      double preHighest = 0;
      double lowest = Double.MAX_VALUE;
      for (int index = tradingInfoList.size() - 1; index >= 0 && counter < 15; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         if (tradingInfo.getOpen() > preHighest) {
            preHighest = tradingInfo.getOpen();
         }
         if (tradingInfo.getClose() > preHighest) {
            preHighest = tradingInfo.getClose();
         }
         if (tradingInfo.getOpen() < lowest) {
            lowest = tradingInfo.getOpen();
         }
         if (tradingInfo.getClose() < lowest) {
            lowest = tradingInfo.getClose();
         }
      }

      List<Tag> result = new ArrayList<Tag>();
      long averageVolume = totalVolume / 40;
      TradingInfo firstHalfHour = halfHourTradingList.get(0);
      if (firstHalfHour.getHigh() > preHighest * 0.97
         && firstHalfHour.getHigh() < preHighest * 1.03
         && firstHalfHour.getVolume() > averageVolume * 3
         && preHighest * 0.7 < lowest) {
         result.add(new Tags.ModelBreakBoundaryTag());
      }
      return result;
   }
}