package com.ambergarden.jewelry.orm.repository.stock;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.stock.Stock;

/**
 * Repository to retrieve categories.
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
}