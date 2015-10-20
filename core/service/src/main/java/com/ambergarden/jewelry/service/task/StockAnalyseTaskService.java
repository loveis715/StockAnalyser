package com.ambergarden.jewelry.service.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ambergarden.jewelry.converter.stock.StockConverter;
import com.ambergarden.jewelry.converter.task.StockAnalyseTaskConverter;
import com.ambergarden.jewelry.executor.StockAnalysisExecutor;
import com.ambergarden.jewelry.orm.entity.task.TaskState;
import com.ambergarden.jewelry.orm.repository.task.StockAnalyseTaskRepository;
import com.ambergarden.jewelry.schema.beans.stock.Stock;
import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTask;
import com.ambergarden.jewelry.schema.beans.task.StockAnalyseTaskRequest;
import com.ambergarden.jewelry.service.stock.StockService;

@Service
public class StockAnalyseTaskService {
   @Autowired
   private TaskExecutor taskExecutor;

   @Autowired
   private StockAnalysisExecutor stockAnalysisExecutor;

   @Autowired
   private StockService stockService;

   @Autowired
   private StockConverter stockConverter;

   @Autowired
   private StockAnalyseTaskRepository stockAnalyseTaskRepository;

   @Autowired
   private StockAnalyseTaskConverter stockAnalyseTaskConverter;

   public StockAnalyseTask findById(int id) {
      com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask stockAnalyseTask
         = stockAnalyseTaskRepository.findOne(id);
      // TODO: Check with NULL
      return stockAnalyseTaskConverter.convertFrom(stockAnalyseTask);
   }

   public StockAnalyseTaskRequest create(StockAnalyseTaskRequest request) {
      com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask stockAnalyseTask
         = new com.ambergarden.jewelry.orm.entity.task.StockAnalyseTask();
      stockAnalyseTask.setStartTime(new Date());
      stockAnalyseTask.setPercentage(0);
      stockAnalyseTask.setTaskState(TaskState.SCHEDULED);

      // TODO: Deal with incorrect alias
      Stock stock = stockService.getByAlias(request.getStockCode());
      stockAnalyseTask.setStock(stockConverter.convertTo(stock));

      stockAnalyseTask = stockAnalyseTaskRepository.save(stockAnalyseTask);

      List<String> stockCodes = new ArrayList<String>();
      stockCodes.add(request.getStockCode());
      stockAnalysisExecutor.setTargetStocks(stockCodes);
      taskExecutor.execute(stockAnalysisExecutor);

      // We will not persist the request for now.
      request.setAnalyseTaskId(stockAnalyseTask.getId());
      return request;
   }
}