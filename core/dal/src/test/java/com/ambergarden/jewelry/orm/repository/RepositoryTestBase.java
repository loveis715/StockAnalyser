package com.ambergarden.jewelry.orm.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ambergarden.jewelry.orm.DataInitializerBean;
import com.ambergarden.jewelry.orm.DatabaseInitializer;

public abstract class RepositoryTestBase implements ApplicationContextAware {
   private ApplicationContext context = null;

   @Autowired
   private DatabaseInitializer initializer;

   @Before
   public void setup() throws DatabaseUnitException, SQLException {
      String beanName = this.getCreationBeanName();
      DataInitializerBean bean = (DataInitializerBean)this.context.getBean(beanName);

      List<DataInitializerBean> initializers = new ArrayList<DataInitializerBean>();
      initializers.add(bean);
      initializer.setDataInitializers(initializers);
      initializer.init();
      initializer.resetSequences();
   }

   @After
   public void destroy() throws DatabaseUnitException, SQLException {
      String beanName = this.getRemovalBeanName();
      DataInitializerBean bean = (DataInitializerBean)this.context.getBean(beanName);

      List<DataInitializerBean> initializers = new ArrayList<DataInitializerBean>();
      initializers.add(bean);
      initializer.setDataInitializers(initializers);
      initializer.init();
      initializer.resetSequences();
   }

   public ApplicationContext getApplicationContext() {
      return this.context;
   }

   public void setApplicationContext(ApplicationContext context) {
      this.context = context;
   }

   public abstract String getCreationBeanName();
   public abstract String getRemovalBeanName();
}