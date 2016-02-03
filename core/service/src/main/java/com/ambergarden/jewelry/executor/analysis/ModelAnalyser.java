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
      List<Tag> result = new ArrayList<Tag>();
      if (tradingInfoList.size() < 5) {
         return result;
      }

      TradingInfo lastDay = tradingInfoList.get(tradingInfoList.size() - 1);
      TradingInfo prevDay = tradingInfoList.get(tradingInfoList.size() - 2);
      if (lastDay.getClose() > prevDay.getClose() * 1.09) {
         return result;
      }

      long volumeMA5 = AnalyserUtils.getAverageVolume(tradingInfoList, 5);
      long averageVolume = volumeMA5 / 8;
      double preHighest = AnalyserUtils.getHighest(tradingInfoList, 10);
      double lowest = AnalyserUtils.getLowest(tradingInfoList, 10);

      TradingInfo firstHalfHour = halfHourTradingList.get(0);
      if (firstHalfHour.getClose() - firstHalfHour.getOpen() < (firstHalfHour.getHigh() - firstHalfHour.getOpen()) * 3 / 4
            || lastDay.getClose() * 1.06 < firstHalfHour.getClose()
            || lastDay.getClose() * 0.97 > firstHalfHour.getLow()) {
         return result;
      }

      boolean breakOut = firstHalfHour.getHigh() > preHighest * 0.97
            && firstHalfHour.getHigh() < preHighest * 1.03;
      if (firstHalfHour.getVolume() > averageVolume * 3) {
         if (breakOut) {
            if (preHighest * 0.7 < lowest) {
               result.add(new Tags.ModelBreakBoundaryTag(Tags.ModelBreakBoundaryTag.Type.HIGH_RATIO));
            } else {
               result.add(new Tags.ModelBreakPreviousHighestTag());
            }
         } else {
            if (firstHalfHour.getClose() < lowest * 1.15) {
               result.add(new Tags.ModelStartAtBottomTag());
            } else {
               result.add(new Tags.ModelTradingRatioHighTag());
            }
         }
      } else if (firstHalfHour.getVolume() > averageVolume * 2) {
         if (breakOut && preHighest * 0.7 < lowest) {
            result.add(new Tags.ModelBreakBoundaryTag(Tags.ModelBreakBoundaryTag.Type.LOW_RATIO));
         }
      }
      return result;
   }
}