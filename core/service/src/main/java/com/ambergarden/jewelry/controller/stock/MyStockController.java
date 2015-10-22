package com.ambergarden.jewelry.controller.stock;

import static com.ambergarden.jewelry.Constants.ID_PATH_VARIABLE;
import static com.ambergarden.jewelry.Constants.MY_STOCK_URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ambergarden.jewelry.schema.beans.stock.MyStock;
import com.ambergarden.jewelry.schema.beans.task.ScanTaskRequest;
import com.ambergarden.jewelry.service.stock.MyStockService;

@Controller
@RequestMapping(value = MY_STOCK_URL)
public class MyStockController {
   @Autowired
   private MyStockService myStockService;

   @RequestMapping(method = RequestMethod.GET)
   @ResponseBody
   public List<MyStock> list() {
      return myStockService.findAll();
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public MyStock create(@RequestBody MyStock myStock) {
      return myStockService.create(myStock);
   }

   @RequestMapping(method = RequestMethod.PUT)
   @ResponseBody
   public MyStock update(@PathVariable(ID_PATH_VARIABLE) int id, @RequestBody MyStock myStock) {
      return myStockService.update(id, myStock);
   }

   @RequestMapping(method = RequestMethod.DELETE)
   @ResponseBody
   public void delete(@PathVariable(ID_PATH_VARIABLE) int id) {
      myStockService.delete(id);
   }
}