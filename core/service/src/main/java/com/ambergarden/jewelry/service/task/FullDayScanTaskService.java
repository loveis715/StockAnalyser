package com.ambergarden.jewelry.service.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.FullDayScanTaskConverter;
import com.ambergarden.jewelry.executor.FullDayScanTaskExecutor;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.FullDayScanTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;

@Service
public class FullDayScanTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private FullDayScanTaskExecutor fullDayScanTaskExecutor;

   @Autowired
   private FullDayScanTaskRepository fullDayScanTaskRepository;

   @Autowired
   private FullDayScanTaskConverter fullDayScanTaskConverter;

   public FullDayScanTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask fullDayScanTask
         = fullDayScanTaskRepository.findOne(id);
      // TODO: Check with NULL
      return fullDayScanTaskConverter.convertFrom(fullDayScanTask);
   }

   public FullDayScanTask findLast() {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask fullDayScanTask
         = fullDayScanTaskRepository.findFirstByOrderByStartTimeDesc();
      if (fullDayScanTask != null) {
         return fullDayScanTaskConverter.convertFrom(fullDayScanTask);
      } else {
         return null;
      }
   }

   public FullDayScanTask create() {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask fullDayScanTask
         = new com.ambergarden.jewelry.orm.entity.task.FullDayScanTask();
      fullDayScanTask.setStartTime(new Date());
      fullDayScanTask.setPercentage(0);
      fullDayScanTask.setTaskState(TaskState.SCHEDULED);

      fullDayScanTask = fullDayScanTaskRepository.save(fullDayScanTask);
      taskExecutor.execute(fullDayScanTaskExecutor);

      return fullDayScanTaskConverter.convertFrom(fullDayScanTask);
   }

   public FullDayScanTask update(FullDayScanTask task) {
      com.ambergarden.jewelry.orm.entity.task.FullDayScanTask fullDayScanTask
         = fullDayScanTaskConverter.convertTo(task);
      fullDayScanTask = fullDayScanTaskRepository.save(fullDayScanTask);
      return fullDayScanTaskConverter.convertFrom(fullDayScanTask);
   }
}