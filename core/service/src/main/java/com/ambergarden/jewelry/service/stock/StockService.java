package com.ambergarden.jewelry.service.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.orm.repository.stock.StockRepository;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Service
public class StockService {
   @Autowired
   private StockRepository stockRepository;

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

   public Stock save(Stock stock) {
      com.ambergarden.jewelry.orm.entity.stock.Stock stockMO
         = stockConverter.convertTo(stock);
      stockMO = stockRepository.save(stockMO);
      return stockConverter.convertFrom(stockMO);
   }
}