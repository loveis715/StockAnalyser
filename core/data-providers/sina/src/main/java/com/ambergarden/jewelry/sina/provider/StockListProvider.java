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

import com.ambergarden.jewelry.schema.beans.provider.stock.Stock;
import com.ambergarden.jewelry.schema.beans.provider.stock.StockCategory;
import com.ambergarden.jewelry.sina.Constants;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * Class used to retrieve all stocks by category
 */
@Component
public class StockListProvider {
   /**
    * List all stocks under a specific category
    * @return all stocks under a specific category
    */
   public List<Stock> listAllStocks(StockCategory category) {
      List<Stock> result = new ArrayList<Stock>();

      int pageNum = 1;
      boolean complete = false;
      while (!complete) {
         String stockListString = retrieveStocks(pageNum, category);
         List<Stock> stockList = parseToDTO(stockListString);
         if (stockList != null && stockList.size() > 0) {
            result.addAll(stockList);
         } else {
            complete = true;
         }
         pageNum++;
      }
      return result;
   }

   private String retrieveStocks(int pageNum, StockCategory category) {
      HttpClient client = HttpClientBuilder.create().build();
      String url = constructRequestURL(pageNum, category);
      HttpResponse response = null;
      try {
         response = client.execute(new HttpGet(url));
      } catch (IOException e) {
         // TODO: Throw an exception when we have built the exception system
      }

      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
         // TODO: Throw an exception to indicate that we've failed to retrieve stocks
      }

      StringBuffer result = new StringBuffer();
      try {
         InputStream contentStream = response.getEntity().getContent();
         BufferedReader rd = new BufferedReader(
               new InputStreamReader(contentStream, "GBK"));

         String line = "";
         while ((line = rd.readLine()) != null) {
            result.append(line);
         }
      } catch (IOException e) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }
      return result.toString();
   }

   private List<Stock> parseToDTO(String stockListString) {
      List<Stock> result = new ArrayList<Stock>();
      try {
         ObjectMapper mapper = new ObjectMapper();
         CollectionType arrayType = mapper.getTypeFactory().constructCollectionType(
               List.class, Stock.class);
         mapper.getFactory().configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         result = mapper.readValue(stockListString, arrayType);
      } catch (Exception ex) {
         // TODO: Throw an exception to indicate that we've failed to read the content
      }
      return result;
   }

   private String constructRequestURL(int pageNum, StockCategory category) {
      switch(category) {
      case SHANGHAI:
         return String.format(Constants.SHANGHAI_STOCK_LIST_URL_FORMAT, pageNum);
      case SHENZHEN:
         return String.format(Constants.SHENZHEN_STOCK_LIST_URL_FORMAT, pageNum);
      default:
         // TODO: Thrown an exception when we have built the exception system
         return "";
      }
   }
}