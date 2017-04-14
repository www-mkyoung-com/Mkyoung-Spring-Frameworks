In this tutorials, you will learn how to create a simple customer management (add and select) web application, Maven as project management tool, Struts 1.x as web framework, Spring as dependency injection framework and Hibernate as database ORM framework.

The overall integration architecture is look like following :

    Struts (Web page) <---> Spring DI <--> Hibernate (DAO) <---> Database

To integrate all those technologies together, you should..

1.  Integrate Spring with Hibernate with Spring’s “**LocalSessionFactoryBean**” class.
2.  Integrate Spring with Struts via Spring’s ready make Struts plug-in – “**ContextLoaderPlugIn**“.

## 1\. Project Structure

This is this final project structure.

![struts-spring-hibernate-1](http://www.mkyong.com/wp-content/uploads/2010/04/struts-spring-hibernate-1.jpg)

![struts-spring-hibernate-2](http://www.mkyong.com/wp-content/uploads/2010/04/struts-spring-hibernate-2.jpg)

## 2\. Table script

Create a customer table to store the customer details.

    DROP TABLE IF EXISTS `mkyong`.`customer`;
    CREATE TABLE  `mkyong`.`customer` (
      `CUSTOMER_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
      `NAME` varchar(45) NOT NULL,
      `ADDRESS` varchar(255) NOT NULL,
      `CREATED_DATE` datetime NOT NULL,
      PRIMARY KEY (`CUSTOMER_ID`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

## 3\. Maven details

Define all the Struts, Spring and Hibernate dependency libraries in pom.xml.  
**pom.xml**

    <project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.mkyong.common</groupId>
      <artifactId>StrutsSpringExample</artifactId>
      <packaging>war</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>StrutsExample Maven Webapp</name>
      <url>http://maven.apache.org</url>

      <repositories>
      	<repository>
      		<id>Java.Net</id>
      		<url>http://download.java.net/maven/2/</url>
      	</repository>

    	<repository>
    		<id>JBoss repository</id>
    		<url>http://repository.jboss.com/maven2/</url>
    	</repository>

      </repositories>

      <dependencies>

            <!-- Spring framework -->
    	<dependency>
    	  <groupId>org.springframework</groupId>
    	  <artifactId>spring</artifactId>
    	  <version>2.5.6</version>
    	</dependency>

            <dependency>
    	  <groupId>org.springframework</groupId>
    	  <artifactId>spring-web</artifactId>
    	  <version>2.5.6</version>
    	</dependency>

    	<dependency>
    	  <groupId>org.springframework</groupId>
    	  <artifactId>spring-struts</artifactId>
    	  <version>2.0.8</version>
    	</dependency>

            <!-- J2EE library -->
    	<dependency>
    	  <groupId>javax</groupId>
    	  <artifactId>javaee-api</artifactId>
    	  <version>6.0</version>
    	</dependency>

            <!-- Unit Test -->
            <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>3.8.1</version>
              <scope>test</scope>
            </dependency>

            <!-- Struts 1.3 framework -->
            <dependency>
              <groupId>org.apache.struts</groupId>
    	  <artifactId>struts-core</artifactId>
              <version>1.3.10</version>
            </dependency>

            <dependency>
              <groupId>org.apache.struts</groupId>
    	  <artifactId>struts-taglib</artifactId>
              <version>1.3.10</version>
            </dependency>

            <dependency>
              <groupId>org.apache.struts</groupId>
    	  <artifactId>struts-extras</artifactId>
              <version>1.3.10</version>
            </dependency>

            <!-- MySQL database driver -->
    	<dependency>
    	  <groupId>mysql</groupId>
    	  <artifactId>mysql-connector-java</artifactId>
    	  <version>5.1.9</version>
    	</dependency>

    	<!-- Hibernate core -->
    	<dependency>
    	  <groupId>org.hibernate</groupId>
    	  <artifactId>hibernate</artifactId>
    	  <version>3.2.7.ga</version>
    	</dependency>

    	<!-- Hibernate core library dependecy start -->
    	<dependency>
    	  <groupId>dom4j</groupId>
    	  <artifactId>dom4j</artifactId>
    	  <version>1.6.1</version>
    	</dependency>

    	<dependency>
    	  <groupId>commons-logging</groupId>
    	  <artifactId>commons-logging</artifactId>
    	  <version>1.1.1</version>
    	</dependency>

    	<dependency>
    	  <groupId>commons-collections</groupId>
    	  <artifactId>commons-collections</artifactId>
    	  <version>3.2.1</version>
    	</dependency>

    	<dependency>
    	  <groupId>cglib</groupId>
    	  <artifactId>cglib</artifactId>
    	  <version>2.2</version>
    	</dependency>
    	<!-- Hibernate core library dependecy end -->

    	<!-- Hibernate query library dependecy start -->
    	<dependency>
    	  <groupId>antlr</groupId>
    	  <artifactId>antlr</artifactId>
    	  <version>2.7.7</version>
    	</dependency>
    	<!-- Hibernate query library dependecy end -->

      </dependencies>
      <build>
        <finalName>StrutsExample</finalName>
      </build>
    </project>

## 4\. Hibernate

Nothing much need to configure in Hibernate, just declare a customer XML mapping file and model.  
**Customer.hbm.xml**

    <?xml version="1.0"?>
    <!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
    <hibernate-mapping>
        <class name="com.mkyong.customer.model.Customer"
            table="customer" catalog="mkyong">

            <id name="customerId" type="long">
                <column name="CUSTOMER_ID" />
                <generator class="identity" />
            </id>
            <property name="name" type="string">
                <column name="NAME" length="45" not-null="true" />
            </property>
            <property name="address" type="string">
                <column name="ADDRESS" not-null="true" />
            </property>
            <property name="createdDate" type="timestamp">
                <column name="CREATED_DATE" length="19" not-null="true" />
            </property>
        </class>
    </hibernate-mapping>

**Customer.java**

    package com.mkyong.customer.model;

    import java.util.Date;

    public class Customer implements java.io.Serializable {

    	private long customerId;
    	private String name;
    	private String address;
    	private Date createdDate;

    	//getter and setter methods

    }

## 5\. Spring

Spring’s beans declaration for Business Object (BO) and Data Access Object (DAO). The DAO class (CustomerDaoImpl.java) is extends Spring’s “**HibernateDaoSupport**” class to access the Hibernate function easily.  
**CustomerBean.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       	<bean id="customerBo"
                    class="com.mkyong.customer.bo.impl.CustomerBoImpl" >
       		<property name="customerDao" ref="customerDao" />
       	</bean>

       	<bean id="customerDao"
                    class="com.mkyong.customer.dao.impl.CustomerDaoImpl" >
       		<property name="sessionFactory" ref="sessionFactory"></property>
       	</bean>

    </beans>

**CustomerBo.java**

    package com.mkyong.customer.bo;

    import java.util.List;

    import com.mkyong.customer.model.Customer;

    public interface CustomerBo{

    	void addCustomer(Customer customer);

    	List<Customer> findAllCustomer();

    }

**CustomerBoImpl.java**

    package com.mkyong.customer.bo.impl;

    import java.util.List;

    import com.mkyong.customer.bo.CustomerBo;
    import com.mkyong.customer.dao.CustomerDao;
    import com.mkyong.customer.model.Customer;

    public class CustomerBoImpl implements CustomerBo{

    	CustomerDao customerDao;

    	public void setCustomerDao(CustomerDao customerDao) {
    		this.customerDao = customerDao;
    	}

    	public void addCustomer(Customer customer){

    		customerDao.addCustomer(customer);

    	}

    	public List<Customer> findAllCustomer(){

    		return customerDao.findAllCustomer();
    	}
    }

**CustomerDao.java**

    package com.mkyong.customer.dao;

    import java.util.List;

    import com.mkyong.customer.model.Customer;

    public interface CustomerDao{

    	void addCustomer(Customer customer);

    	List<Customer> findAllCustomer();

    }

**CustomerDaoImpl.java**

    package com.mkyong.customer.dao.impl;

    import java.util.Date;
    import java.util.List;

    import com.mkyong.customer.dao.CustomerDao;
    import com.mkyong.customer.model.Customer;
    import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

    public class CustomerDaoImpl extends
           HibernateDaoSupport implements CustomerDao{

    	public void addCustomer(Customer customer){

    		customer.setCreatedDate(new Date());
    		getHibernateTemplate().save(customer);

    	}

    	public List<Customer> findAllCustomer(){

    		return getHibernateTemplate().find("from Customer");

    	}
    }

## 6\. Spring + Hibernate

Declare the database details and integrate Spring and Hibernate together via “**LocalSessionFactoryBean**“.  
**database.properties**

    jdbc.driverClassName=com.mysql.jdbc.Driver
    jdbc.url=jdbc:mysql://localhost:3306/mkyong
    jdbc.username=root
    jdbc.password=password

**DataSource.xml**

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

     <bean
       class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
       <property name="location">
         <value>WEB-INF/classes/config/database/properties/database.properties</value>
       </property>
    </bean>

      <bean id="dataSource"
             class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    	<property name="driverClassName" value="${jdbc.driverClassName}" />
    	<property name="url" value="${jdbc.url}" />
    	<property name="username" value="${jdbc.username}" />
    	<property name="password" value="${jdbc.password}" />
      </bean>

    </beans>

**HibernateSessionFactory.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <!-- Hibernate session factory -->
    <bean id="sessionFactory"
         class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">

        <property name="dataSource">
          <ref bean="dataSource"/>
        </property>

        <property name="hibernateProperties">
           <props>
             <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
             <prop key="hibernate.show_sql">true</prop>
           </props>
        </property>

        <property name="mappingResources">
    	<list>
              <value>com/mkyong/customer/hibernate/Customer.hbm.xml</value>
    	</list>
         </property>

    </bean>
    </beans>

**SpringBeans.xml**

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<!-- Database Configuration -->
    	<import resource="config/database/spring/DataSource.xml"/>
    	<import resource="config/database/spring/HibernateSessionFactory.xml"/>

    	<!-- Beans Declaration -->
    	<import resource="com/mkyong/customer/spring/CustomerBean.xml"/>

    </beans>

## 7\. Struts + Spring

To integrate Spring with Struts, you need to registering a Spring’s build-in Struts plug-in “**ContextLoaderPlugIn**” in struts-config.xml file. In Action class, it have to extends the Spring’s “**ActionSupport**” class, and you can get the Spring bean via **getWebApplicationContext()**.

**AddCustomerAction.java**

    package com.mkyong.customer.action;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.apache.commons.beanutils.BeanUtils;
    import org.apache.struts.action.ActionForm;
    import org.apache.struts.action.ActionForward;
    import org.apache.struts.action.ActionMapping;
    import org.springframework.web.struts.ActionSupport;

    import com.mkyong.customer.bo.CustomerBo;
    import com.mkyong.customer.form.CustomerForm;
    import com.mkyong.customer.model.Customer;

    public class AddCustomerAction extends ActionSupport{

    public ActionForward execute(ActionMapping mapping,ActionForm form,
    	HttpServletRequest request,HttpServletResponse response)
            throws Exception {

    	CustomerBo customerBo =
     	  (CustomerBo) getWebApplicationContext().getBean("customerBo");

    	CustomerForm customerForm = (CustomerForm)form;
    	Customer customer = new Customer();

    	//copy customerform to model
    	BeanUtils.copyProperties(customer, customerForm);

    	//save it
    	customerBo.addCustomer(customer);

    	return mapping.findForward("success");

      }
    }

**ListCustomerAction.java**

    package com.mkyong.customer.action;

    import java.util.List;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.apache.struts.action.ActionForm;
    import org.apache.struts.action.ActionForward;
    import org.apache.struts.action.ActionMapping;
    import org.apache.struts.action.DynaActionForm;
    import org.springframework.web.struts.ActionSupport;

    import com.mkyong.customer.bo.CustomerBo;
    import com.mkyong.customer.model.Customer;

    public class ListCustomerAction extends ActionSupport{

      public ActionForward execute(ActionMapping mapping,ActionForm form,
    	HttpServletRequest request,HttpServletResponse response)
            throws Exception {

    	CustomerBo customerBo =
    	  (CustomerBo) getWebApplicationContext().getBean("customerBo");

    	DynaActionForm dynaCustomerListForm = (DynaActionForm)form;

    	List<Customer> list = customerBo.findAllCustomer();

    	dynaCustomerListForm.set("customerList", list);

    	return mapping.findForward("success");

      }
    }

**CustomerForm.java**

    package com.mkyong.customer.form;

    import javax.servlet.http.HttpServletRequest;

    import org.apache.struts.action.ActionErrors;
    import org.apache.struts.action.ActionForm;
    import org.apache.struts.action.ActionMapping;
    import org.apache.struts.action.ActionMessage;

    public class CustomerForm extends ActionForm {

    	private String name;
    	private String address;

    	//getter and setter, basic validation

    }

**Customer.properties**

    #customer module label message
    customer.label.name = Name
    customer.label.address = Address

    customer.label.button.submit = Submit
    customer.label.button.reset = Reset

    #customer module error message
    customer.err.name.required = Name is required
    customer.err.address.required = Address is required

**add_customer.jsp**

<pre><%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

</pre>

# Struts + Spring + Hibernate example

## Add Customer

:

:

**list_customer.jsp**

<pre><%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

</pre>

# Struts + Spring + Hibernate example

## List All Customers

Customer NameAddress  

Add Customer

**struts-config.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE struts-config PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN"
    "http://jakarta.apache.org/struts/dtds/struts-config_1_3.dtd">

    <struts-config>

    	<form-beans>
    		<form-bean name="customerForm"
    		      type="com.mkyong.customer.form.CustomerForm" />

    		<form-bean name="dynaCustomerListForm"
    		      type="org.apache.struts.action.DynaActionForm">
    		      <form-property name="customerList" type="java.util.List"/>
    		</form-bean>

    	</form-beans>

    	<action-mappings>

    	 <action
    		path="/AddCustomerPage"
    		type="org.apache.struts.actions.ForwardAction"
    		parameter="/pages/customer/add_customer.jsp"/>

    	<action
    		path="/AddCustomer"
    		type="com.mkyong.customer.action.AddCustomerAction"
    		name="customerForm"
    		validate="true"
    		input="/pages/customer/add_customer.jsp"
    		>

    		<forward name="success" redirect="true" path="/ListCustomer.do"/>
    	</action>

    	<action
    		path="/ListCustomer"
    		type="com.mkyong.customer.action.ListCustomerAction"
    		name="dynaCustomerListForm"
    		>

    		<forward name="success" path="/pages/customer/list_customer.jsp"/>
    	</action>
          </action-mappings>

    	<message-resources
    		parameter="com.mkyong.customer.properties.Customer" />

     	<!-- Spring Struts plugin -->
     	<plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
    		<set-property property="contextConfigLocation"
    		value="/WEB-INF/classes/SpringBeans.xml" />
      	</plug-in>

    </struts-config>

**web.xml**

    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >

    <web-app>
      <display-name>Struts Hibernate Examples</display-name>

      <servlet>
        <servlet-name>action</servlet-name>
        <servlet-class>
            org.apache.struts.action.ActionServlet
        </servlet-class>
        <init-param>
            <param-name>config</param-name>
            <param-value>
             /WEB-INF/struts-config.xml
            </param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
      </servlet>

      <servlet-mapping>
           <servlet-name>action</servlet-name>
           <url-pattern>*.do</url-pattern>
      </servlet-mapping>

    </web-app>

## 8\. Demonstration

**1\. List customer page**  
List all customers from database.  
_http://localhost:8080/StrutsSpringExample/ListCustomer.do_

![struts-spring-hibernate-demo-1](http://www.mkyong.com/wp-content/uploads/2010/04/struts-spring-hibernate-demo-1.jpg)

**2\. Add customer page**  
Add customer detail into database.  
_http://localhost:8080/StrutsSpringExample/AddCustomerPage.do_

![struts-spring-hibernate-demo-2](http://www.mkyong.com/wp-content/uploads/2010/04/struts-spring-hibernate-demo-2.jpg)

[http://www.mkyong.com/struts/struts-spring-hibernate-integration-example/](http://www.mkyong.com/struts/struts-spring-hibernate-integration-example/)
