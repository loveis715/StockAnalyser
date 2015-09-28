package com.ambergarden.jewelry.orm.entity.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.ambergarden.jewelry.orm.entity.AbstractVersionedEntity;

@MappedSuperclass
public class AbstractTask extends AbstractVersionedEntity {

   private Date startTime;

   private Date endTime;

   @Column(name="percentage", columnDefinition="numeric(6, 4)")
   private double percentage;

   @Enumerated(EnumType.STRING)
   private TaskState taskState;

   public Date getStartTime() {
      return this.startTime;
   }

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
   }

   public Date getEndTime() {
      return this.endTime;
   }

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   public double getPercentage() {
      return this.percentage;
   }

   public void setPercentage(double percentage) {
      this.percentage = percentage;
   }

   public TaskState getTaskState() {
      return this.taskState;
   }

   public void setTaskState(TaskState taskState) {
      this.taskState = taskState;
   }
}