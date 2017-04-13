In this tutorial, we show you how to convert last [Spring AOP + AspectJ annotation](http://www.mkyong.com/spring3/spring-aop-aspectj-annotation-example/) into XML based configuration.

For those don’t like annotation or using JDK 1.4, you can use AspectJ in XML based instead.

Review last customerBo interface again, with few methods, later you will learn how to intercept it via AspectJ in XML file.

    package com.mkyong.customer.bo;

    public interface CustomerBo {

    	void addCustomer();

    	String addCustomerReturnValue();

    	void addCustomerThrowException() throws Exception;

    	void addCustomerAround(String name);
    }

## 1\. AspectJ <aop:before> = @Before

AspectJ @Before example.

    package com.mkyong.aspect;

    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.Before;

    @Aspect
    public class LoggingAspect {

    	@Before("execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))")
    	public void logBefore(JoinPoint joinPoint) {
    		//...
    	}

    }

Equivalent functionality in XML, with** <aop:before>**.

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

      <aop:aspect id="aspectLoggging" ref="logAspect" >

         <!-- @Before -->
         <aop:pointcut id="pointCutBefore"
    	expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))" />

         <aop:before method="logBefore" pointcut-ref="pointCutBefore" />

      </aop:aspect>

    </aop:config>

## 2\. AspectJ <aop:after> = @After

AspectJ @After example.

    package com.mkyong.aspect;

    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.After;

    @Aspect
    public class LoggingAspect {

    	@After("execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))")
    	public void logAfter(JoinPoint joinPoint) {
    		//...
    	}

    }

Equivalent functionality in XML, with **<aop:after>**.

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

      <aop:aspect id="aspectLoggging" ref="logAspect" >

         <!-- @After -->
         <aop:pointcut id="pointCutAfter"
    	expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))" />

         <aop:after method="logAfter" pointcut-ref="pointCutAfter" />

      </aop:aspect>

    </aop:config>

## 3\. AspectJ <aop:after-returning> = @AfterReturning

AspectJ @AfterReturning example.

    package com.mkyong.aspect;

    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.AfterReturning;

    @Aspect
    public class LoggingAspect {

      @AfterReturning(
       pointcut = "execution(* com.mkyong.customer.bo.CustomerBo.addCustomerReturnValue(..))",
       returning= "result")
       public void logAfterReturning(JoinPoint joinPoint, Object result) {
    	//...
       }

    }

Equivalent functionality in XML, with **<aop:after-returning>**.

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

      <aop:aspect id="aspectLoggging" ref="logAspect" >

        <!-- @AfterReturning -->
        <aop:pointcut id="pointCutAfterReturning"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerReturnValue(..))" />

        <aop:after-returning method="logAfterReturning" returning="result"
          pointcut-ref="pointCutAfterReturning" />

      </aop:aspect>

    </aop:config>

## 4\. AspectJ <aop:after-throwing> = @AfterReturning

AspectJ @AfterReturning example.

    package com.mkyong.aspect;

    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.AfterThrowing;

    @Aspect
    public class LoggingAspect {

      @AfterThrowing(
       pointcut = "execution(* com.mkyong.customer.bo.CustomerBo.addCustomerThrowException(..))",
       throwing= "error")
      public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
    	//...
      }
    }

Equivalent functionality in XML, with **<aop:after-throwing>**.

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

      <aop:aspect id="aspectLoggging" ref="logAspect" >

        <!-- @AfterThrowing -->
        <aop:pointcut id="pointCutAfterThrowing"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerThrowException(..))" />

        <aop:after-throwing method="logAfterThrowing" throwing="error"
          pointcut-ref="pointCutAfterThrowing"  />

      </aop:aspect>

    </aop:config>

## 5\. AspectJ <aop:after-around> = @Around

AspectJ @Around example.

    package com.mkyong.aspect;

    import org.aspectj.lang.ProceedingJoinPoint;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.Around;

    @Aspect
    public class LoggingAspect {

    	@Around("execution(* com.mkyong.customer.bo.CustomerBo.addCustomerAround(..))")
    	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    		//...
    	}

    }

Equivalent functionality in XML, with **<aop:after-around>**.

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

       <aop:aspect id="aspectLoggging" ref="logAspect" >

        <!-- @Around -->
       <aop:pointcut id="pointCutAround"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerAround(..))" />

       <aop:around method="logAround" pointcut-ref="pointCutAround"  />

      </aop:aspect>

    </aop:config>

## Full XML example

See complete AspectJ XML based configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:aop="http://www.springframework.org/schema/aop"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/aop
    	http://www.springframework.org/schema/aop/spring-aop-3.0.xsd ">

    <aop:aspectj-autoproxy />

    <bean id="customerBo" class="com.mkyong.customer.bo.impl.CustomerBoImpl" />

    <!-- Aspect -->
    <bean id="logAspect" class="com.mkyong.aspect.LoggingAspect" />

    <aop:config>

      <aop:aspect id="aspectLoggging" ref="logAspect">

        <!-- @Before -->
        <aop:pointcut id="pointCutBefore"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))" />

        <aop:before method="logBefore" pointcut-ref="pointCutBefore" />

        <!-- @After -->
        <aop:pointcut id="pointCutAfter"
           expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))" />

        <aop:after method="logAfter" pointcut-ref="pointCutAfter" />

        <!-- @AfterReturning -->
        <aop:pointcut id="pointCutAfterReturning"
           expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerReturnValue(..))" />

        <aop:after-returning method="logAfterReturning"
          returning="result" pointcut-ref="pointCutAfterReturning" />

        <!-- @AfterThrowing -->
        <aop:pointcut id="pointCutAfterThrowing"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerThrowException(..))" />

        <aop:after-throwing method="logAfterThrowing"
          throwing="error" pointcut-ref="pointCutAfterThrowing" />

        <!-- @Around -->
        <aop:pointcut id="pointCutAround"
          expression="execution(* com.mkyong.customer.bo.CustomerBo.addCustomerAround(..))" />

        <aop:around method="logAround" pointcut-ref="pointCutAround" />

      </aop:aspect>

    </aop:config>

    </beans>

[http://www.mkyong.com/spring3/spring-aop-aspectj-in-xml-configuration-example/](http://www.mkyong.com/spring3/spring-aop-aspectj-in-xml-configuration-example/)
