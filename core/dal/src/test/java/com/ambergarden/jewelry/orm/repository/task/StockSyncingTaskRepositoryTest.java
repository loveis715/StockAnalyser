package com.ambergarden.jewelry.orm.repository.task;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ambergarden.jewelry.orm.entity.task.StockListingTask;
import com.ambergarden.jewelry.orm.entity.task.StockSyncingTask;
import com.ambergarden.jewelry.orm.entity.task.TaskState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/context/dal-test-context.xml" })
public class StockSyncingTaskRepositoryTest {
   @Autowired
   private StockSyncingTaskRepository syncingTaskRepository;

   @Test
   public void testGetLastStockSyncingTask() {
      syncingTaskRepository.deleteAll();

      StockSyncingTask syncingTask = syncingTaskRepository.findFirstByOrderByStartTimeDesc();
      Assert.assertNull(syncingTask);

      syncingTask = createStockSyncingTask();
      syncingTask = syncingTaskRepository.save(syncingTask);

      syncingTask = syncingTaskRepository.findFirstByOrderByStartTimeDesc();
      Assert.assertNotNull(syncingTask);
   }

   @Test
   public void testStockSyncingTaskExecution() {
      StockSyncingTask syncingTask = createStockSyncingTask();
      StockSyncingTask newSyncingTask = syncingTaskRepository.save(syncingTask);
      verifyPendingSyncingTask(syncingTask, newSyncingTask);

      syncingTask = newSyncingTask;
      updateStockSyncingTaskToHalf(syncingTask);
      newSyncingTask = syncingTaskRepository.save(syncingTask);
      verifyPendingSyncingTask(syncingTask, newSyncingTask);

      syncingTask = newSyncingTask;
      updateStockSyncingTaskToComplete(syncingTask);
      newSyncingTask = syncingTaskRepository.save(syncingTask);
      verifyCompletedSyncingTask(syncingTask, newSyncingTask);
   }

   private StockSyncingTask createStockSyncingTask() {
      StockListingTask listingTask = new StockListingTask();
      listingTask.setStartTime(new Date());
      listingTask.setPercentage(0);
      listingTask.setTaskState(TaskState.SCHEDULED);

      StockSyncingTask syncingTask = new StockSyncingTask();
      syncingTask.setStartTime(new Date());
      syncingTask.setPercentage(0);
      syncingTask.setTaskState(TaskState.SCHEDULED);
      syncingTask.setListingTask(listingTask);

      return syncingTask;
   }

   private void verifyPendingSyncingTask(StockSyncingTask syncingTask, StockSyncingTask newSyncingTask) {
      Assert.assertEquals(syncingTask.getTaskState(), newSyncingTask.getTaskState());
      Assert.assertEquals(syncingTask.getPercentage(), newSyncingTask.getPercentage());
      Assert.assertNotNull(newSyncingTask.getListingTask());
      Assert.assertNotNull(newSyncingTask.getStartTime());

      StockListingTask newListingTask = newSyncingTask.getListingTask();
      StockListingTask listingTask = syncingTask.getListingTask();
      Assert.assertEquals(listingTask.getTaskState(), newListingTask.getTaskState());
      Assert.assertEquals(listingTask.getPercentage(), newListingTask.getPercentage());
      Assert.assertNotNull(newListingTask.getStartTime());
   }

   private void verifyCompletedSyncingTask(StockSyncingTask syncingTask, StockSyncingTask newSyncingTask) {
      Assert.assertEquals(syncingTask.getTaskState(), newSyncingTask.getTaskState());
      Assert.assertEquals(syncingTask.getPercentage(), newSyncingTask.getPercentage());
      Assert.assertNotNull(newSyncingTask.getListingTask());
      Assert.assertNotNull(newSyncingTask.getStartTime());
      Assert.assertNotNull(newSyncingTask.getEndTime());

      StockListingTask newListingTask = newSyncingTask.getListingTask();
      StockListingTask listingTask = syncingTask.getListingTask();
      Assert.assertEquals(listingTask.getTaskState(), newListingTask.getTaskState());
      Assert.assertEquals(listingTask.getPercentage(), newListingTask.getPercentage());
      Assert.assertNotNull(newListingTask.getStartTime());
      Assert.assertNotNull(newListingTask.getEndTime());
   }

   private void updateStockSyncingTaskToHalf(StockSyncingTask syncingTask) {
      syncingTask.setPercentage(0.5);
      syncingTask.setTaskState(TaskState.IN_PROGRESS);

      StockListingTask listingTask = syncingTask.getListingTask();
      listingTask.setPercentage(0.5);
      listingTask.setTaskState(TaskState.IN_PROGRESS);
   }

   private void updateStockSyncingTaskToComplete(StockSyncingTask syncingTask) {
      syncingTask.setPercentage(1);
      syncingTask.setTaskState(TaskState.SUCCESS);
      syncingTask.setEndTime(new Date());

      StockListingTask listingTask = syncingTask.getListingTask();
      listingTask.setPercentage(1);
      listingTask.setTaskState(TaskState.SUCCESS);
      listingTask.setEndTime(new Date());
   }
}