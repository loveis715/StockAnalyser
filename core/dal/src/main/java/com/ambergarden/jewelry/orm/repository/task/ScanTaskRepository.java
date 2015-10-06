package com.ambergarden.jewelry.orm.repository.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.task.ScanTask;

@Repository
public interface ScanTaskRepository extends CrudRepository<ScanTask, Integer> {
   ScanTask findFirstByOrderByStartTimeDesc();
}