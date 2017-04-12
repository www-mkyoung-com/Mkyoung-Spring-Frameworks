Uses Spring to dependency inject a bean via constructor.

## 1\. IOutputGenerator

An interface and implementation class of it.

    package com.mkyong.output;

    public interface IOutputGenerator
    {
    	public void generateOutput();
    }

    package com.mkyong.output.impl;

    import com.mkyong.output.IOutputGenerator;

    public class JsonOutputGenerator implements IOutputGenerator
    {
    	public void generateOutput(){
    		System.out.println("This is Json Output Generator");
    	}
    }

## 2\. Helper class

A helper class, later use Spring to DI the IOutputGenerator, via constructor.

    package com.mkyong.output;

    import com.mkyong.output.IOutputGenerator;

    public class OutputHelper {
    	IOutputGenerator outputGenerator;

    	public void generateOutput() {
    		outputGenerator.generateOutput();
    	}

    	//DI via constructor
    	public OutputHelper(IOutputGenerator outputGenerator){
    		this.outputGenerator = outputGenerator;
    	}

    }

## 3\. Spring configuration

See below Spring bean configuration, Spring will DI above “JsonOutputGenerator” into this “OutputHelper” class, via constructor “**public OutputHelper(IOutputGenerator outputGenerator)**“.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<constructor-arg>
    			<ref bean="JsonOutputGenerator" />
    		</constructor-arg>
    	</bean>

    	<bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

## 4\. Run it

Load everything, and run it.

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import com.mkyong.output.OutputHelper;

    public class App {
    	public static void main(String[] args) {
    		ApplicationContext context = new ClassPathXmlApplicationContext(
    				"SpringBeans.xml");

    		OutputHelper output = (OutputHelper)context.getBean("OutputHelper");
        	        output.generateOutput();
    	}
    }

Output

    This is Json Output Generator

[http://www.mkyong.com/spring/spring-di-via-constructor/](http://www.mkyong.com/spring/spring-di-via-constructor/)
