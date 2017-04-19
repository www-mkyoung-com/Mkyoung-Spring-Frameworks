Spring Batch is a framework for batch processing – execution of a series of jobs. In Spring Batch, A job consists of many steps and each step consists of a `READ-PROCESS-WRITE` task or `single operation `task (tasklet).

1.  For “READ-PROCESS-WRITE” process, it means “read” data from the resources (csv, xml or database), “process” it and “write” it to other resources (csv, xml and database). For example, a step may read data from a CSV file, process it and write it into the database. Spring Batch provides many made Classes to read/write CSV, XML and database.
2.  For “single” operation task (tasklet), it means doing single task only, like clean up the resources after or before a step is started or completed.
3.  And the steps can be chained together to run as a job.

    1 Job = Many Steps.
    1 Step = 1 READ-PROCESS-WRITE or 1 Tasklet.
    Job = {Step 1 -> Step 2 -> Step 3} (Chained together)

## Spring Batch Examples

Consider following batch jobs :

1.  Step 1 – Read CSV files from folder A, process, write it to folder B. “READ-PROCESS-WRITE”
2.  Step 2 – Read CSV files from folder B, process, write it to the database. “READ-PROCESS-WRITE”
3.  Step 3 – Delete the CSB files from folder B. “Tasklet”
4.  Step 4 – Read data from a database, process and generate statistic report in XML format, write it to folder C. “READ-PROCESS-WRITE”
5.  Step 5 – Read the report and send it to manager email. “Tasklet”

In Spring Batch, we can declare like the following :

    <job id="abcJob" xmlns="http://www.springframework.org/schema/batch">
    <step id="step1" next="step2">
      <tasklet>
    	<chunk reader="cvsItemReader" writer="cvsItemWriter"
                        processor="itemProcesser" commit-interval="1" />
      </tasklet>
    </step>
    <step id="step2" next="step3">
      <tasklet>
    	<chunk reader="cvsItemReader" writer="databaseItemWriter"
                        processor="itemProcesser" commit-interval="1" />
      </tasklet>
    </step>
    <step id="step3" next="step4">
      <tasklet ref="fileDeletingTasklet" />
    </step>
    <step id="step4" next="step5">
      <tasklet>
    	<chunk reader="databaseItemReader" writer="xmlItemWriter"
                        processor="itemProcesser" commit-interval="1" />
      </tasklet>
    </step>
    <step id="step5">
    	<tasklet ref="sendingEmailTasklet" />
    </step>
      </job>

The entire jobs and steps execution are stored in database, which make the failed step is able to restart at where it was failed, no need start over the entire job.

## 1\. Tutorial

In this Spring Batch tutorial, we will show you how to create a job, read a CSV file, process it, write the output to an XML file.

Tools and libraries used

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring OXM 3.2.2.RELEASE
6.  Spring JDBC 3.2.2.RELEASE
7.  Spring Batch 2.2.0.RELEASE

## 2\. Project Directory

Review final project directory, a standard Maven project.

![spring-batch-hello-world-directory](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-hello-world-directory.png)

## 3\. Project Dependencies

They must have dependencies are just Spring Core, Spring Batch and JDK 1.5\. Read comments for self-explanatory.

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
    		<junit.version>4.11</junit.version>
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

    		<!-- Spring XML to/back object -->
    		<dependency>
    			<groupId>org.springframework</groupId>
    			<artifactId>spring-oxm</artifactId>
    			<version>${spring.version}</version>
    		</dependency>

    		<!-- MySQL database driver -->
    		<dependency>
    			<groupId>mysql</groupId>
    			<artifactId>mysql-connector-java</artifactId>
    			<version>${mysql.driver.version}</version>
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

    		<!-- Spring Batch unit test -->
    		<dependency>
    			<groupId>org.springframework.batch</groupId>
    			<artifactId>spring-batch-test</artifactId>
    			<version>${spring.batch.version}</version>
    		</dependency>

    		<!-- Junit -->
    		<dependency>
    			<groupId>junit</groupId>
    			<artifactId>junit</artifactId>
    			<version>${junit.version}</version>
    			<scope>test</scope>
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

