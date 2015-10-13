package com.ambergarden.jewelry.service.stock;

import static com.ambergarden.jewelry.Constants.PREFIX_SH;
import static com.ambergarden.jewelry.Constants.PREFIX_SZ;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.stock.MinuteDataConverter;
import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.converter.stock.TradingInfoConverter;
import com.ambergarden.jewelry.orm.entity.stock.StockCategory;
import com.ambergarden.jewelry.orm.entity.task.StockSyncingTask;
import com.ambergarden.jewelry.orm.repository.stock.StockRepository;
import com.ambergarden.jewelry.orm.repository.task.StockSyncingTaskRepository;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockStatistics;
import com.ambergarden.jewelry.schema.beans.stock.StockTradings;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@Service
public class StockService {
   @Autowired
   private StockTradingInfoProvider tradingInfoProvider;

   @Autowired
   private StockRepository stockRepository;

   @Autowired
   private StockSyncingTaskRepository stockSyncingTaskRepository;

   @Autowired
   private StockConverter stockConverter;

   @Autowired
   private TradingInfoConverter tradingInfoConverter;

   @Autowired
   private MinuteDataConverter minuteDataConverter;

   public List<Stock> findAll() {
      return stockConverter.convertListFrom(stockRepository.findAll());
   }

   public Stock findById(int id) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockRepository.findOne(id);
      if (stockMO == null) {
         return null;
      }

      return stockConverter.convertFrom(stockMO);
   }

   public Stock create(Stock stock) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockConverter.convertTo(stock);
      stockMO = stockRepository.save(stockMO);
      return stockConverter.convertFrom(stockMO);
   }

   public Stock update(int stockId, Stock stock) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockConverter.convertTo(stock);
      stockMO = stockRepository.save(stockMO);
      return stockConverter.convertFrom(stockMO);
   }

   public StockStatistics getStatistics() {
      StockStatistics statistics = new StockStatistics();
      statistics.setStockCountSH(stockRepository.countByStockCategory(StockCategory.SHANGHAI));
      statistics.setStockCountSZ(stockRepository.countByStockCategory(StockCategory.SHENZHEN));

      StockSyncingTask lastSyncingTask = stockSyncingTaskRepository.findFirstByOrderByStartTimeDesc();
      if (lastSyncingTask != null) {
         statistics.setLastSyncTime(lastSyncingTask.getEndTime());
      }
      return statistics;
   }

   public StockTradings getTradings(String name) {
      String code = "";
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockRepository.findByName(name);
      if (stockMO != null) {
         String prefix = stockMO.getStockCategory() == StockCategory.SHANGHAI ? PREFIX_SH : PREFIX_SZ;
         code = prefix + stockMO.getCode();
      } else {
         // TODO: We need to support searching from code
         code = name;
      }

      List<TradingInfo> tradingInfos = tradingInfoProvider.getDailyTraidingInfo(code);
      List<MinuteData> minuteDatas = tradingInfoProvider.getPerMinuteTradingInfo(code);

      StockTradings stockTradings = new StockTradings();
      if (stockMO != null) {
         // For some specific types of stock, like sh000001, will not have a corresponding stock
         stockTradings.setStock(stockConverter.convertFrom(stockMO));
      }
      stockTradings.getTradingInfos().addAll(tradingInfoConverter.convertListFrom(tradingInfos));
      stockTradings.getMinuteDatas().addAll(minuteDataConverter.convertListFrom(minuteDatas));
      return stockTradings;
   }
}