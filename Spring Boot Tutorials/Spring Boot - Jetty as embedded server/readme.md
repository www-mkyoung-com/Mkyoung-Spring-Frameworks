By default, Spring Boot use Tomcat as the default embedded server, to change it to Jetty, just exclude Tomcat and include Jetty like this :

## 1\. spring-boot-starter-web

pom.xml

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-web</artifactId>
    	<exclusions>
    		<exclusion>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-tomcat</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>

## 2\. spring-boot-starter-thymeleaf

pom.xml

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-thymeleaf</artifactId>
    	<exclusions>
    		<exclusion>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-tomcat</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>

Review the dependencies

    $ mvn dependency:tree

    [INFO] org.springframework.boot:spring-boot-web-thymeleaf:jar:1.0
    [INFO] +- org.springframework.boot:spring-boot-starter-thymeleaf:jar:1.4.2.RELEASE:compile
    [INFO] |  +- org.springframework.boot:spring-boot-starter:jar:1.4.2.RELEASE:compile
    [INFO] |  |  +- org.springframework.boot:spring-boot-starter-logging:jar:1.4.2.RELEASE:compile
    [INFO] |  |  |  +- ch.qos.logback:logback-classic:jar:1.1.7:compile
    [INFO] |  |  |  |  \- ch.qos.logback:logback-core:jar:1.1.7:compile
    [INFO] |  |  |  +- org.slf4j:jcl-over-slf4j:jar:1.7.21:compile
    [INFO] |  |  |  +- org.slf4j:jul-to-slf4j:jar:1.7.21:compile
    [INFO] |  |  |  \- org.slf4j:log4j-over-slf4j:jar:1.7.21:compile
    [INFO] |  |  +- org.springframework:spring-core:jar:4.3.4.RELEASE:compile
    [INFO] |  |  \- org.yaml:snakeyaml:jar:1.17:runtime
    [INFO] |  +- org.springframework.boot:spring-boot-starter-web:jar:1.4.2.RELEASE:compile
    [INFO] |  |  +- org.hibernate:hibernate-validator:jar:5.2.4.Final:compile
    [INFO] |  |  |  +- javax.validation:validation-api:jar:1.1.0.Final:compile
    [INFO] |  |  |  +- org.jboss.logging:jboss-logging:jar:3.3.0.Final:compile
    [INFO] |  |  |  \- com.fasterxml:classmate:jar:1.3.3:compile
    [INFO] |  |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.8.4:compile
    [INFO] |  |  |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.8.4:compile
    [INFO] |  |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.8.4:compile
    [INFO] |  |  +- org.springframework:spring-web:jar:4.3.4.RELEASE:compile
    [INFO] |  |  |  +- org.springframework:spring-aop:jar:4.3.4.RELEASE:compile
    [INFO] |  |  |  \- org.springframework:spring-beans:jar:4.3.4.RELEASE:compile
    [INFO] |  |  \- org.springframework:spring-webmvc:jar:4.3.4.RELEASE:compile
    [INFO] |  |     \- org.springframework:spring-expression:jar:4.3.4.RELEASE:compile
    [INFO] |  +- org.thymeleaf:thymeleaf-spring4:jar:2.1.5.RELEASE:compile
    [INFO] |  |  +- org.thymeleaf:thymeleaf:jar:2.1.5.RELEASE:compile
    [INFO] |  |  |  +- ognl:ognl:jar:3.0.8:compile
    [INFO] |  |  |  +- org.javassist:javassist:jar:3.20.0-GA:compile
    [INFO] |  |  |  \- org.unbescape:unbescape:jar:1.1.0.RELEASE:compile
    [INFO] |  |  \- org.slf4j:slf4j-api:jar:1.7.21:compile
    [INFO] |  \- nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:jar:1.4.0:compile
    [INFO] |     \- org.codehaus.groovy:groovy:jar:2.4.7:compile
    [INFO] +- org.springframework.boot:spring-boot-starter-jetty:jar:1.4.2.RELEASE:compile
    [INFO] |  +- org.eclipse.jetty:jetty-servlets:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty:jetty-continuation:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty:jetty-http:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty:jetty-util:jar:9.3.14.v20161028:compile
    [INFO] |  |  \- org.eclipse.jetty:jetty-io:jar:9.3.14.v20161028:compile
    [INFO] |  +- org.eclipse.jetty:jetty-webapp:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty:jetty-xml:jar:9.3.14.v20161028:compile
    [INFO] |  |  \- org.eclipse.jetty:jetty-servlet:jar:9.3.14.v20161028:compile
    [INFO] |  |     \- org.eclipse.jetty:jetty-security:jar:9.3.14.v20161028:compile
    [INFO] |  |        \- org.eclipse.jetty:jetty-server:jar:9.3.14.v20161028:compile
    [INFO] |  +- org.eclipse.jetty.websocket:websocket-server:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty.websocket:websocket-common:jar:9.3.14.v20161028:compile
    [INFO] |  |  |  \- org.eclipse.jetty.websocket:websocket-api:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty.websocket:websocket-client:jar:9.3.14.v20161028:compile
    [INFO] |  |  \- org.eclipse.jetty.websocket:websocket-servlet:jar:9.3.14.v20161028:compile
    [INFO] |  |     \- javax.servlet:javax.servlet-api:jar:3.1.0:compile
    [INFO] |  +- org.eclipse.jetty.websocket:javax-websocket-server-impl:jar:9.3.14.v20161028:compile
    [INFO] |  |  +- org.eclipse.jetty:jetty-annotations:jar:9.3.14.v20161028:compile
    [INFO] |  |  |  +- org.eclipse.jetty:jetty-plus:jar:9.3.14.v20161028:compile
    [INFO] |  |  |  +- javax.annotation:javax.annotation-api:jar:1.2:compile
    [INFO] |  |  |  +- org.ow2.asm:asm:jar:5.0.1:compile
    [INFO] |  |  |  \- org.ow2.asm:asm-commons:jar:5.0.1:compile
    [INFO] |  |  |     \- org.ow2.asm:asm-tree:jar:5.0.1:compile
    [INFO] |  |  +- org.eclipse.jetty.websocket:javax-websocket-client-impl:jar:9.3.14.v20161028:compile
    [INFO] |  |  \- javax.websocket:javax.websocket-api:jar:1.0:compile
    [INFO] |  \- org.mortbay.jasper:apache-el:jar:8.0.33:compile
    [INFO] +- org.springframework.boot:spring-boot-devtools:jar:1.4.2.RELEASE:compile
    [INFO] |  +- org.springframework.boot:spring-boot:jar:1.4.2.RELEASE:compile
    [INFO] |  |  \- org.springframework:spring-context:jar:4.3.4.RELEASE:compile
    [INFO] |  \- org.springframework.boot:spring-boot-autoconfigure:jar:1.4.2.RELEASE:compile

_P.S Tested with Spring Boot 1.4.2.RELEASE_

[http://www.mkyong.com/spring-boot/spring-boot-jetty-as-embedded-server/](http://www.mkyong.com/spring-boot/spring-boot-jetty-as-embedded-server/)
