package com.ambergarden.jewelry.controller.task;

import static com.ambergarden.jewelry.Constants.FIND_BY_ID_URL;
import static com.ambergarden.jewelry.Constants.FIND_LAST_URL;
import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;
import static com.ambergarden.jewelry.Constants.STOCK_SYNCING_TASKS_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ambergarden.jewelry.schema.beans.task.StockSyncingTask;
import com.ambergarden.jewelry.service.task.StockSyncingTaskService;

@Controller
@RequestMapping(value = STOCK_SYNCING_TASKS_URL)
public class StockSyncingTaskController {
   @Autowired
   private StockSyncingTaskService syncingTaskService;

   @RequestMapping(value = FIND_BY_ID_URL, method = RequestMethod.GET)
   public StockSyncingTask findById(@PathVariable(ID_PATH_VARIABLE) int id) {
      return syncingTaskService.findById(id);
   }

   @RequestMapping(value = FIND_LAST_URL, method = RequestMethod.GET)
   public StockSyncingTask findLast() {
      return syncingTaskService.findLast();
   }

   @RequestMapping(method = RequestMethod.POST)
   public StockSyncingTask create() {
      return syncingTaskService.create();
   }
}