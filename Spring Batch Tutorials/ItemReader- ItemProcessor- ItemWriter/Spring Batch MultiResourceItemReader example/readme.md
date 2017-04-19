In this tutorial, we will show you how to read items from multiple resources (multiple csv files), and write the items into a single csv file.

Tools and libraries used

1.  Maven 3
2.  Eclipse 4.2
3.  JDK 1.6
4.  Spring Core 3.2.2.RELEASE
5.  Spring Batch 2.2.0.RELEASE

_P.S This example – 3 CSV files (reader) – combine into a single CSV file (writer)._

## 1\. Project Directory Structure

Review the final project structure, a standard Maven project.

![spring-batch-MultiResourceItemReader-example](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-MultiResourceItemReader-example.png)

## 2\. Multiple CSV Files

There are 3 csv files, later we will use `MultiResourceItemReader` to read it one by one.

csv/inputs/domain-1-3-2013.csv

    1,facebook.com
    2,yahoo.com
    3,google.com

csv/inputs/domain-2-3-2013.csv

    200,mkyong.com
    300,stackoverflow.com
    400,oracle.com

csv/inputs/domain-3-3-2013.csv

    999,eclipse.org
    888,baidu.com

## 3\. Spring Batch Jobs

A job to read resources that matches this pattern `csv/inputs/domain-*.csv`, and write it into a single cvs file `domain.all.csv`.

resources/spring/batch/jobs/job-read-files.xml

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

      <bean id="domain" class="com.mkyong.Domain" />

      <job id="readMultiFileJob" xmlns="http://www.springframework.org/schema/batch">
        <step id="step1">
    	<tasklet>
    		<chunk reader="multiResourceReader" writer="flatFileItemWriter"
    			commit-interval="1" />
    	</tasklet>
        </step>
      </job>

      <bean id="multiResourceReader"
    	class=" org.springframework.batch.item.file.MultiResourceItemReader">
    	<property name="resources" value="file:csv/inputs/domain-*.csv" />
    	<property name="delegate" ref="flatFileItemReader" />
      </bean>

      <bean id="flatFileItemReader"
            class="org.springframework.batch.item.file.FlatFileItemReader">
    	<property name="lineMapper">
    	  <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
    		<property name="lineTokenizer">
    		    <bean
                        class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    				<property name="names" value="id, domain" />
    		    </bean>
    		</property>
    		<property name="fieldSetMapper">
    		    <bean
    		    class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    				<property name="prototypeBeanName" value="domain" />
    		    </bean>
    		</property>
    	  </bean>
    	</property>
      </bean>

      <bean id="flatFileItemWriter"
              class="org.springframework.batch.item.file.FlatFileItemWriter">
    	<property name="resource" value="file:csv/outputs/domain.all.csv" />
    	<property name="appendAllowed" value="true" />
    	<property name="lineAggregator">
    	  <bean
    	  class="org.springframework.batch.item.file.transform.DelimitedLineAggregator">
    		<property name="delimiter" value="," />
    		<property name="fieldExtractor">
    		  <bean
                      class="org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor">
    			<property name="names" value="id, domain" />
    		  </bean>
    		</property>
    	  </bean>
    	</property>
      </bean>

    </beans>

resources/spring/batch/config/context.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

        <!-- stored job-meta in memory -->
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

    </beans>

## 4\. Run It

Create a Java class and run the batch job.

App.java

    package com.mkyong;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {

      public static void main(String[] args) {
    	App obj = new App();
    	obj.run();
      }

      private void run() {

    	String[] springConfig = { "spring/batch/jobs/job-read-files.xml" };

    	ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    	JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    	Job job = (Job) context.getBean("readMultiFileJob");

    	try {

    		JobExecution execution = jobLauncher.run(job, new JobParameters());
    		System.out.println("Exit Status : " + execution.getStatus());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	System.out.println("Done");

      }

    }

Output. The content of three csv files is read and combine into a single csv file.

csv/outputs/domain.all.csv

    1,facebook.com
    2,yahoo.com
    3,google.coms
    200,mkyong.com
    300,stackoverflow.com
    400,oracle.com
    999,eclipse.org
    888,baidu.com

[http://www.mkyong.com/spring-batch/spring-batch-multiresourceitemreader-example/](http://www.mkyong.com/spring-batch/spring-batch-multiresourceitemreader-example/)
