package com.ambergarden.jewelry.orm.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractVersionedEntity extends AbstractEntity {

   @Version
   protected int lockVersion;

   public int getLockVersion() {
      return lockVersion;
   }

   public void setLockVersion(int lockVersion) {
      this.lockVersion = lockVersion;
   }
}