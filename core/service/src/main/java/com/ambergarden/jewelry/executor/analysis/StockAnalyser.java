package com.ambergarden.jewelry.executor.analysis;

import static com.ambergarden.jewelry.Constants.PREFIX_SH;
import static com.ambergarden.jewelry.Constants.PREFIX_SZ;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
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

   public List<Tag> analysis(Stock stock, List<TradingInfo> marketTradingInfo) {
      List<TradingInfo> tradingInfoList = retrieveTradingInfo(stock);

      List<Tag> result = volumeAnalyser.analyse(tradingInfoList, marketTradingInfo);
      result.addAll(priceAnalyser.analyse(tradingInfoList, marketTradingInfo));
      return result;
   }

   private List<TradingInfo> retrieveTradingInfo(Stock stock) {
      String prefix = stock.getStockCategory() == StockCategory.SHANGHAI ? PREFIX_SH : PREFIX_SZ;
      String code = prefix + stock.getCode();
      return tradingInfoProvider.getDailyTraidingInfo(code);
   }
}