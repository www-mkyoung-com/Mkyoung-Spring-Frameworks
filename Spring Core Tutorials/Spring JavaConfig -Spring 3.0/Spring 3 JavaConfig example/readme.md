Since Spring 3, **JavaConfig** features are included in core Spring module, it allow developer to move bean definition and Spring configuration out of XML file into Java class.

But, you are still allow to use the classic XML way to define beans and configuration, the **JavaConfig** is just another alternative solution.

See the different between classic XML definition and JavaConfig to define a bean in Spring container.

_Spring XML file :_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="helloBean" class="com.mkyong.hello.impl.HelloWorldImpl">

    </beans>

_Equivalent configuration in JavaConfig :_

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import com.mkyong.hello.HelloWorld;
    import com.mkyong.hello.impl.HelloWorldImpl;

    @Configuration
    public class AppConfig {

        @Bean(name="helloBean")
        public HelloWorld helloWorld() {
            return new HelloWorldImpl();
        }

    }

## Spring JavaConfig Hello World

Now, see a full Spring JavaConfig example.

## 1\. Directory Structure

See directory structure of this example.

![directory structure of this example](http://www.mkyong.com/wp-content/uploads/2011/06/spring-javaconfig-folder.png)

## 2\. Dependency Library

To use JavaConfig (**@Configuration**), you need to include **CGLIB** library. See dependencies :

    <!-- Spring 3 dependencies -->
    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-core</artifactId>
    	<version>${spring.version}</version>
    </dependency>

    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-context</artifactId>
    	<version>${spring.version}</version>
    </dependency>

    <!-- JavaConfig need this library -->
    <dependency>
    	<groupId>cglib</groupId>
    	<artifactId>cglib</artifactId>
    	<version>2.2.2</version>
    </dependency>

## 3\. Spring Bean

A simple bean.

    package com.mkyong.hello;

    public interface HelloWorld {

    	void printHelloWorld(String msg);

    }

    package com.mkyong.hello.impl;

    import com.mkyong.hello.HelloWorld;

    public class HelloWorldImpl implements HelloWorld {

    	@Override
    	public void printHelloWorld(String msg) {

    		System.out.println("Hello : " + msg);
    	}

    }

## 4\. JavaConfig Annotation

Annotate with `@Configuration` to tell Spring that this is the core Spring configuration file, and define bean via `@Bean`.

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import com.mkyong.hello.HelloWorld;
    import com.mkyong.hello.impl.HelloWorldImpl;

    @Configuration
    public class AppConfig {

        @Bean(name="helloBean")
        public HelloWorld helloWorld() {
            return new HelloWorldImpl();
        }

    }

## 5\. Run it

Load your JavaConfig class with `AnnotationConfigApplicationContext`.

    package com.mkyong.core;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.AnnotationConfigApplicationContext;
    import com.mkyong.config.AppConfig;
    import com.mkyong.hello.HelloWorld;

    public class App {
    	public static void main(String[] args) {

                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    	    HelloWorld obj = (HelloWorld) context.getBean("helloBean");

    	    obj.printHelloWorld("Spring3 Java Config");

    	}
    }

_Output_

    Hello : Spring3 Java Config

[http://www.mkyong.com/spring3/spring-3-javaconfig-example/](http://www.mkyong.com/spring3/spring-3-javaconfig-example/)
