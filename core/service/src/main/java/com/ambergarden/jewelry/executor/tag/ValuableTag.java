package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_SEGMENT_SEPARATOR;

public class ValuableTag extends Tag {
   private final double value;

   public ValuableTag(String tagName, TagCategory tagCategory, double value) {
      super(tagName, tagCategory);

      this.value = value;
   }

   public double getValue() {
      return value;
   }

   @Override
   public String toString() {
      return super.toString() + TAG_SEGMENT_SEPARATOR + String.valueOf(value);
   }

   public static Tag valueOf(String tagExpression) {
      String[] tagSegments = tagExpression.split(TAG_SEGMENT_SEPARATOR);
      if (tagSegments.length == 2) {
         return new Tag(tagSegments[0], TagCategory.parse(tagSegments[1]));
      } else if (tagSegments.length == 3) {
         return new ValuableTag(tagSegments[0], TagCategory.parse(tagSegments[1]),
               Double.parseDouble(tagSegments[2]));
      } else {
         // TODO: When logging system has been done, add a log here
         return null;
      }
   }
}