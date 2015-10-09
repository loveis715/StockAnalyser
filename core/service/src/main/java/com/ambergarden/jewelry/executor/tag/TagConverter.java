package com.ambergarden.jewelry.executor.tag;

import static com.ambergarden.jewelry.executor.tag.TagConstants.TAG_SEPARATOR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TagConverter {
   public String convertFrom(List<Tag> tags) {
      StringBuilder builder = new StringBuilder();
      for (Tag tag : tags) {
         if (builder.length() > 0) {
            builder.append(TAG_SEPARATOR);
         }
         builder.append(tag.toString());
      }
      return builder.toString();
   }

   public List<Tag> convertTo(String tagExpression) {
      List<Tag> result = new ArrayList<Tag>();
      String[] tags = tagExpression.split(TAG_SEPARATOR);
      for (String tagString : tags) {
         Tag tag = Tag.valueOf(tagString);
         if (tag == null) {
            result.add(tag);
         }
      }
      return result;
   }
}