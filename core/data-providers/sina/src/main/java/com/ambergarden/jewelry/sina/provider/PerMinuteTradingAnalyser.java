package com.ambergarden.jewelry.sina.provider;

import java.util.ArrayList;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;

public class PerMinuteTradingAnalyser {
   private static String ARRAY_START = "[";
   private static String ARRAY_END = "]";
   private static String SEPARATOR = "\'\\],\\[\'";
   private static String VALUE_SEPARATOR = "\', \'";

   public static List<MinuteData> listMinuteData(String data) {
      int startIndex = data.indexOf(ARRAY_START);
      int endIndex = data.lastIndexOf(ARRAY_END);
      if (startIndex + 5 > endIndex) {
         return new ArrayList<MinuteData>();
      }
      data = data.substring(startIndex + 3, endIndex - 2);

      List<MinuteData> result = new ArrayList<MinuteData>();
      String[] minuteDatas = data.split(SEPARATOR);
      for (String minuteData : minuteDatas) {
         result.add(parseMinuteData(minuteData));
      }
      return result;
   }

   private static MinuteData parseMinuteData(String minuteData) {
      String[] dataSegs = minuteData.split(VALUE_SEPARATOR);
      MinuteData result = new MinuteData();
      result.setTime(dataSegs[0]);
      result.setPrice(Double.parseDouble(dataSegs[1]));
      result.setVolume(Long.parseLong(dataSegs[2]));
      return result;
   }
}