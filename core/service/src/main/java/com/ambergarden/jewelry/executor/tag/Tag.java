package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_SEGMENT_SEPARATOR;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Tag {
   private final double value;

   private final String tagName;

   private final TagCategory tagCategory;

   public Tag(String tagName, TagCategory tagCategory, double value) {
      this.value = value;
      this.tagName = tagName;
      this.tagCategory = tagCategory;
   }

   public double getValue() {
      return value;
   }

   public String getTagName() {
      return tagName;
   }

   public TagCategory getTagCategory() {
      return tagCategory;
   }

   @Override
   public String toString() {
      DecimalFormat format = new DecimalFormat("##.##");
      format.setRoundingMode(RoundingMode.HALF_UP);
      return tagName + TAG_SEGMENT_SEPARATOR + tagCategory.toString()
            + TAG_SEGMENT_SEPARATOR + format.format(value);
   }

   public static Tag valueOf(String tagExpression) {
      String[] tagSegments = tagExpression.split(TAG_SEGMENT_SEPARATOR);
      if (tagSegments.length == 3) {
         return new Tag(tagSegments[0], TagCategory.parse(tagSegments[1]),
               Double.parseDouble(tagSegments[2]));
      } else {
         // TODO: When logging system has been done, add a log here
         return null;
      }
   }
}