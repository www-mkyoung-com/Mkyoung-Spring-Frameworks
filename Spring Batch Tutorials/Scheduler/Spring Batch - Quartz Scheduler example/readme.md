In this tutorial, we will show you how to use the Quartz scheduler framework to schedule a Spring batch job to run every 10 seconds.

Tools and libraries used

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring Batch 2.2.0.RELEASE
6.  Quartz 1.8.6

The relationship looks like the following :

    Spring Batch <--> Spring QuartzJobBean <--> Quartz Frameworks

The `QuartzJobBean` is acts like a bridge between Spring batch and Quartz frameworks.

## 1\. Project Dependencies

Spring need `spring-context-support` to support Quartz scheduler.

pom.xml

    <project ...
      <properties>
    	<jdk.version>1.6</jdk.version>
    	<spring.version>3.2.2.RELEASE</spring.version>
    	<spring.batch.version>2.2.0.RELEASE</spring.batch.version>
    	<quartz.version>1.8.6</quartz.version>
      </properties>

      <dependencies>

    	<!-- Spring Core -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- QuartzJobBean in spring-context-support.jar -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-context-support</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- Quartz framework -->
    	<dependency>
    		<groupId>org.quartz-scheduler</groupId>
    		<artifactId>quartz</artifactId>
    		<version>${quartz.version}</version>
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

      </dependencies>
    </project>

## 2\. Spring Batch Jobs

A batch job to read a csv file and print out the content via a custom writer. Few points to highlight :

1\. Configure the `JobRegistryBeanPostProcessor` bean, it registers `Job` beans with `JobRegistry`, so that `QuartzJobBean`is able to get the `Job` bean via `JobRegister` (JobLocator).  
2\. The `JobLauncherDetails` is extended `QuartzJobBean`, acts as a bridge between Spring batch and Quartz.

job-quartz.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:batch="http://www.springframework.org/schema/batch"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/batch
    	http://www.springframework.org/schema/batch/spring-batch-2.2.xsd
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    	">

      <!-- spring batch context -->
      <bean id="jobRepository"
    	class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean">
    	<property name="transactionManager" ref="transactionManager" />
      </bean>

      <bean id="transactionManager"
    	class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />

      <bean id="jobLauncher"
    	class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
    	<property name="jobRepository" ref="jobRepository" />
      </bean>

      <bean
    	class="org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor">
    	<property name="jobRegistry" ref="jobRegistry" />
      </bean>

      <bean id="jobRegistry"
    	class="org.springframework.batch.core.configuration.support.MapJobRegistry" />
      <!-- spring batch context -->

      <bean id="report" class="com.mkyong.model.Report" scope="prototype" />

      <bean id="customWriter" class="com.mkyong.writers.CustomWriter" />

      <batch:job id="reportJob">
    	<batch:step id="step1">
    	  <batch:tasklet>
    		<batch:chunk reader="cvsFileItemReader" writer="customWriter"
    			commit-interval="10">
    		</batch:chunk>
    	  </batch:tasklet>
    	</batch:step>
      </batch:job>

      <bean id="cvsFileItemReader" class="org.springframework.batch.item.file.FlatFileItemReader">

    	<!-- Read a csv file -->
    	<property name="resource" value="classpath:cvs/input/report.csv" />

    	<property name="lineMapper">
    	  <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
    		<property name="lineTokenizer">
    		  <bean
    			class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    			<property name="names" value="id,impressions" />
    		  </bean>
    		</property>
    		<property name="fieldSetMapper">
    		  <bean
    			class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    			<property name="prototypeBeanName" value="report" />
    		  </bean>
    		</property>
    	  </bean>
    	</property>

      </bean>

      <!-- run every 10 seconds -->
      <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    	<property name="triggers">
    	  <bean id="cronTrigger" class="org.springframework.scheduling.quartz.CronTriggerBean">
    		<property name="jobDetail" ref="jobDetail" />
    		<property name="cronExpression" value="*/10 * * * * ?" />
    	  </bean>
    	</property>
      </bean>

      <bean id="jobDetail" class="org.springframework.scheduling.quartz.JobDetailBean">
    	<property name="jobClass" value="com.mkyong.quartz.JobLauncherDetails" />
    	<property name="group" value="quartz-batch" />
    	<property name="jobDataAsMap">
    	  <map>
    		<entry key="jobName" value="reportJob" />
    		<entry key="jobLocator" value-ref="jobRegistry" />
    		<entry key="jobLauncher" value-ref="jobLauncher" />
    		<entry key="param1" value="mkyong1" />
    		<entry key="param2" value="mkyong2" />
    	  </map>
    	</property>
      </bean>

    </beans>

## 3\. QuartzJobBean Example

This class is copied from [Spring batch sample Github repository](https://github.com/SpringSource/spring-batch/blob/master/spring-batch-samples/src/main/java/org/springframework/batch/sample/quartz/JobLauncherDetails.java), with minor change to run the completed job by passing a `new Date()` each time the job is running.

JobLauncherDetails.java

    package com.mkyong.quartz;

    import java.util.Date;
    import java.util.Map;
    import java.util.Map.Entry;

    import org.quartz.JobExecutionContext;
    import org.springframework.batch.core.JobExecutionException;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.JobParametersBuilder;
    import org.springframework.batch.core.configuration.JobLocator;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.scheduling.quartz.QuartzJobBean;

    public class JobLauncherDetails extends QuartzJobBean {

      static final String JOB_NAME = "jobName";

      private JobLocator jobLocator;

      private JobLauncher jobLauncher;

      public void setJobLocator(JobLocator jobLocator) {
    	this.jobLocator = jobLocator;
      }

      public void setJobLauncher(JobLauncher jobLauncher) {
    	this.jobLauncher = jobLauncher;
      }

      @SuppressWarnings("unchecked")
      protected void executeInternal(JobExecutionContext context) {

    	Map<String, Object> jobDataMap = context.getMergedJobDataMap();

    	String jobName = (String) jobDataMap.get(JOB_NAME);

    	JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

    	try {
    		jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
    	} catch (JobExecutionException e) {
    		e.printStackTrace();
    	}
      }

      //get params from jobDataAsMap property, job-quartz.xml
      private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

    	JobParametersBuilder builder = new JobParametersBuilder();

    	for (Entry<String, Object> entry : jobDataMap.entrySet()) {
    		String key = entry.getKey();
    		Object value = entry.getValue();
    		if (value instanceof String && !key.equals(JOB_NAME)) {
    			builder.addString(key, (String) value);
    		} else if (value instanceof Float || value instanceof Double) {
    			builder.addDouble(key, ((Number) value).doubleValue());
    		} else if (value instanceof Integer || value instanceof Long) {
    			builder.addLong(key, ((Number) value).longValue());
    		} else if (value instanceof Date) {
    			builder.addDate(key, (Date) value);
    		} else {
    			// JobDataMap contains values which are not job parameters
    			// (ignoring)
    		}
    	}

    	//need unique job parameter to rerun the completed job
    	builder.addDate("run date", new Date());

    	return builder.toJobParameters();

      }

    }

## 4\. Run It

Loads the Spring application context, the Quartz scheduler will run the “reportJob” every 10 seconds.

App.java

    package com.mkyong;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {

      public static void main(String[] args) {

    	String springConfig = "spring/batch/jobs/job-quartz.xml";

    	ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

      }
    }

[http://www.mkyong.com/spring-batch/spring-batch-and-quartz-scheduler-example/](http://www.mkyong.com/spring-batch/spring-batch-and-quartz-scheduler-example/)
