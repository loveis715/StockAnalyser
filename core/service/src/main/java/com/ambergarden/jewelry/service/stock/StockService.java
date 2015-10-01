package com.ambergarden.jewelry.service.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.orm.entity.stock.StockCategory;
import com.ambergarden.jewelry.orm.entity.task.StockSyncingTask;
import com.ambergarden.jewelry.orm.repository.stock.StockRepository;
import com.ambergarden.jewelry.orm.repository.task.StockSyncingTaskRepository;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.stock.StockStatistics;

@Service
public class StockService {
   @Autowired
   private StockRepository stockRepository;

   @Autowired
   private StockSyncingTaskRepository stockSyncingTaskRepository;

   @Autowired
   private StockConverter stockConverter;

   public List<Stock> findAll() {
      return stockConverter.convertListFrom(stockRepository.findAll());
   }

   public Stock findById(int id) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockRepository.findOne(id);
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
}