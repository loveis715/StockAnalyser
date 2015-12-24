package com.ambergarden.jewelry.sina.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.provider.stock.BillInfo;
import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.RealtimeTrading;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.provider.stock.WeightInfo;
import com.ambergarden.jewelry.sina.Constants;
import com.ambergarden.jewelry.sina.Utils;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public class StockTradingInfoProvider {
   public List<RealtimeTrading> getRealtimeTradingInfo(String code) {
      String url = String.format(Constants.REALTIME_TRADING_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<RealtimeTrading>();
      }

      return RealtimeTradingAnalyser.parse(data);
   }

   public List<BillInfo> getBillingInfo(String code, long volume, String dateString) {
      List<BillInfo> result = new ArrayList<BillInfo>();
      int pageNum = 1;
      boolean complete = false;
      while (!complete) {
         String url = String.format(Constants.BILLING_INFO_URL_FORMAT,
               code, pageNum, volume, dateString);
         String billingString = retrieveData(url);
         List<BillInfo> billList = parseBillString(billingString);
         if (billList != null && billList.size() > 0) {
            result.addAll(billList);
         } else {
            complete = true;
         }
         pageNum++;
      }
      return result;
   }

   public List<MinuteData> getPerMinuteTradingInfo(String code) {
      String url = String.format(Constants.PER_MINUTE_TRADING_INFO_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<MinuteData>();
      }

      return PerMinuteTradingAnalyser.listMinuteData(data);
   }

   public List<TradingInfo> getHalfDayTradingInfo(String code) {
      String url = String.format(Constants.PER_HOUR_TRADING_INFO_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<TradingInfo>();
      }

      List<TradingInfo> tradingInfos = new ArrayList<TradingInfo>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, TradingInfo.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         tradingInfos = mapper.readValue(data, arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }

      List<TradingInfo> result = new ArrayList<TradingInfo>();
      Map<String, TradingInfo> tradingInfoMap = new HashMap<String, TradingInfo>();
      for (TradingInfo tradingInfo : tradingInfos) {
         String dayString = tradingInfo.getDay();
         String[] timeStrings = dayString.split(" ");
         if (timeStrings[1].compareTo("11:30:00") > 0) {
            continue;
         }

         if (tradingInfoMap.containsKey(timeStrings[0])) {
            TradingInfo mappedInfo = tradingInfoMap.get(timeStrings[0]);
            mappedInfo.setDay(timeStrings[0]);
            mappedInfo.setHigh(tradingInfo.getHigh() > mappedInfo.getHigh()
                  ? tradingInfo.getHigh() : mappedInfo.getHigh());
            mappedInfo.setLow(tradingInfo.getLow() > mappedInfo.getLow()
                  ? mappedInfo.getLow() : tradingInfo.getLow());
            if (tradingInfo.getDay().compareTo(mappedInfo.getDay()) > 0) {
               mappedInfo.setClose(tradingInfo.getClose());
            } else {
               mappedInfo.setOpen(tradingInfo.getOpen());
            }
            mappedInfo.setVolume(tradingInfo.getVolume() + mappedInfo.getVolume());

            result.add(mappedInfo);
         } else {
            tradingInfoMap.put(timeStrings[0], tradingInfo);
         }
      }
      return result;
   }

   public List<TradingInfo> getDailyTraidingInfo(String code) {
      String url = String.format(Constants.DAILY_TRADING_INFO_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<TradingInfo>();
      }

      List<TradingInfo> result = new ArrayList<TradingInfo>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, TradingInfo.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         result = mapper.readValue(data, arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }

      List<WeightInfo> weightInfos = retrieveWeightInfo(code);
      adjustTradingInfoWithWeights(result, weightInfos);
      return result;
   }

   public List<TradingInfo> getDailyTraidingInfoFor300Days(String code) {
      String url = String.format(Constants.DAILY_TRADING_INFO_300_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<TradingInfo>();
      }

      List<TradingInfo> result = new ArrayList<TradingInfo>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, TradingInfo.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         result = mapper.readValue(data, arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }

      List<WeightInfo> weightInfos = retrieveWeightInfo(code);
      adjustTradingInfoWithWeights(result, weightInfos);
      return result;
   }

   private void adjustTradingInfoWithWeights(List<TradingInfo> tradingInfos, List<WeightInfo> weightInfos) {
      Map<String, WeightInfo> weightMaps = new HashMap<String, WeightInfo>();
      for (WeightInfo weightInfo : weightInfos) {
         weightMaps.put(weightInfo.getDay(), weightInfo);
      }

      double basePrice = Double.MAX_VALUE;
      for (int index = tradingInfos.size() - 1; index > 0; index--) {
         TradingInfo tradingInfo = tradingInfos.get(index);
         String dayString = tradingInfo.getDay();
         if (weightMaps.containsKey(dayString)) {
            WeightInfo weight = weightMaps.get(dayString);

            double closePrice = tradingInfo.getClose();
            basePrice = closePrice / weight.getWeight();
            break;
         }
      }

      if (basePrice == Double.MAX_VALUE) {
         return;
      }

      for (int index = tradingInfos.size() - 1; index > 0; index--) {
         TradingInfo tradingInfo = tradingInfos.get(index);
         String dayString = tradingInfo.getDay();
         if (weightMaps.containsKey(dayString)) {
            WeightInfo weight = weightMaps.get(dayString);
            double calculatedPrice = weight.getWeight() * basePrice;
            if (calculatedPrice * 0.99 < tradingInfo.getClose()
               && calculatedPrice * 1.01 > tradingInfo.getClose()) {
               continue;
            }

            double ratio = calculatedPrice / tradingInfo.getClose();
            tradingInfo.setClose(calculatedPrice);
            tradingInfo.setHigh(tradingInfo.getHigh() * ratio);
            tradingInfo.setLow(tradingInfo.getLow() * ratio);
            tradingInfo.setOpen(tradingInfo.getOpen() * ratio);
            tradingInfo.setVolume((long)(tradingInfo.getVolume() / ratio));
         }
      }
   }

   private List<WeightInfo> retrieveWeightInfo(String code) {
      String url = String.format(Constants.WEIGHT_INFO_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<WeightInfo>();
      }

      return WeightInfoAnalyser.parse(data);
   }

   private List<BillInfo> parseBillString(String billingString) {
      List<BillInfo> result = new ArrayList<BillInfo>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, BillInfo.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         result = mapper.readValue(billingString, arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }
      return result;
   }

   private String retrieveData(String url) {
      HttpClient client = Utils.getHttpClient();
      HttpGet httpGet = null;
      int retryCount = 0;
      while (retryCount < 3) {
         try {
            httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            return readResponseBody(response);
         } catch (IOException e) {
            retryCount++;
         } finally {
            httpGet.releaseConnection();
         }
      }

      return "";
   }

   private String readResponseBody(HttpResponse response) throws IOException {
      if (response == null || response.getStatusLine() == null
            || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
         return "";
      }

      StringBuffer responseText = new StringBuffer();
      InputStream contentStream = response.getEntity().getContent();
      BufferedReader rd = new BufferedReader(
            new InputStreamReader(contentStream, "GBK"));

      String line = "";
      while ((line = rd.readLine()) != null) {
         responseText.append(line);
      }
      return responseText.toString();
   }
}