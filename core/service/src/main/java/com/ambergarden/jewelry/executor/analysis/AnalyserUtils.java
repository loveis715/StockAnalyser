package com.ambergarden.jewelry.executor.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ambergarden.jewelry.executor.analysis.bean.PriceMAs;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;

public class AnalyserUtils {
   public static final int DAY_0 = 0;
   public static final int DAY_5 = 5;
   public static final int DAY_10 = 10;
   public static final int DAY_20 = 20;
   public static final int DAY_30 = 30;
   public static final int DAY_45 = 45;
   public static final int DAY_60 = 60;
   public static final int DAY_120 = 120;
   public static final int DAY_250 = 250;

   public static Map<String, PriceMAs> calculatePriceMAs(List<TradingInfo> tradingInfoList) {
      double total5 = getTotal(tradingInfoList, DAY_0, DAY_5);
      double total10 = total5 + getTotal(tradingInfoList, DAY_5, DAY_10);
      double total20 = total10 + getTotal(tradingInfoList, DAY_10, DAY_20);
      double total30 = total20 + getTotal(tradingInfoList, DAY_20, DAY_30);
      double total45 = total30 + getTotal(tradingInfoList, DAY_30, DAY_45);
      double total60 = total45 + getTotal(tradingInfoList, DAY_45, DAY_60);
      double total120 = total60 + getTotal(tradingInfoList, DAY_60, DAY_120);
      double total250 = total120 + getTotal(tradingInfoList, DAY_120, DAY_250);

      Map<String, PriceMAs> priceMAMap = new HashMap<String, PriceMAs>();
      for (int index = tradingInfoList.size() - 1; index > 0; index--) {
         PriceMAs priceMAs = new PriceMAs();
         if (index - DAY_5 >= -1) {
            priceMAs.setMA5(total5 / DAY_5);
         }
         if (index - DAY_10 >= -1) {
            priceMAs.setMA10(total10 / DAY_10);
         }
         if (index - DAY_20 >= -1) {
            priceMAs.setMA20(total20 / DAY_20);
         }
         if (index - DAY_30 >= -1) {
            priceMAs.setMA30(total30 / DAY_30);
         }
         if (index - DAY_45 >= -1) {
            priceMAs.setMA45(total45 / DAY_45);
         }
         if (index - DAY_60 >= -1) {
            priceMAs.setMA60(total60 / DAY_60);
         }
         if (index - DAY_120 >= -1) {
            priceMAs.setMA120(total120 / DAY_120);
         }
         if (index - DAY_250 >= -1) {
            priceMAs.setMA250(total250 / DAY_250);
         }

         TradingInfo tradingInfo = tradingInfoList.get(index);
         priceMAMap.put(tradingInfo.getDay(), priceMAs);

         if (index - DAY_5 >= 0) {
            total5 -= tradingInfo.getClose();

            TradingInfo tradingInfo5 = tradingInfoList.get(index - DAY_5);
            total5 += tradingInfo5.getClose();
         }

         if (index - DAY_10 >= 0) {
            total10 -= tradingInfo.getClose();

            TradingInfo tradingInfo10 = tradingInfoList.get(index - DAY_10);
            total10 += tradingInfo10.getClose();
         }

         if (index - DAY_20 >= 0) {
            total20 -= tradingInfo.getClose();

            TradingInfo tradingInfo20 = tradingInfoList.get(index - DAY_20);
            total20 += tradingInfo20.getClose();
         }

         if (index - DAY_30 >= 0) {
            total30 -= tradingInfo.getClose();

            TradingInfo tradingInfo30 = tradingInfoList.get(index - DAY_30);
            total30 += tradingInfo30.getClose();
         }

         if (index - DAY_45 >= 0) {
            total45 -= tradingInfo.getClose();

            TradingInfo tradingInfo45 = tradingInfoList.get(index - DAY_45);
            total45 += tradingInfo45.getClose();
         }

         if (index - DAY_60 >= 0) {
            total60 -= tradingInfo.getClose();

            TradingInfo tradingInfo60 = tradingInfoList.get(index - DAY_60);
            total60 += tradingInfo60.getClose();
         }

         if (index - DAY_120 >= 0) {
            total120 -= tradingInfo.getClose();

            TradingInfo tradingInfo120 = tradingInfoList.get(index - DAY_120);
            total120 += tradingInfo120.getClose();
         }

         if (index - DAY_250 >= 0) {
            total250 -= tradingInfo.getClose();

            TradingInfo tradingInfo250 = tradingInfoList.get(index - DAY_250);
            total250 += tradingInfo250.getClose();
         }
      }
      return priceMAMap;
   }

   private static double getTotal(List<TradingInfo> tradingInfoList, int startIndex, int counter) {
      double total = 0;
      for (int index = tradingInfoList.size() - startIndex - 1; index > 0 && counter > 0; index--) {
         TradingInfo tradingInfo = tradingInfoList.get(index);
         total += tradingInfo.getClose();
         counter--;
      }
      return total;
   }
}