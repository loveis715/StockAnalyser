package com.ambergarden.jewelry.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ambergarden.jewelry.exception.JewelryException;
import com.ambergarden.jewelry.exception.JewelryLoggableException;
import com.ambergarden.jewelry.localization.Localizer;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
   private static Localizer localizer = Localizer.getLocalizer();

   @ExceptionHandler({
      JewelryLoggableException.class
   })
   @ResponseBody
   public ResponseEntity<?> handleJewelryLoggableException(JewelryLoggableException exception, WebRequest request) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(localizer.getLocalizedText("exception.handling.LoggableException"));
   }

   @ExceptionHandler({
      JewelryException.class
   })
   @ResponseBody
   public ResponseEntity<?> handleJewelryException(JewelryException exception, WebRequest request) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(localizer.getLocalizedText("exception.handling.UnhandledJewelryException"));
   }
}