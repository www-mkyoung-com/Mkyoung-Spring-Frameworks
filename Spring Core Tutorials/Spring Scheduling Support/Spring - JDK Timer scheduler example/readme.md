In this example, you will use Spring’s Scheduler API to schedule a task.

## 1\. Scheduler Task

Create a scheduler task…

    package com.mkyong.common;

    public class RunMeTask
    {
    	public void printMe() {
    		System.out.println("Run Me ~");
    	}
    }

    <bean id="runMeTask" class="com.mkyong.common.RunMeTask" />

Spring comes with a **MethodInvokingTimerTaskFactoryBean** as a replacement for the JDK TimerTask. You can define your target scheduler object and method to call here.

    <bean id="schedulerTask"
      class="org.springframework.scheduling.timer.MethodInvokingTimerTaskFactoryBean">
    	<property name="targetObject" ref="runMeTask" />
    	<property name="targetMethod" value="printMe" />
    </bean>

Spring comes with a **ScheduledTimerTask** as a replacement for the JDK Timer. You can pass your scheduler name, delay and period here.

    <bean id="timerTask"
    	class="org.springframework.scheduling.timer.ScheduledTimerTask">
    	<property name="timerTask" ref="schedulerTask" />
    	<property name="delay" value="1000" />
    	<property name="period" value="60000" />
    </bean>

## 2\. TimerFactoryBean

In last, you can configure a TimerFactoryBean bean to start your scheduler task.

    <bean class="org.springframework.scheduling.timer.TimerFactoryBean">
    	<property name="scheduledTimerTasks">
    		<list>
    			<ref local="timerTask" />
    		</list>
    	</property>
    </bean>

_File : Spring-Scheduler.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean id="schedulerTask"
      class="org.springframework.scheduling.timer.MethodInvokingTimerTaskFactoryBean">
    	<property name="targetObject" ref="runMeTask" />
    	<property name="targetMethod" value="printMe" />
    </bean>

    <bean id="runMeTask" class="com.mkyong.common.RunMeTask" />

    <bean id="timerTask"
    	class="org.springframework.scheduling.timer.ScheduledTimerTask">
    	<property name="timerTask" ref="schedulerTask" />
    	<property name="delay" value="1000" />
    	<property name="period" value="60000" />
    </bean>

    <bean class="org.springframework.scheduling.timer.TimerFactoryBean">
    	<property name="scheduledTimerTasks">
    		<list>
    			<ref local="timerTask" />
    		</list>
    	</property>
    </bean>

    </beans>

Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
    		  new ClassPathXmlApplicationContext("Spring-Scheduler.xml");
        }
    }

No code need to call the scheduler task, the **TimerFactoryBean** will run your schedule task during start up. As result, Spring scheduler will run the printMe() method every 60 seconds, with a 1 second delay for the first time of execution.

[http://www.mkyong.com/spring/spring-jdk-timer-scheduler-example/](http://www.mkyong.com/spring/spring-jdk-timer-scheduler-example/)
