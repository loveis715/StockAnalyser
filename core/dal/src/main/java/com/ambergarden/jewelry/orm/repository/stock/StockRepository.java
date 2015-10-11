package com.ambergarden.jewelry.orm.repository.stock;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.stock.Stock;
import com.ambergarden.jewelry.orm.entity.stock.StockCategory;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
   int countByStockCategory(StockCategory stockCategory);

   Stock findByName(String name);
}