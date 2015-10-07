package com.ambergarden.jewelry.sina.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.sina.Constants;
import com.ambergarden.jewelry.sina.Utils;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public class StockTradingInfoProvider {
   public List<MinuteData> getPerMinuteTradingInfo(String code) {
      String url = String.format(Constants.PER_MINUTE_TRADING_INFO_URL_FORMAT, code);
      String data = retrieveData(url);
      if (data == null || data.length() == 0) {
         return new ArrayList<MinuteData>();
      }

      return PerMinuteTradingAnalyser.listMinuteData(data);
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
      return result;
   }

   private String retrieveData(String url) {
      HttpClient client = Utils.getHttpClient();
      HttpResponse response = null;
      HttpGet httpGet = null;
      int retryCount = 0;
      while (response == null && retryCount < 3) {
         try {
            httpGet = new HttpGet(url);
            response = client.execute(httpGet);
         } catch (IOException e) {
            retryCount++;
         } finally {
            httpGet.releaseConnection();
         }
      }

      if (response == null || response.getStatusLine() == null
            || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
         return "";
      }

      StringBuffer responseText = new StringBuffer();
      try {
         InputStream contentStream = response.getEntity().getContent();
         BufferedReader rd = new BufferedReader(
               new InputStreamReader(contentStream, "GBK"));

         String line = "";
         while ((line = rd.readLine()) != null) {
            responseText.append(line);
         }
      } catch (IOException e) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }

      return responseText.toString();
   }
}