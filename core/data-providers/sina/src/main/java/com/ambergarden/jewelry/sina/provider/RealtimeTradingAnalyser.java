package com.ambergarden.jewelry.sina.provider;

import java.util.ArrayList;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.RealtimeTrading;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingType;

public class RealtimeTradingAnalyser {
   private static String ARRAY_EDGE = "\"";
   private static String SEPARATOR = ",";

   public static List<RealtimeTrading> parse(String data) {
      int startIndex = data.indexOf(ARRAY_EDGE);
      int endIndex = data.lastIndexOf(ARRAY_EDGE);
      if (startIndex + 1 > endIndex) {
         return new ArrayList<RealtimeTrading>();
      }
      data = data.substring(startIndex + 1, endIndex);

      List<RealtimeTrading> result = new ArrayList<RealtimeTrading>();
      String[] datas = data.split(SEPARATOR);
      if (datas.length <= 32) {
         return new ArrayList<RealtimeTrading>();
      }

      RealtimeTrading buy1 = new RealtimeTrading();
      buy1.setVolume(Long.parseLong(datas[10]));
      buy1.setPrice(Double.parseDouble(datas[11]));
      buy1.setTradingType(TradingType.BUY);
      result.add(buy1);

      RealtimeTrading buy2 = new RealtimeTrading();
      buy2.setVolume(Long.parseLong(datas[12]));
      buy2.setPrice(Double.parseDouble(datas[13]));
      buy2.setTradingType(TradingType.BUY);
      result.add(buy2);

      RealtimeTrading buy3 = new RealtimeTrading();
      buy3.setVolume(Long.parseLong(datas[14]));
      buy3.setPrice(Double.parseDouble(datas[15]));
      buy3.setTradingType(TradingType.BUY);
      result.add(buy3);

      RealtimeTrading buy4 = new RealtimeTrading();
      buy4.setVolume(Long.parseLong(datas[16]));
      buy4.setPrice(Double.parseDouble(datas[17]));
      buy4.setTradingType(TradingType.BUY);
      result.add(buy4);

      RealtimeTrading buy5 = new RealtimeTrading();
      buy5.setVolume(Long.parseLong(datas[18]));
      buy5.setPrice(Double.parseDouble(datas[19]));
      buy5.setTradingType(TradingType.BUY);
      result.add(buy5);

      RealtimeTrading sell1 = new RealtimeTrading();
      sell1.setVolume(Long.parseLong(datas[20]));
      sell1.setPrice(Double.parseDouble(datas[21]));
      sell1.setTradingType(TradingType.SELL);
      result.add(sell1);

      RealtimeTrading sell2 = new RealtimeTrading();
      sell2.setVolume(Long.parseLong(datas[22]));
      sell2.setPrice(Double.parseDouble(datas[23]));
      sell2.setTradingType(TradingType.SELL);
      result.add(sell2);

      RealtimeTrading sell3 = new RealtimeTrading();
      sell3.setVolume(Long.parseLong(datas[24]));
      sell3.setPrice(Double.parseDouble(datas[25]));
      sell3.setTradingType(TradingType.SELL);
      result.add(sell3);

      RealtimeTrading sell4 = new RealtimeTrading();
      sell4.setVolume(Long.parseLong(datas[26]));
      sell4.setPrice(Double.parseDouble(datas[27]));
      sell4.setTradingType(TradingType.SELL);
      result.add(sell4);

      RealtimeTrading sell5 = new RealtimeTrading();
      sell5.setVolume(Long.parseLong(datas[28]));
      sell5.setPrice(Double.parseDouble(datas[29]));
      sell5.setTradingType(TradingType.SELL);
      result.add(sell5);
      return result;
   }
}