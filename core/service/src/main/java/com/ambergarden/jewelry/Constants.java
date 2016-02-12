package com.ambergarden.jewelry;

public class Constants {
   // API URLs
   public final static String STOCK_URL = "/api/stocks";
   public final static String STOCK_SYNCING_TASKS_URL = "/api/stock_syncing_tasks";
   public final static String SCAN_TASKS_URL = "/api/scan_tasks";
   public final static String TEST_SCAN_TASKS_URL = "/api/test_scan_tasks";
   public final static String STOCK_ANALYSE_TASKS_URL = "/api/analyse_tasks";
   public final static String MY_STOCK_URL = "/api/my_stocks";

   // URL constants
   public final static String ID_PATH_VARIABLE = "id";
   public final static String ALIAS_PATH_VARIABLE = "alias";
   public final static String TYPE_VARIABLE = "type";
   public final static String ID_URL = "/{id}";
   public final static String FIND_BY_ID_URL = ID_URL;
   public final static String FIND_LAST_URL = "/last";
   public final static String FIND_LAST_BY_TYPE_URL = "/last/{" + TYPE_VARIABLE + "}";
   public final static String STATISTICS = "/statistics";
   // TODO: ExtJS only supports this kind of URL at this time. Need to change to /{alias}/tradings
   public final static String TRADINGS = "/{alias}/tradings";
   public final static String ALIAS = "/alias/{alias}";

   // Stock code prefix
   public final static String PREFIX_SH = "sh";
   public final static String PREFIX_SZ = "sz";

   // Special stock codes
   public final static String CODE_SH = "sh000001";
   public final static String CODE_SZ = "sz399001";
}