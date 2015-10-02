package com.ambergarden.jewelry.exception;

public class JewelryException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public JewelryException(String message, Throwable cause) {
      super(message, cause);
   }
}