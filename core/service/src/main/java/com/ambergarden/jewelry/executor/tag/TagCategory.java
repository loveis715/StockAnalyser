package com.ambergarden.jewelry.executor.tag;

public enum TagCategory {
   NEGATIVE(-1),
   NEUTRUAL(0),
   POSITIVE(1);

   private int value;

   TagCategory(int value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return String.valueOf(value);
   }

   public static TagCategory parse(String value) {
      if (value.equalsIgnoreCase("-1")) {
         return NEGATIVE;
      } else if (value.equalsIgnoreCase("0")) {
         return NEUTRUAL;
      } else if (value.equalsIgnoreCase("1")) {
         return POSITIVE;
      } else {
         throw new IllegalArgumentException("Enum TagCategory could only be -1, 0 or 1");
      }
   }
}