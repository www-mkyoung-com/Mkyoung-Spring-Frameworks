## Problem

Developing RSS with Spring MVC, extends “`AbstractRssFeedView`“, hit following error message during application start up.

    Caused by: java.lang.NoClassDefFoundError: com/sun/syndication/feed/WireFeed
    	at java.lang.Class.getDeclaredConstructors0(Native Method)
    	at java.lang.Class.privateGetDeclaredConstructors(Class.java:2389)
    	at java.lang.Class.getDeclaredConstructors(Class.java:1836)
    	//...
    Caused by: java.lang.ClassNotFoundException: com.sun.syndication.feed.WireFeed
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1516)
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1361)
    	at java.lang.ClassLoader.loadClassInternal(ClassLoader.java:320)
    	... 41 more

## Solution

Spring MVC using “[ROME](http://java.net/projects/rome/)” to generate RSS feed. For Maven, include below dependency in `pom.xml` file.

    <dependency>
    	<groupId>net.java.dev.rome</groupId>
    	<artifactId>rome</artifactId>
    	<version>1.0.0</version>
    </dependency>

[http://www.mkyong.com/spring-mvc/classnotfoundexception-com-sun-syndication-feed-wirefeed/](http://www.mkyong.com/spring-mvc/classnotfoundexception-com-sun-syndication-feed-wirefeed/)
