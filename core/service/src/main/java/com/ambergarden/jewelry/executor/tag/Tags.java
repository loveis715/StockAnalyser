package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_CONTINUOUS_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_DECREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_DECREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_HIGH_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_LOW_PRICE;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_VOLUME_INCREMENT_WITH_PRICE_DOWN;

public class Tags {
   /**
    * Volume increases with normal price change.
    */
   public static class VolumeIncrementTag extends ValuableTag {
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
   public static class VolumeIncrementWithLowPrice extends ValuableTag {
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
   public static class VolumeDecrementWithLowPrice extends ValuableTag {
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
   public static class VolumeIncrementWithHighPrice extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE) == 0;
      }

      public VolumeIncrementWithHighPrice() {
         super(TAG_NAME_VOLUME_INCREMENT_HIGH_PRICE, TagCategory.NEGATIVE, TAG_VALUE_VOLUME_INCREMENT_HIGH_PRICE);
      }
   }

   public static class VolumeIncrementWithPriceDownTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN) == 0;
      }

      public VolumeIncrementWithPriceDownTag() {
         super(TAG_NAME_VOLUME_INCREMENT_WITH_PRICE_DOWN, TagCategory.POSITIVE, TAG_VALUE_VOLUME_INCREMENT_WITH_PRICE_DOWN);
      }
   }

   public static class ContinousVolumeIncrementTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_CONTINUOUS_VOLUME_INCREMENT) == 0;
      }

      public ContinousVolumeIncrementTag(double value) {
         super(TAG_NAME_CONTINUOUS_VOLUME_INCREMENT, TagCategory.POSITIVE, value);
      }
   }

   public static class LowPriceTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_LOW_PRICE) == 0;
      }

      public LowPriceTag(double value) {
         super(TAG_NAME_LOW_PRICE, TagCategory.POSITIVE, value);
      }
   }

   public static class HighPriceTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_HIGH_PRICE) == 0;
      }

      public HighPriceTag(double value) {
         super(TAG_NAME_HIGH_PRICE, TagCategory.NEGATIVE, value);
      }
   }
}