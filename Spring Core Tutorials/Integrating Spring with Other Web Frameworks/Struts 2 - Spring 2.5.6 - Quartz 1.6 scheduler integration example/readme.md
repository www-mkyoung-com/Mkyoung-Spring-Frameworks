Struts 2 + Spring 2.5.6 + Quartz scheduler integration example

In this tutorial, we will show you how to integrate **Struts 2 + Spring 2.5.6 + Quartz 1.6.5** scheduler together. The relationship look like this :

    Struts 2 <-- Plugin --> Spring <--(Helper)--> Quartz <---> Scheduler task

Tools used

1.  Spring 2.5.6
2.  Quartz 1.6.3
3.  Struts 2.1.8
4.  Struts2-spring-plugin 2.1.8
5.  Maven 2
6.  Eclipse 3.6

**Note**  
You may also interest at this – [Struts 2 + Spring 3 + Quartz 1.8.6 integration example.](http://www.mkyong.com/struts2/struts-2-spring-3-quartz-1-8-scheduler-example/).

## 1\. Project folder

Here’s the project folder structure.

![Struts 2 Spring Quartz integration example](http://www.mkyong.com/wp-content/uploads/2010/07/Struts2-Spring-Quartz-Integration-Example.jpg)

## 2\. Dependency Libraries

Get all the dependency libraries, you need Spring, Struts2, Strut2-Spring-Plugin and Quartz jar file.

_File : pom.xml_

    ...
      <dependencies>

    <!-- Struts 2 -->
    <dependency>
              <groupId>org.apache.struts</groupId>
      <artifactId>struts2-core</artifactId>
      <version>2.1.8</version>
            </dependency>

    <!-- Quartz framework -->
    <dependency>
              <groupId>opensymphony</groupId>
      <artifactId>quartz</artifactId>
      <version>1.6.3</version>
    </dependency>

    <!-- Quartz dependency library-->
    <dependency>
      <groupId>commons-collections</groupId>
      <artifactId>commons-collections</artifactId>
      <version>3.2.1</version>
    </dependency>

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

    <!-- Struts 2 + Spring plugins -->
    <dependency>
              <groupId>org.apache.struts</groupId>
      <artifactId>struts2-spring-plugin</artifactId>
      <version>2.1.8</version>
            </dependency>

      </dependencies>
      ...

## 3\. Scheduler Task

Put all the scheduler logic in this class.

_File : SchedulerTask.java_

    package com.mkyong.quartz;

    public class SchedulerTask {

       public void printSchedulerMessage() {

    	   System.out.println("Struts 2 + Spring + Quartz ......");

       }
    }

## 4\. Spring + Quartz

To integrate Spring and Quartz, create a class extends the `org.springframework.scheduling.quartz.QuartzJobBean`, reference the scheduler task (**SchedulerTask.java**) via a setter method, and put the scheduler logic inside the `executeInternal()` method.

_File : SchedulerJob.java_

    package com.mkyong.quartz;

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

    		schedulerTask.printSchedulerMessage();

    	}
    }

_File : applicationContext.xml_ – Create an `applicationContext.xml` file, put all the **Spring + Quartz** integration stuffs inside. Read XML comments for detail.

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       <!-- Scheduler task -->
       <bean name="schedulerTask" class="com.mkyong.quartz.SchedulerTask" />

       <!-- Scheduler job -->
       <bean name="schedulerJob"
         class="org.springframework.scheduling.quartz.JobDetailBean">

         <property name="jobClass" value="com.mkyong.quartz.SchedulerJob" />

         <property name="jobDataAsMap">
    	 <map>
    	    <entry key="schedulerTask" value-ref="schedulerTask" />
    	 </map>
          </property>
       </bean>

       <!-- Cron Trigger, run every 10 seconds -->
       <bean id="cronTrigger"
    	class="org.springframework.scheduling.quartz.CronTriggerBean">

    	<property name="jobDetail" ref="schedulerJob" />
    	<property name="cronExpression" value="0/10 * * * * ?" />

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

## 5\. Struts 2 + Spring

To integrate **Struts 2 + Spring**, just put the `org.springframework.web.context.ContextLoaderListener` listener class in the `web.xml` file.

_File : web.xml_

    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >

    <web-app>
      <display-name>Struts 2 Web Application</display-name>

      <filter>
    	<filter-name>struts2</filter-name>
    	<filter-class>
    	  org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter
    	</filter-class>
      </filter>

      <filter-mapping>
    	<filter-name>struts2</filter-name>
    	<url-pattern>/*</url-pattern>
      </filter-mapping>

      <listener>
        <listener-class>
    	  org.springframework.web.context.ContextLoaderListener
    	</listener-class>
      </listener>

    </web-app>

## 6\. Demo

When Strut2 is started, it calls Spring and run the defined Quartz’s job – call `SchedulerTask.printSchedulerMessage()` at every 10 seconds.

    INFO: ... initialized Struts-Spring integration successfully
    16 Julai 2010 12:51:38 PM org.apache.coyote.http11.Http11Protocol start
    INFO: Starting Coyote HTTP/1.1 on http-8080
    16 Julai 2010 12:51:38 PM org.apache.jk.common.ChannelSocket init
    INFO: JK: ajp13 listening on /0.0.0.0:8009
    16 Julai 2010 12:51:38 PM org.apache.jk.server.JkMain start
    INFO: Jk running ID=0 time=0/21  config=null
    16 Julai 2010 12:51:38 PM org.apache.catalina.startup.Catalina start
    INFO: Server startup in 2110 ms
    Struts 2 + Spring + Quartz ......
    Struts 2 + Spring + Quartz ......
    Struts 2 + Spring + Quartz ......
    Struts 2 + Spring + Quartz ......
    Struts 2 + Spring + Quartz ......

[http://www.mkyong.com/struts2/struts-2-spring-quartz-scheduler-integration-example/](http://www.mkyong.com/struts2/struts-2-spring-quartz-scheduler-integration-example/)
