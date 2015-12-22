package com.ambergarden.jewelry.executor.tag;


public class TagConstants {
   public static final String TAG_SEPARATOR = ";";
   public static final String TAG_SEGMENT_SEPARATOR = ":";

   // Tag types
   public static final String TAG_NAME_VOLUME_INCREMENT = "VOL_INC";
   public static final String TAG_NAME_CONTINUOUS_VOLUME_INCREMENT = "VOL_CON_INC";
   public static final String TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN = "VOL_INC_DOWN";
   public static final double TAG_VALUE_VOLUME_INCREMENT_WITH_PRICE_DOWN = 5;
   public static final String TAG_NAME_VOLUME_INCREMENT_LOW_PRICE = "VOL_INC_LOW";
   public static final double TAG_VALUE_VOLUME_INCREMENT_LOW_PRICE = 5;
   public static final String TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE = "VOL_INC_HIGH";
   public static final double TAG_VALUE_VOLUME_INCREMENT_HIGH_PRICE = 2.5;
   public static final String TAG_NAME_VOLUME_DECREMENT_LOW_PRICE = "VOL_DEC_LOW";
   public static final double TAG_VALUE_VOLUME_DECREMENT_LOW_PRICE = 2.5;
   public static final String TAG_NAME_LOW_PRICE = "PRI_LOW";
   public static final String TAG_NAME_HIGH_PRICE = "PRI_HIGH";
   public static final String TAG_NAME_MASS_POS_TRADING = "MASS_POS_TRAD";
   public static final String TAG_NAME_MASS_NEG_TRADING = "MASS_NEG_TRAD";
   public static final String TAG_NAME_TRADING_RATIO_LOW = "TRAD_RATIO_LOW";
   public static final double TAG_VALUE_TRADING_RATIO_LOW = 0.5;
   public static final String TAG_NAME_LARGE_BUY_BILL = "LARGE_BUY_BILL";
   public static final String TAG_NAME_LARGE_SELL_BILL = "LARGE_SELL_BILL";
   public static final String TAG_NAME_RECENT_TOP_PRICE = "RECENT_TOP_PRI";
   public static final String TAG_NAME_RECENT_BOTTOM_PRICE = "RECENT_BOTTOM_PRI";
   public static final String TAG_NAME_OVER_UPPER_BOUND = "OVER_UPPER_BOUND";
   public static final String TAG_NAME_FIND_MA5_SUPPORT = "FIND_MA5_SUPPORT";
   public static final String TAG_NAME_FIND_MA10_SUPPORT = "FIND_MA10_SUPPORT";
   public static final String TAG_NAME_FIND_MA20_SUPPORT = "FIND_MA20_SUPPORT";
   public static final String TAG_NAME_FIND_MA30_SUPPORT = "FIND_MA30_SUPPORT";
   public static final String TAG_NAME_CONFIRM_MA120_SUPPORT = "CONFIRM_MA120_SUPPORT";
   public static final String TAG_NAME_CONFIRM_MA250_SUPPORT = "CONFIRM_MA250_SUPPORT";
   public static final String TAG_NAME_BREAK_BOUNDARY = "BREAK_BOUNDARY";
}