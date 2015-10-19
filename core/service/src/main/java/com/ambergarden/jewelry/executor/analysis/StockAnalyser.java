package com.ambergarden.jewelry.executor.analysis;

import static com.ambergarden.jewelry.executor.tag.TagValueMappings.LARGE_BILL_BASE;
import static com.ambergarden.jewelry.executor.tag.TagValueMappings.TOTAL_VALUE_LEVELS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.schema.beans.provider.stock.BillInfo;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Component
public class StockAnalyser {
   @Autowired
   private StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   private VolumeAnalyser volumeAnalyser;

   @Autowired
   private PriceAnalyser priceAnalyser;

   @Autowired
   private MinuteTradingAnalyser minuteTradingAnalyser;

   @Autowired
   private BillAnalyser billAnalyser;

   public List<Tag> analysis(Stock stock, MarketTradingData marketTradingData) {
      List<TradingInfo> tradingInfoList = tradingInfoProvider.getDailyTraidingInfo(stock.getCode());
      List<MinuteData> minuteDataList = tradingInfoProvider.getPerMinuteTradingInfo(stock.getCode());
      List<BillInfo> billDataList = retrieveLargeBillData(stock, tradingInfoList, marketTradingData);

      List<Tag> result = volumeAnalyser.analyse(stock, tradingInfoList, marketTradingData.getTradingInfo());
      result.addAll(priceAnalyser.analyse(tradingInfoList, marketTradingData.getTradingInfo()));
      result.addAll(minuteTradingAnalyser.analyse(minuteDataList, marketTradingData.getMinuteData()));
      result.addAll(billAnalyser.analyse(billDataList, getAmountBase(stock, tradingInfoList),
            stock, tradingInfoList, marketTradingData.getTradingInfo()));
      return result;
   }

   private List<BillInfo> retrieveLargeBillData(Stock stock,
         List<TradingInfo> tradingInfoList, MarketTradingData marketTradingData) {
      long amount = getAmountBase(stock, tradingInfoList);
      List<TradingInfo> marketTradingInfo = marketTradingData.getTradingInfo();
      TradingInfo lastMarket = marketTradingInfo.get(marketTradingInfo.size() - 1);
      String dayString = lastMarket.getDay();
      return tradingInfoProvider.getBillingInfo(stock.getCode(), amount, dayString);
   }

   private long getAmountBase(Stock stock, List<TradingInfo> tradingInfoList) {
      if (tradingInfoList == null || tradingInfoList.size() == 0) {
         return Long.MAX_VALUE;
      }

      double totalValue = stock.getTotalVolume() * tradingInfoList.get(tradingInfoList.size() - 1).getClose();
      int totalValueLevel = 0;
      for (double valueLevel : TOTAL_VALUE_LEVELS) {
         if (totalValue < valueLevel) {
            break;
         }

         totalValueLevel++;
      }

      return LARGE_BILL_BASE[totalValueLevel];
   }
}