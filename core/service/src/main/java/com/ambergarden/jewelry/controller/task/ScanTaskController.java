package com.ambergarden.jewelry.controller.task;

import static com.ambergarden.jewelry.Constants.FIND_BY_ID_URL;
import static com.ambergarden.jewelry.Constants.FIND_LAST_URL;
import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;
import static com.ambergarden.jewelry.Constants.SCAN_TASKS_URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ambergarden.jewelry.schema.beans.task.ScanTask;
import com.ambergarden.jewelry.service.task.ScanTaskService;

@Controller
@RequestMapping(value = SCAN_TASKS_URL)
public class ScanTaskController {
   @Autowired
   private ScanTaskService scanTaskService;

   @RequestMapping(method = RequestMethod.GET)
   @ResponseBody
   public List<ScanTask> list() {
      return scanTaskService.list();
   }

   @RequestMapping(value = FIND_BY_ID_URL, method = RequestMethod.GET)
   @ResponseBody
   public ScanTask findById(@PathVariable(ID_PATH_VARIABLE) int id) {
      return scanTaskService.findById(id);
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public ScanTask create() {
      return scanTaskService.create();
   }

   @RequestMapping(value = FIND_LAST_URL, method = RequestMethod.GET)
   @ResponseBody
   public ScanTask getLast() {
      return scanTaskService.findLast();
   }
}