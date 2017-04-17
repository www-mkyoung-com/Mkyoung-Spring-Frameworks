In Spring 3, one of the feature of “[mvc:annotation-driven](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/mvc.html#mvc-annotation-driven)“, is support for convert object to/from XML file, if JAXB is in project classpath.

In this tutorial, we show you how to convert a return object into XML format and return it back to user via Spring @MVC framework.

Technologies used :

1.  Spring 3.0.5.RELEASE
2.  JDK 1.6
3.  Eclipse 3.6
4.  Maven 3

**JAXB in JDK6**  
JAXB is included in JDK6, so, you do not need to include JAXB library manually, as long as object is annotated with JAXB annotation, Spring will convert it into XML format automatically.

## 1\. Project Dependencies

No extra dependencies, you need to include Spring MVC in your Maven `pom.xml` only.

    <properties>
    	<spring.version>3.0.5.RELEASE</spring.version>
    </properties>

    <dependencies>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-web</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-webmvc</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    </dependencies>

## 2\. Model + JAXB

A simple POJO model and annotated with **JAXB annotation**, later convert this object into XML output.

    package com.mkyong.common.model;

    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name = "coffee")
    public class Coffee {

    	String name;
    	int quanlity;

    	public String getName() {
    		return name;
    	}

    	@XmlElement
    	public void setName(String name) {
    		this.name = name;
    	}

    	public int getQuanlity() {
    		return quanlity;
    	}

    	@XmlElement
    	public void setQuanlity(int quanlity) {
    		this.quanlity = quanlity;
    	}

    	public Coffee(String name, int quanlity) {
    		this.name = name;
    		this.quanlity = quanlity;
    	}

    	public Coffee() {
    	}

    }

## 3\. Controller

Add “**@ResponseBody**” in the method return value, no much detail in the [Spring documentation](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/bind/annotation/ResponseBody.html).

As i know, when Spring see

1.  Object annotated with JAXB
2.  JAXB library existed in classpath
3.  “mvc:annotation-driven” is enabled
4.  Return method annotated with @ResponseBody

It will handle the conversion automatically.

    package com.mkyong.common.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.ResponseBody;
    import com.mkyong.common.model.Coffee;

    @Controller
    @RequestMapping("/coffee")
    public class XMLController {

    	@RequestMapping(value="{name}", method = RequestMethod.GET)
    	public @ResponseBody Coffee getCoffeeInXML(@PathVariable String name) {

    		Coffee coffee = new Coffee(name, 100);

    		return coffee;

    	}

    }

## 4\. mvc:annotation-driven

In one of your Spring configuration XML file, enable “`mvc:annotation-driven`“.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context-3.0.xsd
            http://www.springframework.org/schema/mvc
            http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

    	<context:component-scan base-package="com.mkyong.common.controller" />

    	<mvc:annotation-driven />

    </beans>

**Note**  
Alternatively, you can declares “**spring-oxm.jar**” dependency and include following `MarshallingView`, to handle the conversion. With this method, you don’t need annotate **@ResponseBody** in your method.

    <beans ...>
    	<bean class="org.springframework.web.servlet.view.BeanNameViewResolver" />

    	<bean id="xmlViewer"
    		class="org.springframework.web.servlet.view.xml.MarshallingView">
    		<constructor-arg>
    		  <bean class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
    			<property name="classesToBeBound">
    				<list>
    					<value>com.mkyong.common.model.Coffee</value>
    				</list>
    			</property>
    		  </bean>
    		</constructor-arg>
    	</bean>
    </beans>

## 5\. Demo

URL : _http://localhost:8080/SpringMVC/rest/coffee/arabica_

![spring mvc and xml example demo](http://www.mkyong.com/wp-content/uploads/2011/07/spring-mvc-xml-demo.png)

[http://www.mkyong.com/spring-mvc/spring-3-mvc-and-xml-example/](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-xml-example/)
