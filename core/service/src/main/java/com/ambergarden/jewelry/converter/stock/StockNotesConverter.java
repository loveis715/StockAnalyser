package com.ambergarden.jewelry.converter.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.orm.entity.stock.StockNote;

@Component
public class StockNotesConverter
   extends AbstractEntityListConverter<StockNote, com.ambergarden.jewelry.schema.beans.stock.StockNote> {

   @Autowired
   private NoteCategoryConverter noteCategoryConverter;

   @Override
   public com.ambergarden.jewelry.schema.beans.stock.StockNote convertFrom(
         StockNote mo) {
      com.ambergarden.jewelry.schema.beans.stock.StockNote stockNote
         = new com.ambergarden.jewelry.schema.beans.stock.StockNote();
      stockNote.setId(mo.getId());
      stockNote.setTitle(mo.getTitle());
      stockNote.setAddTime(mo.getAddTime());
      stockNote.setNoteCategory(noteCategoryConverter.convertFrom(mo.getNoteCategory()));
      stockNote.setContent(mo.getContent());
      return stockNote;
   }

   @Override
   public StockNote convertTo(
         com.ambergarden.jewelry.schema.beans.stock.StockNote dto) {
      StockNote stockNote = new StockNote();
      stockNote.setId(dto.getId());
      stockNote.setTitle(dto.getTitle());
      stockNote.setAddTime(dto.getAddTime());
      stockNote.setNoteCategory(noteCategoryConverter.convertTo(dto.getNoteCategory()));
      stockNote.setContent(dto.getContent());
      return stockNote;
   }
}