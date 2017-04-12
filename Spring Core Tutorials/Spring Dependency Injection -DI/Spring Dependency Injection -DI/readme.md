In Spring frameowork, Dependency Injection (DI) design pattern is used to define the object dependencies between each other. It exits in two major types :

*   Setter Injection
*   Constructor Injection

## 1\. Setter Injection

This is the most popular and simple DI method, it will injects the dependency via a setter method.

Example

A helper class with a setter method.

    package com.mkyong.output;

    import com.mkyong.output.IOutputGenerator;

    public class OutputHelper
    {
    	IOutputGenerator outputGenerator;

    	public void setOutputGenerator(IOutputGenerator outputGenerator){
    		this.outputGenerator = outputGenerator;
    	}

    }

A bean configuration file to declare the beans and set the dependency via setter injection (property tag).

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<property name="outputGenerator">
    			<ref bean="CsvOutputGenerator" />
    		</property>
    	</bean>

    <bean id="CsvOutputGenerator" class="com.mkyong.output.impl.CsvOutputGenerator" />
    <bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

You just injects a ‘CsvOutputGenerator’ bean into ‘OutputHelper’ object via a setter method (setOutputGenerator).

## 2\. Constructor Injection

This DI method will injects the dependency via a constructor.

Example

A helper class with a constructor.

    package com.mkyong.output;

    import com.mkyong.output.IOutputGenerator;

    public class OutputHelper
    {
    	IOutputGenerator outputGenerator;

            OutputHelper(IOutputGenerator outputGenerator){
    		this.outputGenerator = outputGenerator;
    	}
    }

A bean configuration file to declare the beans and set the dependency via constructor injection (constructor-arg tag).

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<constructor-arg>
    			<bean class="com.mkyong.output.impl.CsvOutputGenerator" />
    		</constructor-arg>
    	</bean>

    <bean id="CsvOutputGenerator" class="com.mkyong.output.impl.CsvOutputGenerator" />
    <bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

You just injects a ‘CsvOutputGenerator’ bean into ‘OutputHelper’ object via a constructor.

## Setter or Constructor injection?

There are no hard rule set by Spring framework, just use whatever type of DI that suit your project needs. However, due to the simplicity of the setter injection, it’s always selected for most of the scenarios.

## Reference

1. [http://en.wikipedia.org/wiki/Dependency_injection](http://en.wikipedia.org/wiki/Dependency_injection)


[http://www.mkyong.com/spring/spring-dependency-injection-di/](http://www.mkyong.com/spring/spring-dependency-injection-di/)
