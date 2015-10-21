package com.ambergarden.jewelry.orm.repository.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask;

@Repository
public interface StockAnalyseTaskRepository extends CrudRepository<StockAnalyseTask, Integer> {
   StockAnalyseTask findFirstByOrderByStartTimeDesc();
}