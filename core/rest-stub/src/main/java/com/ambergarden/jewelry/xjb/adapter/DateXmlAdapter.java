package com.ambergarden.jewelry.xjb.adapter;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings("restriction")
public class DateXmlAdapter extends XmlAdapter<String, Date> {
   @Override
   public String marshal(Date date) throws Exception {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      return DatatypeConverter.printDate(calendar);
   }

   @Override
   public Date unmarshal(String dateTime) throws Exception {
      return DatatypeConverter.parseDate(dateTime).getTime();
   }
}