Spring 3, **ContentNegotiatingViewResolver**, is an interesting view resolver, which allow you to output a same resource (content or data) to different type of views like **JSP**, **XML**, **RSS**, **JSON **and etc. Put it simple, see following web requested URL, which will return in different views.

1.  `http://www.mkyong.com/fruit/banana.rss` , returned as RSS file.
2.  `http://www.mkyong.com/fruit/banana.xml` , returned as XML file.
3.  `http://www.mkyong.com/fruit/banana.json` , returned as JSON file.
4.  `http://www.mkyong.com/fruit/banana`, returned to your default view resolver.

**Note**  
This **ContentNegotiatingViewResolver** first determine “which view resolver should return by file extension”, if no view is match, then use the default view resolver. Read this [Spring documentation](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/servlet/view/ContentNegotiatingViewResolver.html) to study how it works.

In this tutorial, we show you how to use `ContentNegotiatingViewResolver`. At the end of this tutorial, a same model will be returned in different views – XML, JSON, RSS and JSP, based on it’s requested file extension.

Technologies used :

1.  Spring 3.0.5.RELEASE
2.  Jackson 1.7.1
3.  Rome 1.0.0
4.  JDK 1.6
5.  Maven 3
6.  Eclipse 3.6

**Note**  
JAXB is bundled in JDK1.6, so, you don’t need to include it manually.

## 1\. Project Dependency

Declares following dependencies in your Maven `pom.xml` file.

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

    		<!-- Jackson JSON Mapper -->
    		<dependency>
    			<groupId>org.codehaus.jackson</groupId>
    			<artifactId>jackson-mapper-asl</artifactId>
    			<version>1.7.1</version>
    		</dependency>

    		<!-- RSS -->
    		<dependency>
    			<groupId>net.java.dev.rome</groupId>
    			<artifactId>rome</artifactId>
    			<version>1.0.0</version>
    		</dependency>

    	</dependencies>

    </project>

## 2\. Model

A Pojo, annotated with JAXB annotation, so that it can output in XML file. Besides, later we use this model to display in different views.

    package com.mkyong.common.model;

    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name = "fruit")
    public class Fruit {

    	String name;
    	int quality;

    	public String getName() {
    		return name;
    	}

    	@XmlElement
    	public void setName(String name) {
    		this.name = name;
    	}

    	public int getQuality() {
    		return quality;
    	}

    	@XmlElement
    	public void setQuality(int quality) {
    		this.quality = quality;
    	}

    	public Fruit(String name, int quality) {
    		this.name = name;
    		this.quality = quality;
    	}

    	public Fruit() {
    	}

    }

## 3\. JSON and XML View

To output JSON and XML views, you don’t need to do any extra works, Spring MVC will handle the conversion automatically. Read this [Spring MVC and XML](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-xml-example/), and [Spring MVC and JSON](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-json-example/) examples.

## 4\. RSS View

To output RSS View, you need to extend `AbstractRssFeedView`. Read this [Spring 3 MVC and RSS example](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-rss-feed-example/) to know how it works.

    package com.mkyong.common.rss;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
    import com.mkyong.common.model.Fruit;
    import com.sun.syndication.feed.rss.Channel;
    import com.sun.syndication.feed.rss.Content;
    import com.sun.syndication.feed.rss.Item;

    public class RssFeedView extends AbstractRssFeedView {

    	@Override
    	protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
    		HttpServletRequest request) {

    		feed.setTitle("Sample Title");
    		feed.setDescription("Sample Description");
    		feed.setLink("http://google.com");

    		super.buildFeedMetadata(model, feed, request);
    	}

    	@Override
    	protected List<Item> buildFeedItems(Map<String, Object> model,
    			HttpServletRequest request, HttpServletResponse response)
    			throws Exception {

    		Fruit fruit = (Fruit) model.get("model");
    		String msg = fruit.getName() + fruit.getQuality();

    		List<Item> items = new ArrayList<Item>(1);
    		Item item = new Item();
    		item.setAuthor("mkyong");
    		item.setLink("http://www.mkyong.com");

    		Content content = new Content();
    		content.setValue(msg);

    		item.setContent(content);

    		items.add(item);

    		return items;
    	}
    }

## 5\. JSP View

A JSP page to display the model data.

_File : list.jsp_

    <html>
    <body>
    	<h1>Spring @MVC ContentNegotiatingViewResolver</h1>

    	Fruit Name : ${model.name} <br />
    	Fruit Quality : ${model.quality}

    </body>
    </html>

## 6\. Controller

Spring controller, to generate a “fruit” model and return it.

    package com.mkyong.common.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import com.mkyong.common.model.Fruit;

    @Controller
    @RequestMapping("/fruit")
    public class FruitController {

    	@RequestMapping(value="{fruitName}", method = RequestMethod.GET)
    	public String getFruit(@PathVariable String fruitName, ModelMap model) {

    		Fruit fruit = new Fruit(fruitName, 1000);
    		model.addAttribute("model", fruit);

    		return "list";

    	}

    }

## 7\. ContentNegotiatingViewResolver example

The code should be self-explanatory. However, you have to define the “**order**” property, where lower value get higher priority. In this case, when a URL is requested, Spring MVC will use “`ContentNegotiatingViewResolver`” (order=1) to return a suitable view (based on file extension declared in “mediaTypes” property), if not match, then use “`InternalResourceViewResolver`” (order=2) to return a default JSP page.

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

    	<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
    	  <property name="order" value="1" />
    	  <property name="mediaTypes">
    		<map>
    		   <entry key="json" value="application/json" />
    		   <entry key="xml" value="application/xml" />
    		   <entry key="rss" value="application/rss+xml" />
    		</map>
    	  </property>

    	  <property name="defaultViews">
    		<list>
    		  <!-- JSON View -->
    		  <bean
    			class="org.springframework.web.servlet.view.json.MappingJacksonJsonView">
    		  </bean>

    		  <!-- RSS View -->
    		  <bean class="com.mkyong.common.rss.RssFeedView" />

    		  <!-- JAXB XML View -->
    		  <bean class="org.springframework.web.servlet.view.xml.MarshallingView">
    			<constructor-arg>
    				<bean class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
    				   <property name="classesToBeBound">
    					<list>
    					   <value>com.mkyong.common.model.Fruit</value>
    					</list>
    				   </property>
    				</bean>
    			</constructor-arg>
    		  </bean>
    		 </list>
    	  </property>
    	  <property name="ignoreAcceptHeader" value="true" />

    	</bean>

    	<!-- If no extension matched, use JSP view -->
    	<bean
    		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="order" value="2" />
    		<property name="prefix">
    			<value>/WEB-INF/pages/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

    </beans>

## 8\. Demo

Same model and display in different views, via `ContentNegotiatingViewResolver`.

_http://localhost:8080/SpringMVC/fruit/banana.xml_ , display as XML file.

![spring mvc and xml demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring-mvc-contentneg-xml.png)

_http://localhost:8080/SpringMVC/fruit/banana.json_ , display as JSON file.

![spring mvc and json demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring-mvc-contentneg-json.png)

_http://localhost:8080/SpringMVC/fruit/banana.rss_ , display as RSS file.

![spring mvc and RSS demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring-mvc-contentneg-rss.png)

_http://localhost:8080/SpringMVC/fruit/banana_ , display as JSP page.

![spring mvc and JSP demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring-mvc-contentneg-jsp.png)

[http://www.mkyong.com/spring-mvc/spring-3-mvc-contentnegotiatingviewresolver-example/](http://www.mkyong.com/spring-mvc/spring-3-mvc-contentnegotiatingviewresolver-example/)
