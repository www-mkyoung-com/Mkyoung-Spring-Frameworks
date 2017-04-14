In this tutorial, you will integrate Struts + Spring + Quartz framework together to perform a scheduler task. Spring comes with ready solution to integrate the Struts and Quartz easily. The relationship as following :

    Struts <--(Plug-In)--> Spring <--(Spring-Helper)--> Quartz <---> Scheduler task

Tools Used :

1.  Struts 1.3.10
2.  Spring 2.5.6
3.  Quartz 1.6.3

## 1\. Scheduler Task

Create a scheduler task, and the `printMessage()` is the method you want to schedule.

_File : SchedulerTask.java_

    package com.mkyong.common.quartz;

    public class SchedulerTask
    {
       public void printMessage() {
    	System.out.println("Struts + Spring + Quartz integration example ~");
       }
    }

## 2\. Scheduler Job

To integrate Spring with Quartz, create a SchedulerJob which extends the Spring’s `QuartzJobBean`, instead of the Quartz Job class.

_File : SchedulerJob.java_

    package com.mkyong.common.quartz;

    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    import org.springframework.scheduling.quartz.QuartzJobBean;

    public class SchedulerJob extends QuartzJobBean
    {
    	private SchedulerTask schedulerTask;

    	public void setSchedulerTask(SchedulerTask schedulerTask) {
    		this.schedulerTask = schedulerTask;
    	}

    	protected void executeInternal(JobExecutionContext context)
    	throws JobExecutionException {

    		schedulerTask.printMessage();

    	}
    }

## 3\. Spring’s Quartz Helper

Spring comes with many Quartz helper classes to simplify the overall Quartz scheduler processes – Scheduler, Trigget, Job and JobDetails.

_File : spring-scheduler.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

      <!-- Scheduler task -->
      <bean name="schedulerTask" class="com.mkyong.common.quartz.SchedulerTask" />

       <!-- Scheduler job -->
       <bean name="schedulerJob"
         class="org.springframework.scheduling.quartz.JobDetailBean">

         <property name="jobClass"
               value="com.mkyong.common.quartz.SchedulerJob" />

         <property name="jobDataAsMap">
    	<map>
    	   <entry key="schedulerTask" value-ref="schedulerTask" />
    	 </map>
          </property>

       </bean>

       <!-- Cron Trigger -->
       <bean id="cronTrigger"
    	class="org.springframework.scheduling.quartz.CronTriggerBean">

    	<property name="jobDetail" ref="schedulerJob" />
    	<property name="cronExpression" value="0/5 * * * * ?" />

       </bean>

       <!-- Scheduler -->
       <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    	<property name="jobDetails">
    	   <list>
    	      <ref bean="schedulerJob" />
    	   </list>
    	</property>

    	<property name="triggers">
    	    <list>
    		<ref bean="cronTrigger" />
    	    </list>
    	</property>
       </bean>

    </beans>

## 4\. Struts

To integrate Spring with Struts, you need to include the Spring’s `ContextLoaderPlugIn` into the Struts configuration file.

_File : struts-config.xml_

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE struts-config PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN"
    "http://jakarta.apache.org/struts/dtds/struts-config_1_3.dtd">

    <struts-config>

       <action-mappings>

        <action
    	path="/Welcome"
    	type="org.apache.struts.actions.ForwardAction"
    	parameter="/pages/quartz_started.jsp"/>

       </action-mappings>

       <!-- Spring Struts plugin -->
       <plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
    	<set-property property="contextConfigLocation"
    	value="/WEB-INF/spring-scheduler.xml" />
        </plug-in>

    </struts-config>

## 5\. How It Works

During Struts initialization, it will start the Spring Ioc container via the Spring’s `ContextLoaderPlugIn` Struts plug-in; While Spring initialization, it will start the Quartz scheduled job automatically.

In this example, the `printMessage()` method will be executed in every 5 seconds.

[http://www.mkyong.com/struts/struts-spring-quartz-scheduler-integration-example/](http://www.mkyong.com/struts/struts-spring-quartz-scheduler-integration-example/)
