package com.ambergarden.jewelry.test.sina.provider;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.sina.provider.StockTradingInfoProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/com/ambergarden/jewelry/test/sina/provider-test-context.xml" })
public class StockTradingInfoProviderTest {
   @Autowired
   private StockTradingInfoProvider tradingInfoProvider;

   @Test
   public void testRetrieveTradingInfo() {
      tradingInfoProvider.getDailyTraidingInfo("sz300104");
   }

   @Test
   public void testRetrieveMinuteData() {
      List<MinuteData> minuteDataList = tradingInfoProvider.getPerMinuteTradingInfo("sz300104");
      Assert.assertEquals(240, minuteDataList.size());
   }
}