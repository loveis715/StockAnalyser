package com.ambergarden.jewelry.orm.repository.stock;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ambergarden.jewelry.orm.entity.stock.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/context/dal-test-context.xml" })
public class StockRepositoryTest {
   private static final String stockName = "Test_Name";
   private static final String stockCode = "Test_Code";

   @Autowired
   private StockRepository stockRepository;

   @Test
   public void testCRUD() {
      long count = stockRepository.count();

      Stock stock = new Stock();
      stock.setName(stockName);
      stock.setCode(stockCode);
      Stock savedStock = stockRepository.save(stock);
      Assert.assertEquals(stock.getName(), savedStock.getName());
      Assert.assertEquals(stock.getCode(), savedStock.getCode());
      Assert.assertEquals(count + 1, stockRepository.count());

      Stock retrievedStock = stockRepository.findOne(savedStock.getId());
      Assert.assertNotNull(retrievedStock);
   }
}