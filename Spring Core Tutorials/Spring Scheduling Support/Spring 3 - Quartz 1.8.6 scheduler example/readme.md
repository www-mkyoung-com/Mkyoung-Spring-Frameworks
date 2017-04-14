**Updated on 25 July 2012** – Upgrade article to use Spring 3 and Quartz 1.8.6 (it was Spring 2.5.6 and Quartz 1.6)

In this tutorial, we will show you how to integrate Spring with Quartz scheduler framework. Spring comes with many handy classes to support Quartz, and decouple your class to Quartz APIs.

Tools Used :

1.  Spring 3.1.2.RELEASE
2.  Quartz 1.8.6
3.  Eclipse 4.2
4.  Maven 3

**Why NOT Quartz 2?**  
Currently, Spring 3 is still NOT support Quartz 2 APIs, see this [SPR-8581 bug report](https://jira.springsource.org/browse/SPR-8581). Will update this article again once bug fixed is released.

## 1\. Project Dependency

You need following dependencies to integrate Spring 3 and Quartz 1.8.6

_File : pom.xml_

    ...
    <dependencies>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>3.1.2.RELEASE</version>
    	</dependency>

    	<!-- QuartzJobBean in spring-context-support.jar -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-context-support</artifactId>
    		<version>3.1.2.RELEASE</version>
    	</dependency>

    	<!-- Spring + Quartz need transaction -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-tx</artifactId>
    		<version>3.1.2.RELEASE</version>
    	</dependency>

    	<!-- Quartz framework -->
    	<dependency>
    		<groupId>org.quartz-scheduler</groupId>
    		<artifactId>quartz</artifactId>
    		<version>1.8.6</version>
    	</dependency>

    </dependencies>
    ...

## 2\. Scheduler Task

Create a normal Java class, this is the class you want to schedule in Quartz.

_File : RunMeTask.java_

    package com.mkyong.common;

    public class RunMeTask {
    	public void printMe() {
    		System.out.println("Spring 3 + Quartz 1.8.6 ~");
    	}
    }

## 3\. Declare Quartz Scheduler Job

With Spring, you can declare Quartz job in two ways :

**3.1 MethodInvokingJobDetailFactoryBean**  
This is the simplest and straightforward method, suitable for simple scheduler.

    <bean id="runMeJob"
     	class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">

    	<property name="targetObject" ref="runMeTask" />
    	<property name="targetMethod" value="printMe" />

    </bean>

**3.2 JobDetailBean**  
The `QuartzJobBean` is more flexible and suitable for complex scheduler. You need to create a class extends the Spring’s `QuartzJobBean`, and define the method you want to schedule in `executeInternal()` method, and pass the scheduler task (RunMeTask) via setter method.

_File : RunMeJob.java_

    package com.mkyong.common;

    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    import org.springframework.scheduling.quartz.QuartzJobBean;

    public class RunMeJob extends QuartzJobBean {
    	private RunMeTask runMeTask;

    	public void setRunMeTask(RunMeTask runMeTask) {
    		this.runMeTask = runMeTask;
    	}

    	protected void executeInternal(JobExecutionContext context)
    		throws JobExecutionException {

    		runMeTask.printMe();

    	}
    }

Configure the target class via `jobClass` and method to run via `jobDataAsMap`.

    <bean name="runMeJob" class="org.springframework.scheduling.quartz.JobDetailBean">

    	<property name="jobClass" value="com.mkyong.common.RunMeJob" />

    	<property name="jobDataAsMap">
    		<map>
    			<entry key="runMeTask" value-ref="runMeTask" />
    		</map>
    	</property>

    </bean>

## 4\. Trigger

Configure Quartz trigger to define when will run your scheduler job. Two type of triggers are supported :

**4.1 SimpleTrigger**  
It allows to set the start time, end time, repeat interval to run your job.

    <!-- Simple Trigger, run every 5 seconds -->
    <bean id="simpleTrigger"
                    class="org.springframework.scheduling.quartz.SimpleTriggerBean">

    	<property name="jobDetail" ref="runMeJob" />
    	<property name="repeatInterval" value="5000" />
    	<property name="startDelay" value="1000" />

    </bean>

**4.2 CronTrigger**  
It allows Unix cron expression to specify the dates and times to run your job.

    <!-- Cron Trigger, run every 5 seconds -->
    <bean id="cronTrigger"
    	class="org.springframework.scheduling.quartz.CronTriggerBean">

    	<property name="jobDetail" ref="runMeJob" />
    	<property name="cronExpression" value="0/5 * * * * ?" />

    </bean>

**Note**  
The Unix cron expression is highly flexible and powerful, read more in following websites :

1.  [http://en.wikipedia.org/wiki/CRON_expression](http://en.wikipedia.org/wiki/CRON_expression)
2.  [http://www.quartz-scheduler.org/docs/examples/Example3.html](http://www.quartz-scheduler.org/docs/examples/Example3.html)

## 5\. Scheduler Factory

Create a Scheduler factory bean to integrate both job detail and trigger together.

    <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="jobDetails">
       <list>
          <ref bean="runMeJob" />
       </list>
    </property>

    <property name="triggers">
        <list>
    	<ref bean="simpleTrigger" />
        </list>
    </property>
       </bean>

## 6\. Spring Bean Configuration File

Complete Spring’s bean configuration file.

_File : Spring-Quartz.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="runMeTask" class="com.mkyong.common.RunMeTask" />

    	<!-- Spring Quartz -->
    	<bean name="runMeJob" class="org.springframework.scheduling.quartz.JobDetailBean">

    		<property name="jobClass" value="com.mkyong.common.RunMeJob" />

    		<property name="jobDataAsMap">
    		  <map>
    			<entry key="runMeTask" value-ref="runMeTask" />
    		  </map>
    		</property>

    	</bean>

    	<!--
    	<bean id="runMeJob"
                class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
    		<property name="targetObject" ref="runMeTask" />
    		<property name="targetMethod" value="printMe" />
    	</bean>
    	-->

    	<!-- Simple Trigger, run every 5 seconds -->
    	<bean id="simpleTrigger"
                    class="org.springframework.scheduling.quartz.SimpleTriggerBean">

    		<property name="jobDetail" ref="runMeJob" />
    		<property name="repeatInterval" value="5000" />
    		<property name="startDelay" value="1000" />

    	</bean>

    	<!-- Cron Trigger, run every 5 seconds -->
    	<bean id="cronTrigger"
                    class="org.springframework.scheduling.quartz.CronTriggerBean">

    		<property name="jobDetail" ref="runMeJob" />
    		<property name="cronExpression" value="0/5 * * * * ?" />

    	</bean>

    	<bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    		<property name="jobDetails">
    			<list>
    				<ref bean="runMeJob" />
    			</list>
    		</property>

    		<property name="triggers">
    			<list>
    				<ref bean="simpleTrigger" />
    			</list>
    		</property>
    	</bean>

    </beans>

## 7\. Demo

Run it ~

    package com.mkyong.common;

    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args ) throws Exception
        {
        	new ClassPathXmlApplicationContext("Spring-Quartz.xml");
        }
    }

Output to console.

    Jul 25, 2012 3:23:09 PM org.springframework.scheduling.quartz.SchedulerFactoryBean startScheduler
    INFO: Starting Quartz Scheduler now
    Spring 3 + Quartz 1.8.6 ~ //run every 5 seconds
    Spring 3 + Quartz 1.8.6 ~

[http://www.mkyong.com/spring/spring-quartz-scheduler-example/](http://www.mkyong.com/spring/spring-quartz-scheduler-example/)
