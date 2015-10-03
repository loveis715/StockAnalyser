package com.ambergarden.jewelry.executor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;
import com.ambergarden.jewelry.schema.beans.task.TaskState;
import com.ambergarden.jewelry.service.task.FullDayScanTaskService;

@Component
public class FullDayScanTaskExecutor implements Runnable {

   @Autowired
   private FullDayScanTaskService fullDayScanTaskService;

   @Override
   public void run() {
      FullDayScanTask fullDayScanTask = fullDayScanTaskService.findLast();
      fullDayScanTask.setTaskState(TaskState.SUCCESS);
      fullDayScanTask.setEndTime(new Date());
      fullDayScanTaskService.update(fullDayScanTask);
   }
}