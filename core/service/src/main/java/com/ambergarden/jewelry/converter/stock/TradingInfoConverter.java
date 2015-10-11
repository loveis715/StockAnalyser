package com.ambergarden.jewelry.converter.stock;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.schema.beans.stock.TradingInfo;

@Component
public class TradingInfoConverter
   extends AbstractEntityListConverter<com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo, TradingInfo> {

   @Override
   public TradingInfo convertFrom(
         com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo mo) {
      TradingInfo tradingInfo = new TradingInfo();
      tradingInfo.setDay(mo.getDay());
      tradingInfo.setOpen(mo.getOpen());
      tradingInfo.setClose(mo.getClose());
      tradingInfo.setHigh(mo.getHigh());
      tradingInfo.setLow(mo.getLow());
      tradingInfo.setVolume(mo.getVolume());
      return tradingInfo;
   }

   @Override
   public com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo convertTo(
         TradingInfo dto) {
      throw new UnsupportedOperationException("Should not convert and send data from service to provider");
   }
}