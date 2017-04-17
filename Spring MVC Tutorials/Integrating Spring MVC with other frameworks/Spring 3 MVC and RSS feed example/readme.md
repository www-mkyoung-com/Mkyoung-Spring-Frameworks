In Spring 3, comes with a abstract class “**AbstractRssFeedView**” to generate RSS feed view, using java.net’s ROME package. In this tutorial, we show you how to generate a RSS feed view from Spring MVC framework.

Technologies used :

1.  Spring 3.0.5.RELEASE
2.  ROME 1.0.0
3.  JDK 1.6
4.  Eclipse 3.6
5.  Maven 3

At the end of the tutorial, when you visit this URL – _http://localhost:8080/SpringMVC/rest/rssfeed_, browser will return following RSS feed content :

    <?xml version="1.0" encoding="UTF-8"?>
    <rss xmlns:content="http://purl.org/rss/1.0/modules/content/" version="2.0">
      <channel>
        <title>Mkyong Dot Com</title>
        <link>http://www.mkyong.com</link>
        <description>Java Tutorials and Examples</description>
        <item>
          <title>Spring MVC Tutorial 1</title>
          <link>http://www.mkyong.com/spring-mvc/tutorial-1</link>
          <content:encoded>Tutorial 1 summary ...</content:encoded>
          <pubDate>Tue, 26 Jul 2011 02:26:08 GMT</pubDate>
        </item>
        <item>
          <title>Spring MVC Tutorial 2</title>
          <link>http://www.mkyong.com/spring-mvc/tutorial-2</link>
          <content:encoded>Tutorial 2 summary ...</content:encoded>
          <pubDate>Tue, 26 Jul 2011 02:26:08 GMT</pubDate>
        </item>
      </channel>
    </rss>

## 1\. Directory Structure

Review the final project structure.

![directory structure](http://www.mkyong.com/wp-content/uploads/2011/07/spring-mvc-rss-feed.png)

## 2\. Project Dependencies

For Maven, declares following dependencies in your `pom.xml`.

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

    	<!-- RSS -->
    	<dependency>
    		<groupId>net.java.dev.rome</groupId>
    		<artifactId>rome</artifactId>
    		<version>1.0.0</version>
    	</dependency>

    	<!-- for compile only, your container should have this -->
    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>servlet-api</artifactId>
    		<version>2.5</version>
    		<scope>provided</scope>
    	</dependency>

    </dependencies>

## 3\. Model

A simple POJO, later use this object to generate the RSS feed content.

    package com.mkyong.common.model;

    import java.util.Date;

    public class SampleContent {

    	String title;
    	String url;
    	String summary;
    	Date createdDate;

    	//getter and seeter methods
    }

## 4\. AbstractRssFeedView

Create a class extends **AbstractRssFeedView**, and override the `buildFeedMetadata` and `buildFeedItems` methods, below code should be self-explanatory.

    package com.mkyong.common.rss;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
    import com.mkyong.common.model.SampleContent;
    import com.sun.syndication.feed.rss.Channel;
    import com.sun.syndication.feed.rss.Content;
    import com.sun.syndication.feed.rss.Item;

    public class CustomRssViewer extends AbstractRssFeedView {

    	@Override
    	protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
    		HttpServletRequest request) {

    		feed.setTitle("Mkyong Dot Com");
    		feed.setDescription("Java Tutorials and Examples");
    		feed.setLink("http://www.mkyong.com");

    		super.buildFeedMetadata(model, feed, request);
    	}

    	@Override
    	protected List<Item> buildFeedItems(Map<String, Object> model,
    		HttpServletRequest request, HttpServletResponse response)
    		throws Exception {

    		@SuppressWarnings("unchecked")
    		List<SampleContent> listContent = (List<SampleContent>) model.get("feedContent");
    		List<Item> items = new ArrayList<Item>(listContent.size());

    		for(SampleContent tempContent : listContent ){

    			Item item = new Item();

    			Content content = new Content();
    			content.setValue(tempContent.getSummary());
    			item.setContent(content);

    			item.setTitle(tempContent.getTitle());
    			item.setLink(tempContent.getUrl());
    			item.setPubDate(tempContent.getCreatedDate());

    			items.add(item);
    		}

    		return items;
    	}

    }

## 5\. Controller

Spring MVC controller class, generate the rss feed content, and return a view name “**rssViewer**” (This view name is belong to above “**CustomRssViewer**“, will register in step 6 later).

    package com.mkyong.common.controller;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.servlet.ModelAndView;
    import com.mkyong.common.model.SampleContent;

    @Controller
    public class RssController {

    	@RequestMapping(value="/rssfeed", method = RequestMethod.GET)
    	public ModelAndView getFeedInRss() {

    		List<SampleContent> items = new ArrayList<SampleContent>();

    		SampleContent content  = new SampleContent();
    		content.setTitle("Spring MVC Tutorial 1");
    		content.setUrl("http://www.mkyong.com/spring-mvc/tutorial-1");
    		content.setSummary("Tutorial 1 summary ...");
    		content.setCreatedDate(new Date());
    		items.add(content);

    		SampleContent content2  = new SampleContent();
    		content2.setTitle("Spring MVC Tutorial 2");
    		content2.setUrl("http://www.mkyong.com/spring-mvc/tutorial-2");
    		content2.setSummary("Tutorial 2 summary ...");
    		content2.setCreatedDate(new Date());
    		items.add(content2);

    		ModelAndView mav = new ModelAndView();
    		mav.setViewName("rssViewer");
    		mav.addObject("feedContent", items);

    		return mav;

    	}

    }

## 6\. Spring Bean Registration

In a Spring bean definition file, enable the auto component scanning, and register your “`CustomRssViewer`” class and “`BeanNameViewResolver`” view resolver, so that when view name “**rssViewer**” is returned, Spring know it should map to bean id “**rssViewer**“.

_File : mvc-dispatcher-servlet.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context-3.0.xsd">

    	<context:component-scan base-package="com.mkyong.common.controller" />

    	<!-- Map returned view name "rssViewer" to bean id "rssViewer" -->
    	<bean class="org.springframework.web.servlet.view.BeanNameViewResolver" />

    	<bean id="rssViewer" class="com.mkyong.common.rss.CustomRssViewer" />

    </beans>

**Note**  
File content of `web.xml` is omitted, just a standard configuration, if you are interest, download this whole project at the end of the article.

## 7\. Demo

URL : _http://localhost:8080/SpringMVC/rest/rssfeed_

![spring mvc and rss feed demo](http://www.mkyong.com/wp-content/uploads/2011/07/spring-mvc-rss-feed-demo.png)

**How about Atom?**  
For Atom, you just need to extends `AbstractAtomFeedView`, instead of `AbstractRssFeedView`.

[http://www.mkyong.com/spring-mvc/spring-3-mvc-and-rss-feed-example/](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-rss-feed-example/)
