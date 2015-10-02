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
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.sina.Constants;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public class StockTradingInfoProvider {
   public List<TradingInfo> getDailyTraidingInfo(String code) {
      String url = String.format(Constants.DAILY_TRADING_INFO_URL_FORMAT, code);
      HttpClient client = HttpClientBuilder.create().build();

      HttpResponse response = null;
      HttpGet httpGet = null;
      try {
         httpGet = new HttpGet(url);
         response = client.execute(httpGet);
      } catch (IOException e) {
         // TODO: Throw an exception when we have built the exception system
      }

      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
         // TODO: Throw an exception to indicate that we've failed to retrieve stocks
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

      httpGet.releaseConnection();

      List<TradingInfo> result = new ArrayList<TradingInfo>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, TradingInfo.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         result = mapper.readValue(responseText.toString(), arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }
      return result;
   }
}