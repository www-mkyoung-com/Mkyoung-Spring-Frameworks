## Problem

With **Spring OXM + Castor binding**, the Castor library is added, but still hit the following error message?

    Exception in thread "main" java.lang.RuntimeException:
    	Could not instantiate serializer org.apache.xml.serialize.XMLSerializer:
    	java.lang.ClassNotFoundException: org.apache.xml.serialize.XMLSerializer
    	at org.exolab.castor.xml.XercesSerializer.<init>(XercesSerializer.java:50)
    	//...

Castor dependency in Maven.

    <dependency>
    	<groupId>org.codehaus.castor</groupId>
    	<artifactId>castor</artifactId>
    	<version>1.2</version>
    </dependency>

## Solution

If not mistake, **Castor need Xerces to work**, so, you need to add Xerces dependency also.

    <dependencies>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-oxm</artifactId>
    		<version>3.0.5.RELEASE</version>
    	</dependency>

    	<dependency>
    		<groupId>org.codehaus.castor</groupId>
    		<artifactId>castor</artifactId>
    		<version>1.2</version>
    	</dependency>

    	<dependency>
    		<groupId>xerces</groupId>
    		<artifactId>xercesImpl</artifactId>
    		<version>2.8.1</version>
    	</dependency>

    </dependencies>

[http://www.mkyong.com/spring3/classnotfoundexception-org-apache-xml-serialize-xmlserializer/](http://www.mkyong.com/spring3/classnotfoundexception-org-apache-xml-serialize-xmlserializer/)
