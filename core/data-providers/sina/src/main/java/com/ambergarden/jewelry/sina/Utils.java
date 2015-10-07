package com.ambergarden.jewelry.sina;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

public class Utils {
   public static final int REQUEST_TIME_OUT = 20000;

   public static synchronized HttpClient getHttpClient() {
      RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(REQUEST_TIME_OUT)
            .setConnectTimeout(REQUEST_TIME_OUT)
            .setConnectionRequestTimeout(REQUEST_TIME_OUT)
            .build();

      return HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .build();
   }
}