package com.ambergarden.jewelry.executor.tag;

public class TagValueMappings {
   public static final double[] VOLUME_TAG_LEVELS
      = new double[] {1.3, 1.46, 1.62, 1.77, 1.92, 2.06, 2.2, 2.33, 2.46, 2.58, 2.7, 2.81, 2.92, 3.02, 3.12, 3.21, 3.3, 3.38, 3.46, 3.53};
   public static final int VOLUME_TAG_MAX_SCORE = 20;

   public static final int[] MASS_VOLUME_LEVELS = new int[] {15, 20, 25, 30, 35, 40, 45, 50, 60, 70};

   public static final double[] TOTAL_VALUE_LEVELS = new double[] {1e9, 3e9, 1e10, 5e10, 1e11, 3e11, 1e12};
   public static final long[] LARGE_BILL_BASE = new long[] {300000, 500000, 750000, 1000000, 2000000, 3000000, 5000000, 10000000};
}