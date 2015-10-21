package com.ambergarden.jewelry.sina;

public class Constants {
   public static final String SHANGHAI_STOCK_LIST_URL_FORMAT
      = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=%d&num=500&node=sh_a";
   public static final String SHENZHEN_STOCK_LIST_URL_FORMAT
      = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=%d&num=500&node=sz_a";
   public static final String DAILY_TRADING_INFO_URL_FORMAT
      = "http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=%s&scale=240&ma=no&datalen=20";
   public static final String PER_HOUR_TRADING_INFO_URL_FORMAT
      = "http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=%s&scale=60&ma=no&datalen=80";
   public static final String PER_MINUTE_TRADING_INFO_URL_FORMAT
      = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vML_DataList.php?asc=j&symbol=%s&num=250";
   public static final String REALTIME_TRADING_URL_FORMAT
      = "http://hq.sinajs.cn/?list=%s";
   public static final String BILLING_INFO_URL_FORMAT
      = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_Bill.GetBillList?symbol=%s&num=400&page=%d&sort=ticktime&asc=0&amount=%d&type=0&day=%s";
}