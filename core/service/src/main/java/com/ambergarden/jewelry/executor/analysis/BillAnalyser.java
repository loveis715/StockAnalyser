package com.ambergarden.jewelry.executor.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.executor.tag.Tag;
import com.ambergarden.jewelry.executor.tag.Tags;
import com.ambergarden.jewelry.schema.beans.provider.stock.BillInfo;
import com.ambergarden.jewelry.schema.beans.provider.stock.BillKind;
import com.ambergarden.jewelry.schema.beans.provider.stock.TradingInfo;
import com.ambergarden.jewelry.schema.beans.stock.Stock;

@Component
public class BillAnalyser {
   public List<Tag> analyse(List<BillInfo> billList, long amountBase, Stock stock,
         List<TradingInfo> tradingInfoList, List<TradingInfo> marketTradingInfo) {
      if (billList.size() == 0) {
         return new ArrayList<Tag>();
      }

      Collections.sort(billList, new Comparator<BillInfo>() {
         @Override
         public int compare(BillInfo info1, BillInfo info2) {
            return info1.getTicktime().compareTo(info2.getTicktime());
         }
      });

      List<Tag> result = new ArrayList<Tag>();
      result.addAll(analyseBuyIn(billList, amountBase));
      result.addAll(analyseSellOut(billList, amountBase));
      return result;
   }

   private List<Tag> analyseBuyIn(List<BillInfo> billList, long amountBase) {
      int counter = 0;
      String prevTimeString = "";
      long totalAmount = 0;
      long accumulatedAmount = 0;
      for (BillInfo billInfo : billList) {
         if (billInfo.getTicktime().compareTo("09:35:00") < 0) {
            continue;
         }
         if (billInfo.getKind() == BillKind.D) {
            continue;
         }

         if (!isRelativeBill(billInfo, prevTimeString, counter)) {
            if (totalAmount > amountBase * 5 && counter > 3) {
               accumulatedAmount += totalAmount;
            }
            totalAmount = 0;
            counter = 0;
         }

         if (billInfo.getKind() == BillKind.U) {
            counter++;
            totalAmount += billInfo.getVolume() * billInfo.getPrice();
         }

         // We're only counting for price up
         prevTimeString = billInfo.getTicktime();
      }

      List<Tag> result = new ArrayList<Tag>();
      if (accumulatedAmount > 0) {
         double tagValue = accumulatedAmount / amountBase / 10;
         result.add(new Tags.LargeBuyBillTag(tagValue));
      }
      return result;
   }

   private List<Tag> analyseSellOut(List<BillInfo> billList, long amountBase) {
      int negCounter = 0;
      String prevTimeString = "";
      long totalAmount = 0;
      long accumulatedNegAmount = 0;
      for (BillInfo billInfo : billList) {
         if (billInfo.getTicktime().compareTo("09:35:00") < 0) {
            continue;
         }
         if (billInfo.getKind() == BillKind.U) {
            continue;
         }

         if (!isRelativeBill(billInfo, prevTimeString, negCounter)) {
            if (totalAmount > amountBase * 5 && negCounter > 3) {
               accumulatedNegAmount += totalAmount;
            }
            totalAmount = 0;
            negCounter = 0;
         }

         if (billInfo.getKind() == BillKind.D) {
            negCounter++;
            totalAmount += billInfo.getVolume() * billInfo.getPrice();
         }

         // We're only counting for price up
         prevTimeString = billInfo.getTicktime();
      }

      List<Tag> result = new ArrayList<Tag>();
      if (accumulatedNegAmount > 0) {
         double tagValue = accumulatedNegAmount / amountBase / 10;
         result.add(new Tags.LargeSellBillTag(tagValue));
      }
      return result;
   }

   private boolean isRelativeBill(BillInfo billInfo, String prevTimeString, int counter) {
      if (prevTimeString == null || prevTimeString.length() == 0) {
         return false;
      }

      String[] segments = prevTimeString.split(":");
      int hour = Integer.parseInt(segments[0]);
      int minute = Integer.parseInt(segments[1]);
      minute += 1;
      if (minute >= 60) {
         minute -= 60;
         hour++;
      }

      String adjustedString = String.format("%02d:%02d:%s", hour, minute, segments[2]);
      return adjustedString.compareTo(billInfo.getTicktime()) > 0;
   }
}