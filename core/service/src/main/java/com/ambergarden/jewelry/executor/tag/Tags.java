package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_CONTINUOUS_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_LARGE_BUY_BILL;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_LARGE_SELL_BILL;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_MASS_NEG_TRADING;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_MASS_POS_TRADING;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_OVER_UPPER_BOUND;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_RECENT_BOTTOM_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_RECENT_TOP_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_TRADING_RATIO_LOW;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_DECREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_TRADING_RATIO_LOW;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_DECREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_WITH_PRICE_DOWN;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_FIND_MA5_SUPPORT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_FIND_MA10_SUPPORT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_FIND_MA20_SUPPORT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_FIND_MA30_SUPPORT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_CONFIRM_MA120_SUPPORT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_CONFIRM_MA250_SUPPORT;

public class Tags {
   /**
    * Volume increases with normal price change.
    */
   public static class VolumeIncrementTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT) == 0;
      }

      public VolumeIncrementTag(double value) {
         super(TAG_NAME_VOLUME_INCREMENT, TagCategory.POSITIVE, value);
      }
   }

   /**
    * Volume increases and we have reached the bottom price
    */
   public static class VolumeIncrementWithLowPrice extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT_LOW_PRICE) == 0;
      }

      public VolumeIncrementWithLowPrice() {
         super(TAG_NAME_VOLUME_INCREMENT_LOW_PRICE, TagCategory.POSITIVE, TAG_VALUE_VOLUME_INCREMENT_LOW_PRICE);
      }
   }

   /**
    * Volume decreases and the price has dropped down rapidly
    */
   public static class VolumeDecrementWithLowPrice extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_DECREMENT_LOW_PRICE) == 0;
      }

      public VolumeDecrementWithLowPrice() {
         super(TAG_NAME_VOLUME_DECREMENT_LOW_PRICE, TagCategory.NEGATIVE, TAG_VALUE_VOLUME_DECREMENT_LOW_PRICE);
      }
   }

   /**
    * Volume increases and we have reached the top price
    */
   public static class VolumeIncrementWithHighPrice extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE) == 0;
      }

      public VolumeIncrementWithHighPrice() {
         super(TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE, TagCategory.NEGATIVE, TAG_VALUE_VOLUME_INCREMENT_HIGH_PRICE);
      }
   }

   /**
    * Volume increases and the price has gone down.
    */
   public static class VolumeIncrementWithPriceDownTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN) == 0;
      }

      public VolumeIncrementWithPriceDownTag() {
         super(TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN, TagCategory.NEUTRUAL, TAG_VALUE_VOLUME_INCREMENT_WITH_PRICE_DOWN);
      }
   }

   /**
    * Volume continuously increases
    */
   public static class ContinousVolumeIncrementTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_CONTINUOUS_VOLUME_INCREMENT) == 0;
      }

      public ContinousVolumeIncrementTag(double value) {
         super(TAG_NAME_CONTINUOUS_VOLUME_INCREMENT, TagCategory.POSITIVE, value);
      }
   }

   /**
    * The stock is in a relatively low price today
    */
   public static class LowPriceTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_LOW_PRICE) == 0;
      }

      public LowPriceTag(double value) {
         super(TAG_NAME_LOW_PRICE, TagCategory.POSITIVE, value);
      }
   }

   /**
    * The stock is in a relatively high price today
    */
   public static class HighPriceTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_HIGH_PRICE) == 0;
      }

      public HighPriceTag(double value) {
         super(TAG_NAME_HIGH_PRICE, TagCategory.NEGATIVE, value);
      }
   }

   /**
    * The stock has massive buy in in several minutes
    */
   public static class MassPositiveTradingTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_MASS_POS_TRADING) == 0;
      }

      public MassPositiveTradingTag(double value) {
         super(TAG_NAME_MASS_POS_TRADING, TagCategory.POSITIVE, value);
      }
   }

   /**
    * The stock has massive sell out in several minutes
    */
   public static class MassNegativeTradingTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_MASS_NEG_TRADING) == 0;
      }

      public MassNegativeTradingTag(double value) {
         super(TAG_NAME_MASS_NEG_TRADING, TagCategory.NEGATIVE, value);
      }
   }

   /**
    * Trade ratio is a little small
    */
   public static class TradingRatioLowTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_TRADING_RATIO_LOW) == 0;
      }

      public TradingRatioLowTag() {
         super(TAG_NAME_TRADING_RATIO_LOW, TagCategory.NEGATIVE, TAG_VALUE_TRADING_RATIO_LOW);
      }
   }

   /**
    * Tag for frequent large buy in
    */
   public static class LargeBuyBillTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_LARGE_BUY_BILL) == 0;
      }

      public LargeBuyBillTag(double value) {
         super(TAG_NAME_LARGE_BUY_BILL, TagCategory.POSITIVE, value);
      }
   }

   /**
    * Tag for frequent large sell out
    */
   public static class LargeSellBillTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_LARGE_SELL_BILL) == 0;
      }

      public LargeSellBillTag(double value) {
         super(TAG_NAME_LARGE_SELL_BILL, TagCategory.NEGATIVE, value);
      }
   }

   /**
    * Tag for recent top price
    */
   public static class RecentTopPriceTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_RECENT_TOP_PRICE) == 0;
      }

      public RecentTopPriceTag() {
         super(TAG_NAME_RECENT_TOP_PRICE, TagCategory.POSITIVE, 2.0);
      }
   }

   public static class PriceOverUpperBoundaryTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_OVER_UPPER_BOUND) == 0;
      }

      public PriceOverUpperBoundaryTag() {
         super(TAG_NAME_OVER_UPPER_BOUND, TagCategory.POSITIVE, 3.0);
      }
   }

   /**
    * Tag for recent bottom price
    */
   public static class RecentBottomPriceTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_RECENT_BOTTOM_PRICE) == 0;
      }

      public RecentBottomPriceTag() {
         super(TAG_NAME_RECENT_BOTTOM_PRICE, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Find MA5's support
    */
   public static class FindMA5SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_FIND_MA5_SUPPORT) == 0;
      }

      public FindMA5SupportTag() {
         super(TAG_NAME_FIND_MA5_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Find MA10's support
    */
   public static class FindMA10SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_FIND_MA10_SUPPORT) == 0;
      }

      public FindMA10SupportTag() {
         super(TAG_NAME_FIND_MA10_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Find MA20's support
    */
   public static class FindMA20SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_FIND_MA20_SUPPORT) == 0;
      }

      public FindMA20SupportTag() {
         super(TAG_NAME_FIND_MA20_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Find MA30's support
    */
   public static class FindMA30SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_FIND_MA30_SUPPORT) == 0;
      }

      public FindMA30SupportTag() {
         super(TAG_NAME_FIND_MA30_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Confirm MA120's support
    */
   public static class ConfirmMA120SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_CONFIRM_MA120_SUPPORT) == 0;
      }

      public ConfirmMA120SupportTag() {
         super(TAG_NAME_CONFIRM_MA120_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }

   /**
    * Confirm MA250's support
    */
   public static class ConfirmMA250SupportTag extends Tag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_CONFIRM_MA250_SUPPORT) == 0;
      }

      public ConfirmMA250SupportTag() {
         super(TAG_NAME_CONFIRM_MA250_SUPPORT, TagCategory.POSITIVE, 1.0);
      }
   }
}