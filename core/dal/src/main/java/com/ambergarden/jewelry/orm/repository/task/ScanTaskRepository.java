package com.ambergarden.jewelry.orm.repository.task;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambergarden.jewelry.orm.entity.task.ScanTask;
import com.ambergarden.jewelry.orm.entity.task.ScanType;

@Repository
public interface ScanTaskRepository extends CrudRepository<ScanTask, Integer> {

   @Query("SELECT st FROM ScanTask st WHERE startTime = (SELECT MAX(startTime) FROM ScanTask WHERE scanType = ?1)")
   ScanTask findLastByScanType(ScanType scanType);
}