package com.ambergarden.jewelry.executor.analysis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class StockAnalyser {
   @Autowired
   private VolumeAnalyser volumeAnalyser;

   public List<Tag> analysis(Stock stock, List<TradingInfo> marketTradingInfo) {
      return volumeAnalyser.analyse(stock, marketTradingInfo);
   }
}