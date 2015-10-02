package com.ambergarden.jewelry.test.sina.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/com/ambergarden/jewelry/test/sina/provider-test-context.xml" })
public class StockTradingInfoProviderTest {
   @Autowired
   private StockTradingInfoProvider tradingInfoProvider;

   @Test
   public void testListStocks() {
      tradingInfoProvider.getDailyTraidingInfo("sz300104");
   }
}