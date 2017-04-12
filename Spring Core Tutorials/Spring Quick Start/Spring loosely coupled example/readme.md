The concept of object-oriented is a good design to break your system into a group of reusable objects. However, when system grows larger, especially in Java project, the huge object dependencies will always tightly coupled causing objects very hard to manage or modify. In this scenario, you can use Spring framework to act as a central module to manage all the object dependencies easily and efficiently.

## Output Generator Example

Let’s see an example, assume your project has a function to output the content to Csv or Json format. Your code may look like the following example:

_File : IOutputGenerator.java – An interface for output generator_

    package com.mkyong.output;

    public interface IOutputGenerator
    {
    	public void generateOutput();
    }

_File : CsvOutputGenerator.java – A Csv output generator to implement the IOutputGenerator interface._

    package com.mkyong.output.impl;

    import com.mkyong.output.IOutputGenerator;

    public class CsvOutputGenerator implements IOutputGenerator
    {
    	public void generateOutput(){
    		System.out.println("Csv Output Generator");
    	}
    }

_File : JsonOutputGenerator.java – A Json output generator to implement the IOutputGenerator interface._

    package com.mkyong.output.impl;

    import com.mkyong.output.IOutputGenerator;

    public class JsonOutputGenerator implements IOutputGenerator
    {
    	public void generateOutput(){
    		System.out.println("Json Output Generator");
    	}
    }

There are couple of ways to call the IOutputGenerator, and how to use Spring to avoid objects to coupled tightly with each other.

## 1\. Method 1 – Call it directly

Normal way, call it directly.

    package com.mkyong.common;

    import com.mkyong.output.IOutputGenerator;
    import com.mkyong.output.impl.CsvOutputGenerator;

    public class App
    {
        public static void main( String[] args )
        {
        	IOutputGenerator output = new CsvOutputGenerator();
        	output.generateOutput();
        }
    }

**Problem**  
In this way, the problem is the “output” is coupled tightly to CsvOutputGenerator, every change of output generator may involve code change. If this code is scattered all over of your project, every change of the output generator will make you suffer seriously.

## Method 2 – Call it with helper class

You may think of creating a helper class to move all the output implementation inside.

    package com.mkyong.output;

    import com.mkyong.output.IOutputGenerator;
    import com.mkyong.output.impl.CsvOutputGenerator;

    public class OutputHelper
    {
    	IOutputGenerator outputGenerator;

    	public OutputHelper(){
    		outputGenerator = new CsvOutputGenerator();
    	}

    	public void generateOutput(){
    		outputGenerator.generateOutput();
    	}

    }

Call it via helper class.

    package com.mkyong.common;

    import com.mkyong.output.OutputHelper;

    public class App
    {
        public static void main( String[] args )
        {
        	OutputHelper output = new OutputHelper();
        	output.generateOutput();
        }
    }

**Problem**  
This looks more elegant, and you only need to manage a single helper class, however the helper class is still tightly coupled to CsvOutputGenerator, every change of output generator still involves minor code change.

## Method 3 – Spring

In this scenario, Spring Dependency Injection (DI) is a good choice. Spring can make your output generator loosely coupled to the output generator.

Minor change in OutputHelper class.

    package com.mkyong.output;

    import com.mkyong.output.IOutputGenerator;

    public class OutputHelper
    {
    	IOutputGenerator outputGenerator;

    	public void generateOutput(){
    		outputGenerator.generateOutput();
    	}

    	public void setOutputGenerator(IOutputGenerator outputGenerator){
    		this.outputGenerator = outputGenerator;
    	}
    }

Create a Spring bean configuration file and declare all your Java object dependencies here.

    <!-- Spring-Common.xml -->
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<property name="outputGenerator" ref="CsvOutputGenerator" />
    	</bean>

    	<bean id="CsvOutputGenerator" class="com.mkyong.output.impl.CsvOutputGenerator" />
    	<bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

Call it via Spring

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    import com.mkyong.output.OutputHelper;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        	   new ClassPathXmlApplicationContext(new String[] {"Spring-Common.xml"});

        	OutputHelper output = (OutputHelper)context.getBean("OutputHelper");
        	output.generateOutput();

        }
    }

Now, you just need to change the Spring XML file for a different output generator. When output changed, you need to modify the Spring XML file only, no code changed, means less error.

## Conclusion

With Spring framework – Dependency Injection (DI) is a useful feature for object dependencies management, it is just elegant, highly flexible and facilitates maintainability, especially in large Java project.

[http://www.mkyong.com/spring/spring-loosely-coupled-example/](http://www.mkyong.com/spring/spring-loosely-coupled-example/)
