package com.ambergarden.jewelry.controller.task;

import static com.ambergarden.jewelry.Constants.FIND_BY_ID_URL;
import static com.ambergarden.jewelry.Constants.FIND_LAST_URL;
import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;
import static com.ambergarden.jewelry.Constants.TEST_SCAN_TASKS_URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ambergarden.jewelry.schema.beans.task.TestScanTask;
import com.ambergarden.jewelry.schema.beans.task.TestScanTaskRequest;
import com.ambergarden.jewelry.service.task.TestScanTaskService;

@Controller
@RequestMapping(value = TEST_SCAN_TASKS_URL)
public class TestScanTaskController {
   @Autowired
   private TestScanTaskService testScanTaskService;

   @RequestMapping(method = RequestMethod.GET)
   @ResponseBody
   public List<TestScanTask> list() {
      return testScanTaskService.list();
   }

   @RequestMapping(value = FIND_BY_ID_URL, method = RequestMethod.GET)
   @ResponseBody
   public TestScanTask findById(@PathVariable(ID_PATH_VARIABLE) int id) {
      return testScanTaskService.findById(id);
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public TestScanTaskRequest create(@RequestBody TestScanTaskRequest request) {
      return testScanTaskService.create(request);
   }

   @RequestMapping(value = FIND_LAST_URL, method = RequestMethod.GET)
   @ResponseBody
   public TestScanTask getLast() {
      return testScanTaskService.findLast();
   }
}