## 4\. Spring Batch Jobs

A CSV file.

report.csv

    1001,"213,100",980,"mkyong", 29/7/2013
    1002,"320,200",1080,"staff 1", 30/7/2013
    1003,"342,197",1200,"staff 2", 31/7/2013

A Spring batch job, to read above csv file with `FlatFileItemReader`, process the data with `itemProcessor` and write it to an XML file  
with `StaxEventItemWriter`.

job-hello-world.xml

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

    	<bean id="report" class="com.mkyong.model.Report" scope="prototype" />
    	<bean id="itemProcessor" class="com.mkyong.CustomItemProcessor" />

    	<batch:job id="helloWorldJob">
    	  <batch:step id="step1">
    		<batch:tasklet>
    			<batch:chunk reader="cvsFileItemReader" writer="xmlItemWriter"
                                  processor="itemProcessor" commit-interval="10">
    			</batch:chunk>
    		</batch:tasklet>
    	  </batch:step>
    	</batch:job>

    	<bean id="cvsFileItemReader" class="org.springframework.batch.item.file.FlatFileItemReader">

    		<property name="resource" value="classpath:cvs/input/report.csv" />

    		<property name="lineMapper">
    		    <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
    			<property name="lineTokenizer">
    				<bean
    					class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    					<property name="names" value="id,sales,qty,staffName,date" />
    				</bean>
    			</property>
    			<property name="fieldSetMapper">
    				<bean class="com.mkyong.ReportFieldSetMapper" />

    				 <!-- if no data type conversion, use BeanWrapperFieldSetMapper to map by name
    				<bean
    					class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    					<property name="prototypeBeanName" value="report" />
    				</bean>
    				 -->
    			</property>
    		    </bean>
    		</property>

    	</bean>

    	<bean id="xmlItemWriter" class="org.springframework.batch.item.xml.StaxEventItemWriter">
    		<property name="resource" value="file:xml/outputs/report.xml" />
    		<property name="marshaller" ref="reportMarshaller" />
    		<property name="rootTagName" value="report" />
    	</bean>

    	<bean id="reportMarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
    	   <property name="classesToBeBound">
    		<list>
    			<value>com.mkyong.model.Report</value>
    		</list>
    	    </property>
    	</bean>

    </beans>

Map CSV value to `Report` object and write it to XML file (via jaxb annotations).

Report.java

    package com.mkyong.model;

    import java.math.BigDecimal;
    import java.util.Date;
    import javax.xml.bind.annotation.XmlAttribute;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name = "record")
    public class Report {

    	private int id;
    	private BigDecimal sales;
    	private int qty;
    	private String staffName;
    	private Date date;

    	@XmlAttribute(name = "id")
    	public int getId() {
    		return id;
    	}

    	public void setId(int id) {
    		this.id = id;
    	}

    	@XmlElement(name = "sales")
    	public BigDecimal getSales() {
    		return sales;
    	}

    	public void setSales(BigDecimal sales) {
    		this.sales = sales;
    	}

    	@XmlElement(name = "qty")
    	public int getQty() {
    		return qty;
    	}

    	public void setQty(int qty) {
    		this.qty = qty;
    	}

    	@XmlElement(name = "staffName")
    	public String getStaffName() {
    		return staffName;
    	}

    	public void setStaffName(String staffName) {
    		this.staffName = staffName;
    	}

    	public Date getDate() {
    		return date;
    	}

    	public void setDate(Date date) {
    		this.date = date;
    	}

    	@Override
    	public String toString() {
    		return "Report [id=" + id + ", sales=" + sales
                        + ", qty=" + qty + ", staffName=" + staffName + "]";
    	}

    }

To convert a `Date`, you need a custom `FieldSetMapper`. If no data type conversion, just use `BeanWrapperFieldSetMapper`to map the values by name automatically.

