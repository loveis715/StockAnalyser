package com.ambergarden.jewelry.test.sina.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ambergarden.jewelry.schema.beans.stock.StockCategory;
import com.ambergarden.jewelry.sina.provider.StockListProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/com/ambergarden/jewelry/test/sina/provider-test-context.xml" })
public class StockListProviderTest {
   @Autowired
   private StockListProvider listProvider;

   @Test
   public void testListStocks() {
      listProvider.listAllStocks(StockCategory.SHANGHAI);
      listProvider.listAllStocks(StockCategory.SHENZHEN);
   }
}