In this tutorial, we will show you how to read data from a MySQL database, with `JdbcCursorItemReader` and `JdbcPagingItemReader`, and write it into an XML file.

Tools and libraries used

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring OXM 3.2.2.RELEASE
6.  Spring Batch 2.2.0.RELEASE
7.  MySQL Java Driver 5.1.25

_P.S This example – MySQL jdbc (reader) – XML (writer)._

## 1\. Project Directory Structure

Review the final project structure, a standard Maven project.

![spring batch database to xml](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-database-xml.png)

## 2\. Database

A “users” table, contains 5 records only, later read it with jdbc.

users table

    id, user_login, password, age

    '1','mkyong','password','30'
    '2','user_a','password','25'
    '3','user_b','password','10'
    '4','user_c','password','25'
    '5','user_d','password','40'

## 3\. Item Reader

Create a row mapper to map database values to “user” object.

User.java

    package com.mkyong;

    public class User {

    	int id;
    	String username;
    	String password;
    	int age;

    	//... getter and setter methods

    }

UserRowMapper.java

    package com.mkyong;

    import java.sql.ResultSet;
    import java.sql.SQLException;
    import org.springframework.jdbc.core.RowMapper;

    public class UserRowMapper implements RowMapper<User> {

    	@Override
    	public User mapRow(ResultSet rs, int rowNum) throws SQLException {

    		User user = new User();

    		user.setId(rs.getInt("id"));
    		user.setUsername(rs.getString("user_login"));
    		user.setPassword(rs.getString("user_pass"));
    		user.setAge(rs.getInt("age"));

    		return user;
    	}

    }

Example to read data from database.

job.xml

    <bean id="itemReader"
    class="org.springframework.batch.item.database.JdbcCursorItemReader"
    scope="step">
    <property name="dataSource" ref="dataSource" />
    <property name="sql"
    	value="select ID, USER_LOGIN, USER_PASS, AGE from USERS" />
    <property name="rowMapper">
    	<bean class="com.mkyong.UserRowMapper" />
    </property>
      </bean>

For big records, you can use `JdbcPagingItemReader`.

job.xml

    <bean id="pagingItemReader"
    class="org.springframework.batch.item.database.JdbcPagingItemReader"
    scope="step">
    <property name="dataSource" ref="dataSource" />
    <property name="queryProvider">
      <bean
    	class="org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean">
    	<property name="dataSource" ref="dataSource" />
    	<property name="selectClause" value="select id, user_login, user_pass, age" />
    	<property name="fromClause" value="from users" />
    	<property name="whereClause" value="where user_login=:name" />
    	<property name="sortKey" value="id" />
      </bean>
    </property>
    <property name="parameterValues">
       <map>
    	<entry key="name" value="#{jobParameters['name']}" />
       </map>
    </property>
    <property name="pageSize" value="10" />
    <property name="rowMapper">
    	<bean class="com.mkyong.UserRowMapper" />
    </property>
      </bean>

## 4\. Item Writer

Write data to an XML file.

job.xml

    <bean id="itemWriter"
                    class="org.springframework.batch.item.xml.StaxEventItemWriter">
    	<property name="resource" value="file:xml/outputs/users.xml" />
    	<property name="marshaller" ref="userUnmarshaller" />
    	<property name="rootTagName" value="users" />
    </bean>

    <bean id="userUnmarshaller"
                    class="org.springframework.oxm.xstream.XStreamMarshaller">
    	<property name="aliases">
    		<util:map id="aliases">
    			<entry key="user" value="com.mkyong.User" />
    		</util:map>
    	</property>
    </bean>

## 5\. Spring Batch Jobs

A job to read data from MySQL and write it XML file.

resources/spring/batch/jobs/job-extract-users.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:batch="http://www.springframework.org/schema/batch"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/batch
    		http://www.springframework.org/schema/batch/spring-batch-2.2.xsd
    		http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		">

      <import resource="../config/context.xml" />
      <import resource="../config/database.xml" />

      <bean id="itemReader"
    	class="org.springframework.batch.item.database.JdbcCursorItemReader"
    	scope="step">
    	<property name="dataSource" ref="dataSource" />
    	<property name="sql"
    		value="select ID, USER_LOGIN, USER_PASS, AGE from USERS where age > #{jobParameters['age']}" />
    	<property name="rowMapper">
    		<bean class="com.mkyong.UserRowMapper" />
    	</property>
      </bean>

      <job id="testJob" xmlns="http://www.springframework.org/schema/batch">
    	<step id="step1">
    	  <tasklet>
    		<chunk reader="pagingItemReader" writer="itemWriter"
    			commit-interval="1" />
    	  </tasklet>
    	</step>
      </job>

      <bean id="itemWriter" class="org.springframework.batch.item.xml.StaxEventItemWriter">
    	<property name="resource" value="file:xml/outputs/users.xml" />
    	<property name="marshaller" ref="userUnmarshaller" />
    	<property name="rootTagName" value="users" />
      </bean>

      <bean id="userUnmarshaller" class="org.springframework.oxm.xstream.XStreamMarshaller">
    	<property name="aliases">
    		<util:map id="aliases">
    			<entry key="user" value="com.mkyong.User" />
    		</util:map>
    	</property>
      </bean>
    </beans>

resources/spring/batch/config/database.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		http://www.springframework.org/schema/jdbc
    		http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd">

            <!-- connect to database -->
    	<bean id="dataSource"
    		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    		<property name="url" value="jdbc:mysql://localhost:3306/test" />
    		<property name="username" value="root" />
    		<property name="password" value="" />
    	</bean>

    	<bean id="transactionManager"
    	class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />

    </beans>

## 6\. Run It

Create a Java class and run the batch job.

App.java

    package com.mkyong;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.JobParametersBuilder;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {

      public static void main(String[] args) {
    	App obj = new App();
    	obj.run();
      }

      private void run() {

    	String[] springConfig = { "spring/batch/jobs/job-extract-users.xml" };

    	ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    	JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    	Job job = (Job) context.getBean("testJob");

    	try {

    		JobParameters param = new JobParametersBuilder().addString("age", "20").toJobParameters();

    		JobExecution execution = jobLauncher.run(job, param);
    		System.out.println("Exit Status : " + execution.getStatus());
    		System.out.println("Exit Status : " + execution.getAllFailureExceptions());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	System.out.println("Done");

      }

    }

Output. Extracts all “user where age > 20” into an XML file.

xml/outputs/users.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <users>
    	<user>
    		<id>1</id>
    		<username>mkyong</username>
    		<password>password</password>
    		<age>30</age>
    	</user>
    	<user>
    		<id>2</id>
    		<username>user_a</username>
    		<password>password</password>
    		<age>25</age>
    	</user>
    	<user>
    		<id>4</id>
    		<username>user_c</username>
    		<password>password</password>
    		<age>25</age>
    	</user>
    	<user>
    		<id>5</id>
    		<username>user_d</username>
    		<password>password</password>
    		<age>40</age>
    	</user>
    </users>

[http://www.mkyong.com/spring-batch/spring-batch-example-mysql-database-to-xml/](http://www.mkyong.com/spring-batch/spring-batch-example-mysql-database-to-xml/)
