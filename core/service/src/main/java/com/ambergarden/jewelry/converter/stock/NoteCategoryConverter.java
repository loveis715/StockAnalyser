package com.ambergarden.jewelry.converter.stock;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.stock.NoteCategory;
import com.ambergarden.jewelry.schema.beans.stock.StockCategory;

@Component
public class NoteCategoryConverter {
   public NoteCategory convertFrom(com.ambergarden.jewelry.orm.entity.stock.NoteCategory category) {
      switch (category) {
      case POSITIVE: return NoteCategory.POSITIVE;
      case NEUTRUAL: return NoteCategory.NEUTRUAL;
      case NEGATIVE: return NoteCategory.NEGATIVE;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }

   public com.ambergarden.jewelry.orm.entity.stock.NoteCategory convertTo(NoteCategory category) {
      switch (category) {
      case POSITIVE: return com.ambergarden.jewelry.orm.entity.stock.NoteCategory.POSITIVE;
      case NEUTRUAL: return com.ambergarden.jewelry.orm.entity.stock.NoteCategory.NEUTRUAL;
      case NEGATIVE: return com.ambergarden.jewelry.orm.entity.stock.NoteCategory.NEGATIVE;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }
}