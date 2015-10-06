package com.ambergarden.jewelry.service.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.ScanTaskConverter;
import com.ambergarden.jewelry.executor.ScanTaskExecutor;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.ScanTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;

@Service
public class ScanTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private ScanTaskExecutor scanTaskExecutor;

   @Autowired
   private ScanTaskRepository scanTaskRepository;

   @Autowired
   private ScanTaskConverter scanTaskConverter;

   public List<ScanTask> list() {
      Iterable<com.ambergarden.jewelry.orm.entity.task.ScanTask> fullDayScanTasks
         = scanTaskRepository.findAll();
      return scanTaskConverter.convertListFrom(fullDayScanTasks);
   }

   public ScanTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskRepository.findOne(id);
      // TODO: Check with NULL
      return scanTaskConverter.convertFrom(scanTask);
   }

   public ScanTask findLast() {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskRepository.findFirstByOrderByStartTimeDesc();
      if (scanTask != null) {
         return scanTaskConverter.convertFrom(scanTask);
      } else {
         return null;
      }
   }

   public ScanTask create() {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = new com.ambergarden.jewelry.orm.entity.task.ScanTask();
      scanTask.setStartTime(new Date());
      scanTask.setPercentage(0);
      scanTask.setTaskState(TaskState.SCHEDULED);

      scanTask = scanTaskRepository.save(scanTask);
      taskExecutor.execute(scanTaskExecutor);

      return scanTaskConverter.convertFrom(scanTask);
   }

   public ScanTask update(ScanTask task) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskConverter.convertTo(task);
      scanTask = scanTaskRepository.save(scanTask);
      return scanTaskConverter.convertFrom(scanTask);
   }
}