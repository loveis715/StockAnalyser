package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_CONTINUOUS_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_DOWN_WITH_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_NAME_VOLUME_INCREMENT;
import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_VALUE_DOWN_WITH_VOLUME_INCREMENT;

public class Tags {
   public static class VolumeIncrementTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_VOLUME_INCREMENT) == 0;
      }

      public VolumeIncrementTag(double value) {
         super(TAG_NAME_VOLUME_INCREMENT, TagCategory.POSITIVE, value);
      }
   }

   public static class DownWithVolumeIncrementTag extends ValuableTag {
      public static boolean instanceOf(Tag tag) {
         return tag.getTagName().compareTo(TAG_NAME_DOWN_WITH_VOLUME_INCREMENT) == 0;
      }

      public DownWithVolumeIncrementTag() {
         super(TAG_NAME_DOWN_WITH_VOLUME_INCREMENT, TagCategory.POSITIVE, TAG_VALUE_DOWN_WITH_VOLUME_INCREMENT);
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
}