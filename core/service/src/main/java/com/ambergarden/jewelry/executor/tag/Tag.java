package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_SEGMENT_SEPARATOR;

public class Tag {
   private final String tagName;

   private final TagCategory tagCategory;

   public Tag(String tagName, TagCategory tagCategory) {
      this.tagName = tagName;
      this.tagCategory = tagCategory;
   }

   public String getTagName() {
      return tagName;
   }

   public TagCategory getTagCategory() {
      return tagCategory;
   }

   @Override
   public String toString() {
      return tagName + TAG_SEGMENT_SEPARATOR + tagCategory.toString();
   }
}