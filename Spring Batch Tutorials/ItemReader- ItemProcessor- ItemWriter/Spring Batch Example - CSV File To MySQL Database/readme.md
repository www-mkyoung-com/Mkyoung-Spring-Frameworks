In this tutorial, we will show you how to configure a Spring Batch job to read data from a CSV file into a database.

Tools and libraries used :

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring Batch 2.2.0.RELEASE
6.  MySQL Java Driver 5.1.25

## 1\. Java Project

Create a Java Project with Maven

    $ mvn archetype:generate -DgroupId=com.mkyong -DartifactId=SpringBatchExample
      -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

Convert to Eclipse project, and imports it into Eclipse IDE.

    $ cd SpringBatchExample/
    $ mvn eclipse:eclipse

## 2\. Project Dependencies

Declares all project dependencies in _pom.xml_.

pom.xml

    <project xmlns="http://maven.apache.org/POM/4.0.0"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
    	http://maven.apache.org/maven-v4_0_0.xsd">
    	<modelVersion>4.0.0</modelVersion>
    	<groupId>com.mkyong</groupId>
    	<artifactId>SpringBatchExample</artifactId>
    	<packaging>jar</packaging>
    	<version>1.0-SNAPSHOT</version>
    	<name>SpringBatchExample</name>
    	<url>http://maven.apache.org</url>

    	<properties>
    		<jdk.version>1.6</jdk.version>
    		<spring.version>3.2.2.RELEASE</spring.version>
    		<spring.batch.version>2.2.0.RELEASE</spring.batch.version>
    		<mysql.driver.version>5.1.25</mysql.driver.version>
    	</properties>

    	<dependencies>

    		<!-- Spring Core -->
    		<dependency>
    			<groupId>org.springframework</groupId>
    			<artifactId>spring-core</artifactId>
    			<version>${spring.version}</version>
    		</dependency>

    		<!-- Spring jdbc, for database -->
    		<dependency>
    			<groupId>org.springframework</groupId>
    			<artifactId>spring-jdbc</artifactId>
    			<version>${spring.version}</version>
    		</dependency>

    		<!-- Spring Batch dependencies -->
    		<dependency>
    			<groupId>org.springframework.batch</groupId>
    			<artifactId>spring-batch-core</artifactId>
    			<version>${spring.batch.version}</version>
    		</dependency>
    		<dependency>
    			<groupId>org.springframework.batch</groupId>
    			<artifactId>spring-batch-infrastructure</artifactId>
    			<version>${spring.batch.version}</version>
    		</dependency>

    		<!-- MySQL database driver -->
    		<dependency>
    			<groupId>mysql</groupId>
    			<artifactId>mysql-connector-java</artifactId>
    			<version>${mysql.driver.version}</version>
    		</dependency>

    	</dependencies>
    	<build>
    		<finalName>spring-batch</finalName>
    		<plugins>
    		  <plugin>
    			<groupId>org.apache.maven.plugins</groupId>
    			<artifactId>maven-eclipse-plugin</artifactId>
    			<version>2.9</version>
    			<configuration>
    				<downloadSources>true</downloadSources>
    				<downloadJavadocs>false</downloadJavadocs>
    			</configuration>
    		  </plugin>
    		  <plugin>
    			<groupId>org.apache.maven.plugins</groupId>
    			<artifactId>maven-compiler-plugin</artifactId>
    			<version>2.3.2</version>
    			<configuration>
    				<source>${jdk.version}</source>
    				<target>${jdk.version}</target>
    			</configuration>
    		  </plugin>
    		</plugins>
    	</build>
    </project>

## 3\. Project Directory Structure

Review the final project structure.

![spring-batch-csv-database-project-structure](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-csv-database-project-structure.png)

## 4\. CSV File

This is the csv file in the resource folder.

report.csv

    Date,Impressions,Clicks,Earning
    6/1/13,"139,237",37,227.21
    6/2/13,"149,582",55,234.71
    6/3/13,"457,425",132,211.48
    6/4/13,"466,870",141,298.40
    6/5/13,"472,385",194,281.35
    ......

## 5\. MySQL Database

Defines a “dataSource” bean for MySQL database. The `jdbc:initialize-database` is used to create the metadata tables automatically, Spring Batch need it to store the job’s detail.

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

      <!-- create job-meta tables automatically -->
      <jdbc:initialize-database data-source="dataSource">
    	<jdbc:script location="org/springframework/batch/core/schema-drop-mysql.sql" />
    	<jdbc:script location="org/springframework/batch/core/schema-mysql.sql" />
      </jdbc:initialize-database>

    </beans>

## 6\. Spring Batch Core Setting

Defines `jobRepository` and `jobLauncher`.

