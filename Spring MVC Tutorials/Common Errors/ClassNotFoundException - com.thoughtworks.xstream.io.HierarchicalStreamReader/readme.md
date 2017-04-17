## Problem

XML development in Spring MVC, via oxm , hit “`HierarchicalStreamReader`” class not found exception?

    Caused by: java.lang.ClassNotFoundException: com.thoughtworks.xstream.io.HierarchicalStreamReader
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1516)
    	at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1361)
    	at java.lang.ClassLoader.loadClassInternal(ClassLoader.java:320)
    	... 49 more

## Solution

The class “`HierarchicalStreamReader`” class belong to “**xstream.jar**“. If you are using Maven, declares following dependency in your `pom.xml` file.

    <dependency>
    	<groupId>com.thoughtworks.xstream</groupId>
    	<artifactId>xstream</artifactId>
    	<version>1.3.1</version>
    </dependency>

**Note**  
For Ant user, just download the “**xstream.jar**” from [http://xstream.codehaus.org/](http://xstream.codehaus.org/) directly.

[http://www.mkyong.com/spring-mvc/classnotfoundexception-com-thoughtworks-xstream-io-hierarchicalstreamreader/](http://www.mkyong.com/spring-mvc/classnotfoundexception-com-thoughtworks-xstream-io-hierarchicalstreamreader/)
