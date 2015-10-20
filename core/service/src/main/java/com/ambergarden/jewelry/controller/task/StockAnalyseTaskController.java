package com.ambergarden.jewelry.controller.task;

import static com.ambergarden.jewelry.Constants.FIND_BY_ID_URL;
import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;
import static com.ambergarden.jewelry.Constants.STOCK_ANALYSE_TASKS_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTask;
import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTaskRequest;
import com.ambergarden.jewelry.service.task.StockAnalyseTaskService;

@Controller
@RequestMapping(value = STOCK_ANALYSE_TASKS_URL)
public class StockAnalyseTaskController {
   @Autowired
   private StockAnalyseTaskService stockAnalyseTaskService;

   @RequestMapping(value = FIND_BY_ID_URL, method = RequestMethod.GET)
   @ResponseBody
   public StockAnalyseTask findById(@PathVariable(ID_PATH_VARIABLE) int id) {
      return stockAnalyseTaskService.findById(id);
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public StockAnalyseTaskRequest create(@RequestBody StockAnalyseTaskRequest request) {
      return stockAnalyseTaskService.create(request);
   }
}