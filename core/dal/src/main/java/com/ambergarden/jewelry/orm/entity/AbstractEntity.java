package com.ambergarden.jewelry.orm.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public abstract class AbstractEntity {

   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private int id = -1;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   @Override
   public int hashCode() {
      return new HashCodeBuilder()
         .append(this.getId())
         .append(this.getClass())
         .toHashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj == null) {
         return false;
      }

      if (getClass() != obj.getClass()) {
         return false;
      }

      AbstractVersionedEntity entity = (AbstractVersionedEntity) obj;
      return new EqualsBuilder()
         .append(this.getId(), entity.getId())
         .isEquals();
   }
}