ReportFieldSetMapper.java

    package com.mkyong;

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import org.springframework.batch.item.file.mapping.FieldSetMapper;
    import org.springframework.batch.item.file.transform.FieldSet;
    import org.springframework.validation.BindException;

    import com.mkyong.model.Report;

    public class ReportFieldSetMapper implements FieldSetMapper<Report> {

    	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    	@Override
    	public Report mapFieldSet(FieldSet fieldSet) throws BindException {

    		Report report = new Report();
    		report.setId(fieldSet.readInt(0));
    		report.setSales(fieldSet.readBigDecimal(1));
    		report.setQty(fieldSet.readInt(2));
    		report.setStaffName(fieldSet.readString(3));

    		//default format yyyy-MM-dd
    		//fieldSet.readDate(4);
    		String date = fieldSet.readString(4);
    		try {
    			report.setDate(dateFormat.parse(date));
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}

    		return report;

    	}

    }

A itemProcessor will be fired before itemWriter.

CustomItemProcessor.java

    package com.mkyong;

    import org.springframework.batch.item.ItemProcessor;
    import com.mkyong.model.Report;

    public class CustomItemProcessor implements ItemProcessor<Report, Report> {

    	@Override
    	public Report process(Report item) throws Exception {

    		System.out.println("Processing..." + item);
    		return item;
    	}

    }

Spring context and database configuration.

context.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
    		http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

    	<!-- stored job-meta in memory -->
    	<!--
    	<bean id="jobRepository"
    		class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean">
    		<property name="transactionManager" ref="transactionManager" />
    	</bean>
     	 -->

     	 <!-- stored job-meta in database -->
    	<bean id="jobRepository"
    		class="org.springframework.batch.core.repository.support.JobRepositoryFactoryBean">
    		<property name="dataSource" ref="dataSource" />
    		<property name="transactionManager" ref="transactionManager" />
    		<property name="databaseType" value="mysql" />
    	</bean>

    	<bean id="transactionManager"
    		class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />

    	<bean id="jobLauncher"
    		class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
    		<property name="jobRepository" ref="jobRepository" />
    	</bean>

    </beans>

database.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		http://www.springframework.org/schema/jdbc
    		http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd">

            <!-- connect to MySQL database -->
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

5\. Run It

The most simplest way to run a batch job.

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
    		{
    			"spring/batch/jobs/job-hello-world.xml"
    		};

    	ApplicationContext context =
    			new ClassPathXmlApplicationContext(springConfig);

    	JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    	Job job = (Job) context.getBean("helloWorldJob");

    	try {

    		JobExecution execution = jobLauncher.run(job, new JobParameters());
    		System.out.println("Exit Status : " + execution.getStatus());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	System.out.println("Done");

      }
    }

Output

report.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <report>
    	<record id="1001">
    		<date>2013-07-29T00:00:00+08:00</date>
    		<qty>980</qty>
    		<sales>213100</sales>
    		<staffName>mkyong</staffName>
    	</record>
    	<record id="1002">
    		<date>2013-07-30T00:00:00+08:00</date>
    		<qty>1080</qty>
    		<sales>320200</sales>
    		<staffName>staff 1</staffName>
    	</record>
    	<record id="1003">
    		<date>2013-07-31T00:00:00+08:00</date>
    		<qty>1200</qty>
    		<sales>342197</sales>
    		<staffName>staff 2</staffName>
    	</record>
    </report>

In console

    Jul 30, 2013 11:52:00 PM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=helloWorldJob]] launched with the following parameters: [{}]
    Jul 30, 2013 11:52:00 PM org.springframework.batch.core.job.SimpleStepHandler handleStep
    INFO: Executing step: [step1]
    Processing...Report [id=1001, sales=213100, qty=980, staffName=mkyong]
    Processing...Report [id=1002, sales=320200, qty=1080, staffName=staff 1]
    Processing...Report [id=1003, sales=342197, qty=1200, staffName=staff 2]
    Jul 30, 2013 11:52:00 PM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=helloWorldJob]] completed with the following parameters: [{}] and the following status: [COMPLETED]
    Exit Status : COMPLETED
    Done

[http://www.mkyong.com/spring-batch/spring-batch-hello-world-example/](http://www.mkyong.com/spring-batch/spring-batch-hello-world-example/)
