package com.ambergarden.jewelry.converter.task;

import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.TaskState;

@Component
public class TaskStateConverter {
   public TaskState convertFrom(com.ambergarden.jewelry.orm.entity.task.TaskState state) {
      switch (state) {
      case SCHEDULED: return TaskState.SCHEDULED;
      case IN_PROGRESS: return TaskState.IN_PROGRESS;
      case SUCCESS: return TaskState.SUCCESS;
      case FAILED: return TaskState.FAILED;
      case CANCELED: return TaskState.CANCELED;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }

   public com.ambergarden.jewelry.orm.entity.task.TaskState convertTo(TaskState state) {
      switch (state) {
      case SCHEDULED: return com.ambergarden.jewelry.orm.entity.task.TaskState.SCHEDULED;
      case IN_PROGRESS: return com.ambergarden.jewelry.orm.entity.task.TaskState.IN_PROGRESS;
      case SUCCESS: return com.ambergarden.jewelry.orm.entity.task.TaskState.SUCCESS;
      case FAILED: return com.ambergarden.jewelry.orm.entity.task.TaskState.FAILED;
      case CANCELED: return com.ambergarden.jewelry.orm.entity.task.TaskState.CANCELED;
      default: throw new UnsupportedOperationException("Invalid TaskState value.");
      }
   }
}