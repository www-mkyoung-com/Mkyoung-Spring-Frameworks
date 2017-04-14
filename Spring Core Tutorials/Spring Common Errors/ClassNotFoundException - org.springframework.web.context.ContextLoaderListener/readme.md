## Problem

The **ContextLoaderListener** is used to integrate Spring with other web application.

    <!-- file : web.xml -->
    <context-param>
    	<param-name>contextConfigLocation</param-name>
    	<param-value>/WEB-INF/Spring/applicationContext.xml</param-value>
     </context-param>

     <listener>
       <listener-class>
             org.springframework.web.context.ContextLoaderListener
       </listener-class>
     </listener>

And the common error message is, your server can not find this Spring `ContextLoaderListener` class during the server start up.

    SEVERE: Error configuring application listener of class
    org.springframework.web.context.ContextLoaderListener java.lang.ClassNotFoundException:

    org.springframework.web.context.ContextLoaderListener
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1516)
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1361)
    	at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:3915)
    	at org.apache.catalina.core.StandardContext.start(StandardContext.java:4467)
    	at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1045)
    	at org.apache.catalina.core.StandardHost.start(StandardHost.java:785)
    	at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1045)
    	at org.apache.catalina.core.StandardEngine.start(StandardEngine.java:443)
    	at org.apache.catalina.core.StandardService.start(StandardService.java:519)
    	at org.apache.catalina.core.StandardServer.start(StandardServer.java:710)
    	at org.apache.catalina.startup.Catalina.start(Catalina.java:581)
    	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
    	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
    	at java.lang.reflect.Method.invoke(Unknown Source)
    	at org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:289)
    	at org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:414)

## Solution

This is always happened in deployment and debugging environment.

## 1.Deployment environment

In deployment environment, just make sure your server classpath has included the Spring jar library (e.g spring-2.5.6.jar).

**For Spring3**, `ContextLoaderListener` is moved to **spring-web.jar**, you can get the library from Maven central repository.

    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-web</artifactId>
    	<version>3.0.5.RELEASE</version>
    </dependency>

## 2.Debugging environment

In debugging environment, the steps may vary from different IDE, but the solution is same. In Eclipse, developers usually will create a tomcat, jboss…whatever application server for debugging, just make sure the correct Spring jars are included.

1\. Double click on your debugging server  
2\. Click on the “Open launch configuration” to access the server environment

![](http://www.mkyong.com/wp-content/uploads/2010/03/eclipse-spring-ContextLoaderListener-not-found-1.gif)

3\. Click on the classpath tab  
4\. Include the Spring jar file here , it may also required common log jar due to Spring dependency.

![](http://www.mkyong.com/wp-content/uploads/2010/03/eclipse-spring-ContextLoaderListener-not-found-2.gif)

5\. Done, run your application again.

[http://www.mkyong.com/spring/spring-error-classnotfoundexception-org-springframework-web-context-contextloaderlistener/](http://www.mkyong.com/spring/spring-error-classnotfoundexception-org-springframework-web-context-contextloaderlistener/)
