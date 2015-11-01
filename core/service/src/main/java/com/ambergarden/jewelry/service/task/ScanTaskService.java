package com.ambergarden.jewelry.service.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.ScanTaskConverter;
import com.ambergarden.jewelry.converter.task.ScanTypeConverter;
import com.ambergarden.jewelry.executor.HalfDayScanTaskExecutor;
import com.ambergarden.jewelry.executor.MorphologyScanExecutor;
import com.ambergarden.jewelry.executor.ScanTaskExecutor;
import com.ambergarden.jewelry.executor.StockAnalysisExecutor;
import com.ambergarden.jewelry.orm.entity.task.ScanType;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.ScanTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.schema.beans.task.ScanTaskRequest;

@Service
public class ScanTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private ScanTaskExecutor scanTaskExecutor;

   @Autowired
   private HalfDayScanTaskExecutor halfDayScanTaskExecutor;

   @Autowired
   private MorphologyScanExecutor morphologyScanExecutor;

   @Autowired
   private StockAnalysisExecutor stockAnalysisExecutor;

   @Autowired
   private ScanTaskRepository scanTaskRepository;

   @Autowired
   private ScanTypeConverter scanTypeConverter;

   @Autowired
   private ScanTaskConverter scanTaskConverter;

   public List<ScanTask> list() {
      Iterable<com.ambergarden.jewelry.orm.entity.task.ScanTask> scanTasks
         = scanTaskRepository.findAll();
      return scanTaskConverter.convertListFrom(scanTasks);
   }

   public ScanTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskRepository.findOne(id);
      // TODO: Check with NULL
      return scanTaskConverter.convertFrom(scanTask);
   }

   public ScanTask findLast(com.ambergarden.jewelry.schema.beans.task.ScanType scanType) {
      ScanType type = scanTypeConverter.convertTo(scanType);
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskRepository.findLastByScanType(type);
      if (scanTask != null) {
         return scanTaskConverter.convertFrom(scanTask);
      } else {
         return null;
      }
   }

   public ScanTaskRequest create(ScanTaskRequest request) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = new com.ambergarden.jewelry.orm.entity.task.ScanTask();
      scanTask.setStartTime(new Date());
      scanTask.setPercentage(0);
      scanTask.setTaskState(TaskState.SCHEDULED);
      scanTask.setScanType(scanTypeConverter.convertTo(request.getScanType()));

      scanTask = scanTaskRepository.save(scanTask);
      switch (request.getScanType()) {
      case FULL_DAY:
         taskExecutor.execute(scanTaskExecutor);
         break;
      case HALF_DAY:
         taskExecutor.execute(halfDayScanTaskExecutor);
         break;
      case MORPHOLOGY:
         taskExecutor.execute(morphologyScanExecutor);
         break;
      case SINGLE_STOCK:
      case TRADING_ANALYSIS:
         break;
      }

      // We will not persist the request for now.
      request.setScanTaskId(scanTask.getId());
      return request;
   }

   public ScanTask update(ScanTask task) {
      com.ambergarden.jewelry.orm.entity.task.ScanTask scanTask
         = scanTaskConverter.convertTo(task);
      scanTask = scanTaskRepository.save(scanTask);
      return scanTaskConverter.convertFrom(scanTask);
   }
}