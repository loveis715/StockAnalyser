package com.ambergarden.jewelry.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * The filter for supporting CORS.
 */
public class CORSFilter implements Filter {

   /**
    * Process the request, which will add CORS specific headers to HTTP response.
    */
   @Override
   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
         throws IOException, ServletException {
      HttpServletResponse response = (HttpServletResponse) res;
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type");
      chain.doFilter(req, res);
   }

   @Override
   public void init(FilterConfig filterConfig) {}

   @Override
   public void destroy() {}
}