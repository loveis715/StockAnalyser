package com.ambergarden.jewelry.converter.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.ambergarden.jewelry.converter.base.AbstractEntityListConverter;
import com.ambergarden.jewelry.schema.beans.task.AbstractTask;

public abstract class AbstractTaskConverter<
   S extends com.ambergarden.jewelry.orm.entity.task.AbstractTask,
   T extends AbstractTask>
   extends AbstractEntityListConverter<S, T> {

   @Autowired
   private TaskStateConverter taskStateConverter;

   public void convertFrom(S mo, T dto) {
      dto.setId(mo.getId());
      dto.setLockVersion(mo.getLockVersion());
      dto.setStartTime(mo.getStartTime());
      dto.setEndTime(mo.getEndTime());
      dto.setPercentage(mo.getPercentage());
      dto.setTaskState(taskStateConverter.convertFrom(mo.getTaskState()));
   }

   public void convertTo(T dto, S mo) {
      mo.setId(dto.getId());
      mo.setLockVersion(dto.getLockVersion());
      mo.setStartTime(dto.getStartTime());
      mo.setEndTime(dto.getEndTime());
      mo.setPercentage(dto.getPercentage());
      mo.setTaskState(taskStateConverter.convertTo(dto.getTaskState()));
   }

}