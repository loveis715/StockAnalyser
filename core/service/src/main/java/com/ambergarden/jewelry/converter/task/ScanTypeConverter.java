package com.ambergarden.jewelry.converter.task;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.ScanType;

@Component
public class ScanTypeConverter {
   public ScanType convertFrom(com.ambergarden.jewelry.orm.entity.task.ScanType scanType) {
      switch (scanType) {
      case FULL_DAY: return ScanType.FULL_DAY;
      case HALF_DAY: return ScanType.HALF_DAY;
      case SINGLE_STOCK: return ScanType.SINGLE_STOCK;
      case TRADING_ANALYSIS: return ScanType.TRADING_ANALYSIS;
      case MORPHOLOGY: return ScanType.MORPHOLOGY;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }

   public com.ambergarden.jewelry.orm.entity.task.ScanType convertTo(ScanType scanType) {
      switch (scanType) {
      case FULL_DAY: return com.ambergarden.jewelry.orm.entity.task.ScanType.FULL_DAY;
      case HALF_DAY: return com.ambergarden.jewelry.orm.entity.task.ScanType.HALF_DAY;
      case SINGLE_STOCK: return com.ambergarden.jewelry.orm.entity.task.ScanType.SINGLE_STOCK;
      case TRADING_ANALYSIS: return com.ambergarden.jewelry.orm.entity.task.ScanType.TRADING_ANALYSIS;
      case MORPHOLOGY: return com.ambergarden.jewelry.orm.entity.task.ScanType.MORPHOLOGY;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }
}