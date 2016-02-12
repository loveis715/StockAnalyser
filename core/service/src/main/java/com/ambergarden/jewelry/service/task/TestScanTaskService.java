package com.ambergarden.jewelry.service.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.task.TestScanTaskConverter;
import com.ambergarden.jewelry.executor.TestScanTaskExecutor;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.TestScanTaskRepository;
import com.ambergarden.jewelry.schema.beans.task.TestScanTask;
import com.ambergarden.jewelry.schema.beans.task.TestScanTaskRequest;

@Service
public class TestScanTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private TestScanTaskExecutor testScanTaskExecutor;

   @Autowired
   private TestScanTaskRepository testScanTaskRepository;

   @Autowired
   private TestScanTaskConverter testScanTaskConverter;

   public List<TestScanTask> list() {
      Iterable<com.ambergarden.jewelry.orm.entity.task.TestScanTask> testScanTasks
         = testScanTaskRepository.findAll();
      return testScanTaskConverter.convertListFrom(testScanTasks);
   }

   public TestScanTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.TestScanTask testScanTask
         = testScanTaskRepository.findOne(id);
      // TODO: Check with NULL
      return testScanTaskConverter.convertFrom(testScanTask);
   }

   public TestScanTask findLast() {
      com.ambergarden.jewelry.orm.entity.task.TestScanTask testScanTask
         = testScanTaskRepository.findFirstByOrderByStartTimeDesc();
      if (testScanTask != null) {
         return testScanTaskConverter.convertFrom(testScanTask);
      } else {
         return null;
      }
   }

   public TestScanTaskRequest create(TestScanTaskRequest request) {
      com.ambergarden.jewelry.orm.entity.task.TestScanTask testScanTask
         = new com.ambergarden.jewelry.orm.entity.task.TestScanTask();
      testScanTask.setStartTime(new Date());
      testScanTask.setPercentage(0);
      testScanTask.setTaskState(TaskState.SCHEDULED);

      testScanTask = testScanTaskRepository.save(testScanTask);
      taskExecutor.execute(testScanTaskExecutor);

      // We will not persist the request for now.
      request.setTestScanTaskId(testScanTask.getId());
      return request;
   }

   public TestScanTask update(TestScanTask testTask) {
      com.ambergarden.jewelry.orm.entity.task.TestScanTask testScanTask
         = testScanTaskConverter.convertTo(testTask);
      testScanTask = testScanTaskRepository.save(testScanTask);
      return testScanTaskConverter.convertFrom(testScanTask);
   }
}