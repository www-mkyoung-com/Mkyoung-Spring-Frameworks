## Problem

In Spring OXM (object XML mapping), when converting an object to XML file, it hits following error message :

    Caused by: java.lang.ClassNotFoundException: org.exolab.castor.xml.XMLException
    	at java.net.URLClassLoader$1.run(URLClassLoader.java:200)
    	at java.security.AccessController.doPrivileged(Native Method)
    	at java.net.URLClassLoader.findClass(URLClassLoader.java:188)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:307)
    	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:301)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:252)
    	at java.lang.ClassLoader.loadClassInternal(ClassLoader.java:320)
    	... 29 more

Is [castor data binding framework](http://www.castor.org/) included in Spring oxm?

## Solution

The castor is an optional dependency in **spring-oxm.jar**, to use castor to marshaller or unmarshaller XML in Spring OXM, add this castor dependency into your Maven `pom.xml` file.

    <dependencies>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-oxm</artifactId>
    		<version>3.0.5.RELEASE</version>
    	</dependency>

    	<!-- Uses Castor for XML -->
    	<dependency>
    		<groupId>org.codehaus.castor</groupId>
    		<artifactId>castor</artifactId>
    		<version>1.2</version>
    	</dependency>

    	<!-- Castor need this -->
    	<dependency>
    		<groupId>xerces</groupId>
    		<artifactId>xercesImpl</artifactId>
    		<version>2.8.1</version>
    	</dependency>

    </dependencies>

[http://www.mkyong.com/spring3/classnotfoundexception-org-exolab-castor-xml-xmlexception/](http://www.mkyong.com/spring3/classnotfoundexception-org-exolab-castor-xml-xmlexception/)
