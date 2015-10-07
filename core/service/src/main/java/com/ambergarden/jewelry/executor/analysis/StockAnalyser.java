package com.ambergarden.jewelry.executor.analysis;

import static com.ambergarden.jewelry.Constants.PREFIX_SH;
import static com.ambergarden.jewelry.Constants.PREFIX_SZ;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockCategory;
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

   public List<Tag> analysis(Stock stock, MarketTradingData marketTradingData) {
      List<TradingInfo> tradingInfoList = retrieveTradingInfo(stock);
      List<MinuteData> minuteDataList = retrieveMinuteDataList(stock);

      List<Tag> result = volumeAnalyser.analyse(tradingInfoList, marketTradingData.getTradingInfo());
      result.addAll(priceAnalyser.analyse(tradingInfoList, marketTradingData.getTradingInfo()));
      result.addAll(minuteTradingAnalyser.analyse(minuteDataList, marketTradingData.getMinuteData()));
      return result;
   }

   private List<TradingInfo> retrieveTradingInfo(Stock stock) {
      String prefix = stock.getStockCategory() == StockCategory.SHANGHAI ? PREFIX_SH : PREFIX_SZ;
      String code = prefix + stock.getCode();
      return tradingInfoProvider.getDailyTraidingInfo(code);
   }

   private List<MinuteData> retrieveMinuteDataList(Stock stock) {
      String prefix = stock.getStockCategory() == StockCategory.SHANGHAI ? PREFIX_SH : PREFIX_SZ;
      String code = prefix + stock.getCode();
      return tradingInfoProvider.getPerMinuteTradingInfo(code);
   }
}