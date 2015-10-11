package com.ambergarden.jewelry.converter.stock;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.schema.beans.stock.MinuteData;

@Component
public class MinuteDataConverter
   extends AbstractEntityListConverter<com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData, MinuteData> {

   @Override
   public MinuteData convertFrom(
         com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData mo) {
      MinuteData minuteData = new MinuteData();
      minuteData.setPrice(mo.getPrice());
      minuteData.setTime(mo.getTime());
      minuteData.setVolume(mo.getVolume());
      return minuteData;
   }

   @Override
   public com.ambergarden.jewelry.schema.beans.provider.stock.MinuteData convertTo(
         MinuteData dto) {
      throw new UnsupportedOperationException("Should not convert and send data from service to provider");
   }
}