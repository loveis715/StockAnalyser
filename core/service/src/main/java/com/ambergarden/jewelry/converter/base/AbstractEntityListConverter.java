package com.ambergarden.jewelry.converter.base;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityListConverter<S, T>
   implements EntityListConverter<S, T> {
   /**
    * Transforms model object list retrieved from database to data transfer
    * object/business object list used in business logic
    *
    * @param moList the source model object collection
    * @return the converted data transfer object list
    */
   public List<T> convertListFrom(Iterable<S> moList) {
      List<T> dtoList = new ArrayList<T>();
      for (S mo : moList) {
         dtoList.add(convertFrom(mo));
      }
      return dtoList;
   }

   /**
    * Transforms data transfer object/business object list used in business logic
    * to model object list to be saved in database
    *
    * @param dto the data transfer object collection
    * @return the converted model object list
    */
   public List<S> convertListTo(Iterable<T> dtoList) {
      List<S> moList = new ArrayList<S>();
      for (T mo : dtoList) {
         moList.add(convertTo(mo));
      }
      return moList;
   }
}