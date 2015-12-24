package com.ambergarden.jewelry.sina.provider;

import java.util.ArrayList;
import java.util.List;

import com.ambergarden.jewelry.schema.beans.provider.stock.WeightInfo;

public class WeightInfoAnalyser {
   private static String ARRAY_START = ":{";
   private static String ARRAY_END = "}}]";
   private static String SEPARATOR = ",";
   private static String VALUE_SEPARATOR = ":";
   private static String DATE_SEPARATOR = "_";
   private static String DATE_FORMATTER = "-";

   public static List<WeightInfo> parse(String data) {
      int startIndex = data.indexOf(ARRAY_START);
      int endIndex = data.lastIndexOf(ARRAY_END);
      if (startIndex + 2 > endIndex) {
         return new ArrayList<WeightInfo>();
      }
      data = data.substring(startIndex + 2, endIndex);

      List<WeightInfo> result = new ArrayList<WeightInfo>();
      String[] weightInfos = data.split(SEPARATOR);
      for (String weightInfo : weightInfos) {
         WeightInfo weight = parseWeightInfo(weightInfo);
         if (weight != null) {
            result.add(weight);
         }
      }
      return result;
   }

   private static WeightInfo parseWeightInfo(String weightInfo) {
      String[] dataSegs = weightInfo.split(VALUE_SEPARATOR);
      WeightInfo result = new WeightInfo();
      result.setDay(parseDate(dataSegs[0]));
      if (dataSegs[1].length() < 3) {
         return null;
      }
      result.setWeight(parseWeight(dataSegs[1]));
      return result;
   }

   private static String parseDate(String dateString) {
      dateString = dateString.replace(DATE_SEPARATOR, DATE_FORMATTER);
      return dateString.substring(1);
   }

   private static double parseWeight(String weightString) {
      weightString = weightString.substring(1, weightString.length() - 2);
      return Double.parseDouble(weightString);
   }
}