package com.ambergarden.jewelry.exception;

public class JewelryLoggableException extends JewelryException {

   private static final long serialVersionUID = -3572950566992164958L;

   public JewelryLoggableException(String message, Throwable cause) {
      super(message, cause);
   }
}