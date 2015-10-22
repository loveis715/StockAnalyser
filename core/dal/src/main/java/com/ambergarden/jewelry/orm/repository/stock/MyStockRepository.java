package com.ambergarden.jewelry.orm.repository.stock;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.stock.MyStock;

@Repository
public interface MyStockRepository extends CrudRepository<MyStock, Integer> {
}