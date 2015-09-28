package com.ambergarden.jewelry.orm.repository.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.task.StockSyncingTask;

@Repository
public interface StockSyncingTaskRepository extends CrudRepository<StockSyncingTask, Integer> {
   StockSyncingTask findFirstByOrderByStartTimeDesc();
}