In this tutorial, we will extend last [Maven + Spring hello world example](http://www.mkyong.com/spring/quick-start-maven-spring-example/) by adding JDBC support, to use Spring + JDBC to insert a record into a customer table.

## 1\. Customer table

In this example, we are using MySQL database.

    CREATE TABLE `customer` (
      `CUST_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
      `NAME` varchar(100) NOT NULL,
      `AGE` int(10) unsigned NOT NULL,
      PRIMARY KEY (`CUST_ID`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

## 2\. Project Dependency

Add Spring and MySQL dependencies in Maven `pom.xml` file.

_File : pom.xml_

    <project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.mkyong.common</groupId>
      <artifactId>SpringExample</artifactId>
      <packaging>jar</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>SpringExample</name>
      <url>http://maven.apache.org</url>

      <dependencies>

            <!-- Spring framework -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring</artifactId>
    		<version>2.5.6</version>
    	</dependency>

            <!-- MySQL database driver -->
    	<dependency>
    		<groupId>mysql</groupId>
    		<artifactId>mysql-connector-java</artifactId>
    		<version>5.1.9</version>
    	</dependency>

      </dependencies>
    </project>

## 3\. Customer model

Add a customer model to store customer’s data.

    package com.mkyong.customer.model;

    import java.sql.Timestamp;

    public class Customer
    {
    	int custId;
    	String name;
    	int age;
    	//getter and setter methods

    }

## 4\. Data Access Object (DAO) pattern

Customer Dao interface.

    package com.mkyong.customer.dao;

    import com.mkyong.customer.model.Customer;

    public interface CustomerDAO
    {
    	public void insert(Customer customer);
    	public Customer findByCustomerId(int custId);
    }

Customer Dao implementation, use JDBC to issue a simple insert and select statement.

    package com.mkyong.customer.dao.impl;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import javax.sql.DataSource;
    import com.mkyong.customer.dao.CustomerDAO;
    import com.mkyong.customer.model.Customer;

    public class JdbcCustomerDAO implements CustomerDAO
    {
    	private DataSource dataSource;

    	public void setDataSource(DataSource dataSource) {
    		this.dataSource = dataSource;
    	}

    	public void insert(Customer customer){

    		String sql = "INSERT INTO CUSTOMER " +
    				"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";
    		Connection conn = null;

    		try {
    			conn = dataSource.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			ps.setInt(1, customer.getCustId());
    			ps.setString(2, customer.getName());
    			ps.setInt(3, customer.getAge());
    			ps.executeUpdate();
    			ps.close();

    		} catch (SQLException e) {
    			throw new RuntimeException(e);

    		} finally {
    			if (conn != null) {
    				try {
    					conn.close();
    				} catch (SQLException e) {}
    			}
    		}
    	}

    	public Customer findByCustomerId(int custId){

    		String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";

    		Connection conn = null;

    		try {
    			conn = dataSource.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			ps.setInt(1, custId);
    			Customer customer = null;
    			ResultSet rs = ps.executeQuery();
    			if (rs.next()) {
    				customer = new Customer(
    					rs.getInt("CUST_ID"),
    					rs.getString("NAME"),
    					rs.getInt("Age")
    				);
    			}
    			rs.close();
    			ps.close();
    			return customer;
    		} catch (SQLException e) {
    			throw new RuntimeException(e);
    		} finally {
    			if (conn != null) {
    				try {
    				conn.close();
    				} catch (SQLException e) {}
    			}
    		}
    	}
    }

## 5\. Spring bean configuration

Create the Spring bean configuration file for customerDAO and datasource.  
_File : Spring-Customer.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerDAO" class="com.mkyong.customer.dao.impl.JdbcCustomerDAO">
    		<property name="dataSource" ref="dataSource" />
    	</bean>

    </beans>

_File : Spring-Datasource.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="dataSource"
    		class="org.springframework.jdbc.datasource.DriverManagerDataSource">

    		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    		<property name="url" value="jdbc:mysql://localhost:3306/mkyongjava" />
    		<property name="username" value="root" />
    		<property name="password" value="password" />
    	</bean>

    </beans>

_File : Spring-Module.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<import resource="database/Spring-Datasource.xml" />
    	<import resource="customer/Spring-Customer.xml" />

    </beans>

## 6\. Review project structure

Full directory structure of this example.

![](http://www.mkyong.com/wp-content/uploads/2010/03/maven-spring-jdbc.png)

## 7\. Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import com.mkyong.customer.dao.CustomerDAO;
    import com.mkyong.customer.model.Customer;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        		new ClassPathXmlApplicationContext("Spring-Module.xml");

            CustomerDAO customerDAO = (CustomerDAO) context.getBean("customerDAO");
            Customer customer = new Customer(1, "mkyong",28);
            customerDAO.insert(customer);

            Customer customer1 = customerDAO.findByCustomerId(1);
            System.out.println(customer1);

        }
    }

output

    Customer [age=28, custId=1, name=mkyong]

[http://www.mkyong.com/spring/maven-spring-jdbc-example/](http://www.mkyong.com/spring/maven-spring-jdbc-example/)
