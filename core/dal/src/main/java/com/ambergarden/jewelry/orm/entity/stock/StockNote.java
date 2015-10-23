package com.ambergarden.jewelry.orm.entity.stock;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ambergarden.jewelry.orm.entity.AbstractEntity;

@Entity
public class StockNote extends AbstractEntity {
   private Date addTime;

   private String title;

   private String content;

   @Enumerated(EnumType.STRING)
   private NoteCategory noteCategory;

   public Date getAddTime() {
      return addTime;
   }

   public void setAddTime(Date addTime) {
      this.addTime = addTime;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public NoteCategory getNoteCategory() {
      return noteCategory;
   }

   public void setNoteCategory(NoteCategory noteCategory) {
      this.noteCategory = noteCategory;
   }
}