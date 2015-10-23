package com.ambergarden.jewelry.service.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.stock.MyStockConverter;
import com.ambergarden.jewelry.orm.repository.stock.MyStockRepository;
import com.ambergarden.jewelry.schema.beans.stock.MyStock;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Service
public class MyStockService {
   @Autowired
   private MyStockRepository myStockRepository;

   @Autowired
   private MyStockConverter myStockConverter;

   public List<MyStock> findAll() {
      Iterable<com.ambergarden.jewelry.orm.entity.stock.MyStock> myStocks = myStockRepository.findAll();
      return myStockConverter.convertListFrom(myStocks);
   }

   public MyStock findById(int id) {
      com.ambergarden.jewelry.orm.entity.stock.MyStock myStock = myStockRepository.findOne(id);
      return myStockConverter.convertFrom(myStock);
   }

   public MyStock create(MyStock myStock) {
      com.ambergarden.jewelry.orm.entity.stock.MyStock myStockMO
         = myStockConverter.convertTo(myStock);
      myStockMO = myStockRepository.save(myStockMO);
      return myStockConverter.convertFrom(myStockMO);
   }

   public MyStock update(int myStockId, MyStock myStock) {
      com.ambergarden.jewelry.orm.entity.stock.MyStock myStockMO
         = myStockConverter.convertTo(myStock);
      myStockMO = myStockRepository.save(myStockMO);
      return myStockConverter.convertFrom(myStockMO);
   }

   public void delete(int myStockId) {
      myStockRepository.delete(myStockId);
   }
}