resources/spring/batch/config/context.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

      <!-- stored job-metadata in database -->
      <bean id="jobRepository"
    	class="org.springframework.batch.core.repository.support.JobRepositoryFactoryBean">
    	<property name="dataSource" ref="dataSource" />
    	<property name="transactionManager" ref="transactionManager" />
    	<property name="databaseType" value="mysql" />
      </bean>

      <!-- stored job-metadata in memory -->
      <!--
      <bean id="jobRepository"
    	class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean">
    	<property name="transactionManager" ref="transactionManager" />
      </bean>
       -->

      <bean id="jobLauncher"
    	class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
    	<property name="jobRepository" ref="jobRepository" />
      </bean>

    </beans>

## 7\. Spring Batch Jobs

This is the main xml file to configure the Spring batch job. This `job-report.xml` file define a job to read a `report.csv` file, match it to `report` plain pojo and write the data into MySQL database.

Read the comment, it should be self-explanatory. Btw, remember create the “**RAW_REPORT**” table manually.

resources/spring/batch/jobs/job-report.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:batch="http://www.springframework.org/schema/batch"
    	xmlns:task="http://www.springframework.org/schema/task"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/batch
    	http://www.springframework.org/schema/batch/spring-batch-2.2.xsd
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

      <bean id="report" class="com.mkyong.model.Report" scope="prototype" />

      <batch:job id="reportJob">
    	<batch:step id="step1">
    	  <batch:tasklet>
    		<batch:chunk reader="cvsFileItemReader" writer="mysqlItemWriter"
    			commit-interval="2">
    		</batch:chunk>
    	  </batch:tasklet>
    	</batch:step>
      </batch:job>

      <bean id="cvsFileItemReader" class="org.springframework.batch.item.file.FlatFileItemReader">

    	<!-- Read a csv file -->
    	<property name="resource" value="classpath:cvs/report.csv" />

    	<property name="lineMapper">
    		<bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
    		  <!-- split it -->
    		  <property name="lineTokenizer">
    		        <bean
    			  class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    				<property name="names" value="date,impressions,clicks,earning" />
    			</bean>
    		  </property>
    		  <property name="fieldSetMapper">
    		         <!-- return back to reader, rather than a mapped object. -->
    		         <!--
    			 <bean class="org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper" />
    		          -->
    			  <!-- map to an object -->
    			  <bean
    			    class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    				<property name="prototypeBeanName" value="report" />
    			  </bean>
    		  </property>

    		  </bean>
    	  </property>

      </bean>

      <bean id="mysqlItemWriter"
    	class="org.springframework.batch.item.database.JdbcBatchItemWriter">
    	<property name="dataSource" ref="dataSource" />
    	<property name="sql">
    	  <value>
                <![CDATA[
                	insert into RAW_REPORT(DATE,IMPRESSIONS,CLICKS,EARNING)
    			values (:date, :impressions, :clicks, :earning)
                ]]>
    	  </value>
    	</property>
    	<!-- It will take care matching between object property and sql name parameter -->
    	<property name="itemSqlParameterSourceProvider">
    		<bean
    		class="org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider" />
    	</property>
      </bean>

    </beans>

com/mkyong/model/Report.java

    package com.mkyong.model;

    public class Report {

    	private String Date;
    	private String Impressions;
    	private String Clicks;
    	private String Earning;

    	//getter and setter methods

    }

**Note**  
For detail explanation, please refer to this [Spring batch references](http://static.springsource.org/spring-batch/reference/html/).

## 8\. Run It

Loads everything and run it `jobLauncher`. This is the simplest way to start and test it, but, in real life, you may need to launch it with scheduler frameworks like Spring task, Quartz or system scheduler like “cron” command (I will show you in coming tutorials).

com/mkyong/App.java

    package com.mkyong;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {
      public static void main(String[] args) {

    	String[] springConfig  =
    		{	"spring/batch/config/database.xml",
    			"spring/batch/config/context.xml",
    			"spring/batch/jobs/job-report.xml"
    		};

    	ApplicationContext context =
    		new ClassPathXmlApplicationContext(springConfig);

    	JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    	Job job = (Job) context.getBean("reportJob");

    	try {

    		JobExecution execution = jobLauncher.run(job, new JobParameters());
    		System.out.println("Exit Status : " + execution.getStatus());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	System.out.println("Done");

      }
    }

Output. The Spring Batch metadata tables are created, and the content of `report.cvs` is inserted into database table “**RAW_REPORT**“.

![spring-batch-cvs-database-data](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-cvs-database-data.png)

Done.

[http://www.mkyong.com/spring-batch/spring-batch-example-csv-file-to-database/](http://www.mkyong.com/spring-batch/spring-batch-example-csv-file-to-database/)
