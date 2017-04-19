## Problem

Working with Spring Security, which jar contains `DefaultSavedRequest`?

    SEVERE: Exception loading sessions from persistent storage
    java.lang.ClassNotFoundException:
            org.springframework.security.web.savedrequest.DefaultSavedRequest
    	at java.net.URLClassLoader$1.run(URLClassLoader.java:200)
    	at java.security.AccessController.doPrivileged(Native Method)
    	at java.net.URLClassLoader.findClass(URLClassLoader.java:188)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:307)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:252)

## Solution

`DefaultSavedRequest` is inside **spring-security-web.jar**. Visit this [Spring Security hello world example](http://www.mkyong.com/spring-security/spring-security-hello-world-example/) for the list of dependencies libraries.

    <!-- Spring Security & dependencies -->
    <dependency>
    	<groupId>org.springframework.security</groupId>
    	<artifactId>spring-security-core</artifactId>
    	<version>3.0.5.RELEASE</version>
    </dependency>

    <dependency>
    	<groupId>org.springframework.security</groupId>
    	<artifactId>spring-security-web</artifactId>
    	<version>3.0.5.RELEASE</version>
    </dependency>

    <dependency>
    	<groupId>org.springframework.security</groupId>
    	<artifactId>spring-security-config</artifactId>
    	<version>3.0.5.RELEASE</version>
    </dependency>

[http://www.mkyong.com/spring-security/classnotfoundexception-defaultsavedrequest/](http://www.mkyong.com/spring-security/classnotfoundexception-defaultsavedrequest/)
