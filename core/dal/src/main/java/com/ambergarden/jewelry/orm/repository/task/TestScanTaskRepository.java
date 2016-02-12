package com.ambergarden.jewelry.orm.repository.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.task.TestScanTask;

@Repository
public interface TestScanTaskRepository extends CrudRepository<TestScanTask, Integer> {

   TestScanTask findFirstByOrderByStartTimeDesc();
}