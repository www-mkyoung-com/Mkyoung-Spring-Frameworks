In this tutorial, we will show you how to use **Spring TaskScheduler** to schedule a batch job to run every 5 seconds.

Tools and libraries used

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring Batch 2.2.0.RELEASE

## 1\. Project Directory Structure

A standard Maven project.

![spring-batch-taskscheduler](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-taskscheduler.png)

## 2\. Spring TaskScheduler

Spring 3.0 introduces a `TaskScheduler` for scheduling tasks. It’s part of the Spring-Core, no need to declare an extra dependency.

    <task:scheduled-tasks>
    <task:scheduled ref="runScheduler" method="run" fixed-delay="5000" />
      </task:scheduled-tasks>

      <task:scheduled-tasks>
    <task:scheduled ref="runScheduler" method="run" cron="*/5 * * * * *" />
      </task:scheduled-tasks>

The `TaskScheduler` will schedule to run below bean.

RunScheduler.java

    package com.mkyong;

    import java.util.Date;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.JobParametersBuilder;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    @Component
    public class RunScheduler {

      @Autowired
      private JobLauncher jobLauncher;

      @Autowired
      private Job job;

      public void run() {

        try {

    	String dateParam = new Date().toString();
    	JobParameters param =
    	  new JobParametersBuilder().addString("date", dateParam).toJobParameters();

    	System.out.println(dateParam);

    	JobExecution execution = jobLauncher.run(job, param);
    	System.out.println("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
    	e.printStackTrace();
        }

      }
    }

_P.S JobParamater need to be unique each time a batch job to run, for testing purpose, we just pass in a `new Date()`everything running the job._

## 3\. Spring Batch Jobs

This job is just reading a csv file and display the value via a custom writer. Refer to the end of the file, we use `task:scheduled-tasks` to run this batch job every 5 seconds.

resources/spring/batch/jobs/job-report.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:batch="http://www.springframework.org/schema/batch"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:task="http://www.springframework.org/schema/task"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/batch
    	http://www.springframework.org/schema/batch/spring-batch-2.2.xsd
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-3.2.xsd
    	http://www.springframework.org/schema/task
            http://www.springframework.org/schema/task/spring-task-3.2.xsd
    	">

      <context:component-scan base-package="com.mkyong" />

      <!-- job context -->
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
      <!-- job context -->

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

      <bean id="cvsFileItemReader"
            class="org.springframework.batch.item.file.FlatFileItemReader">

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

      <bean id="runScheduler" class="com.mkyong.RunScheduler" />

      <!-- Run every 5 seconds -->
      <task:scheduled-tasks>
        <!--
    	<task:scheduled ref="runScheduler" method="run" fixed-delay="5000" />
        -->
    	<task:scheduled ref="runScheduler" method="run" cron="*/5 * * * * *" />
       </task:scheduled-tasks>

    </beans>

report.csv

    1,"139,237"
    2,"500,657"
    3,"342,100"

CustomWriter.java

    package com.mkyong.writers;

    import java.util.List;
    import org.springframework.batch.item.ItemWriter;
    import com.mkyong.model.Report;

    public class CustomWriter implements ItemWriter<Report> {

      @Override
      public void write(List<? extends Report> items) throws Exception {

    	System.out.println("writer..." + items.size());
    	for(Report item : items){
    		System.out.println(item);
    	}

      }

    }

## 4\. Run It

Loads the Spring application context, the scheduler will be run automatically.

App.java

    package com.mkyong;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {

      public static void main(String[] args) {

    	String springConfig = "spring/batch/jobs/job-report.xml";

    	ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

      }
    }

Output, it prints the csv content every 5 seconds.

    ......
    Sun Jul 28 11:20:30 MYT 2013
    Jul 28, 2013 11:20:30 AM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=reportJob]] launched with the following parameters: [{date=Sun Jul 28 11:20:30 MYT 2013}]
    Jul 28, 2013 11:20:30 AM org.springframework.batch.core.job.SimpleStepHandler handleStep
    INFO: Executing step: [step1]
    writer...3
    Report [id=1, Impressions=139,237]
    Report [id=2, Impressions=500,657]
    Report [id=3, Impressions=342,100]
    Jul 28, 2013 11:20:30 AM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=reportJob]] completed with the following parameters: [{date=Sun Jul 28 11:20:30 MYT 2013}] and the following status: [COMPLETED]
    Exit Status : COMPLETED

    Sun Jul 28 11:20:35 MYT 2013
    Jul 28, 2013 11:20:35 AM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=reportJob]] launched with the following parameters: [{date=Sun Jul 28 11:20:35 MYT 2013}]
    Jul 28, 2013 11:20:35 AM org.springframework.batch.core.job.SimpleStepHandler handleStep
    INFO: Executing step: [step1]
    writer...3
    Report [id=1, Impressions=139,237]
    Report [id=2, Impressions=500,657]
    Report [id=3, Impressions=342,100]
    Exit Status : COMPLETED
    Jul 28, 2013 11:20:35 AM org.springframework.batch.core.launch.support.SimpleJobLauncher$1 run
    INFO: Job: [FlowJob: [name=reportJob]] completed with the following parameters: [{date=Sun Jul 28 11:20:35 MYT 2013}] and the following status: [COMPLETED]
    ......

[http://www.mkyong.com/spring-batch/spring-batch-and-spring-taskscheduler-example/](http://www.mkyong.com/spring-batch/spring-batch-and-spring-taskscheduler-example/)
