package com.ambergarden.jewelry.controller.task;

import static com.ambergarden.jewelry.Constants.FIND_BY_ID_URL;
import static com.ambergarden.jewelry.Constants.FIND_LAST_URL;
import static com.ambergarden.jewelry.Constants.FULL_DAY_SCAN_TASKS_URL;
import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ambergarden.jewelry.schema.beans.task.FullDayScanTask;
import com.ambergarden.jewelry.service.task.FullDayScanTaskService;

@Controller
@RequestMapping(value = FULL_DAY_SCAN_TASKS_URL)
public class FullDayScanTaskController {
   @Autowired
   private FullDayScanTaskService fullDayScanTaskService;

   @RequestMapping(method = RequestMethod.GET)
   public List<FullDayScanTask> list() {
      return fullDayScanTaskService.list();
   }

   @RequestMapping(value = FIND_BY_ID_URL, method = RequestMethod.GET)
   public FullDayScanTask findById(@PathVariable(ID_PATH_VARIABLE) int id) {
      return fullDayScanTaskService.findById(id);
   }

   @RequestMapping(method = RequestMethod.POST)
   public FullDayScanTask create() {
      return fullDayScanTaskService.create();
   }

   @RequestMapping(value = FIND_LAST_URL, method = RequestMethod.GET)
   public FullDayScanTask getLast() {
      return fullDayScanTaskService.findLast();
   }
}