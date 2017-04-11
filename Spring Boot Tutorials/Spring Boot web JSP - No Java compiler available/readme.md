Spring boot web application with embeded Tomcat and JSP. Run and access the JSP page, but hits the following errors

    $ mvn spring-boot:run
    ...
    java.lang.IllegalStateException: No Java compiler available
            at org.apache.jasper.JspCompilationContext.createCompiler(JspCompilationContext.java:235) ~[tomcat-embed-jasper-8.5.6.jar:8.5.6]
            at org.apache.jasper.JspCompilationContext.compile(JspCompilationContext.java:592) ~[tomcat-embed-jasper-8.5.6.jar:8.5.6]
            at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:368) ~[tomcat-embed-jasper-8.5.6.jar:8.5.6]
            at org.apache.jasper.servlet.JspServlet.serviceJspFile(JspServlet.java:385) ~[tomcat-embed-jasper-8.5.6.jar:8.5.6]
            at org.apache.jasper.servlet.JspServlet.service(JspServlet.java:329) ~[tomcat-embed-jasper-8.5.6.jar:8.5.6]
            at javax.servlet.http.HttpServlet.service(HttpServlet.java:729) [tomcat-embed-core-8.5.6.jar:8.5.6]
            at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:230) [tomcat-embed-core-8.5.6.jar:8.5.6]
            at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165) [tomcat-embed-core-8.5.6.jar:8.5.6]
            at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52) [tomcat-embed-websocket-8.5.6.jar:8.5.6]
            at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192) [tomcat-embed-core-8.5.6.jar:8.5.6]
            at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165) [tomcat-embed-core-8.5.6.jar:8.5.6]
            at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:101) [spring-web-4.3.4.RELEASE.jar:4.3.4.RELEASE]
            at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)

Environment :

1.  Spring Boot 1.4.2.RELEASE
2.  Tomcat Embed 8.5.6

## 1\. Spring Boot Environment

To compile JSP, need `tomcat-embed-jasper`.

pom.xml

    <parent>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-parent</artifactId>
    	<version>1.4.2.RELEASE</version>
    </parent>

    <dependencies>

    	<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-web</artifactId>
    	</dependency>
    	<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-tomcat</artifactId>
    		<scope>provided</scope>
    	</dependency>

    	<dependency>
    		<groupId>org.apache.tomcat.embed</groupId>
    		<artifactId>tomcat-embed-jasper</artifactId>
    		<scope>provided</scope>
    	</dependency>

    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>jstl</artifactId>
    	</dependency>

    </dependencies>

Show project dependencies :

    $ mvn dependency:tree

    [INFO] +- org.springframework.boot:spring-boot-starter-tomcat:jar:1.4.2.RELEASE:provided
    [INFO] |  +- org.apache.tomcat.embed:tomcat-embed-core:jar:8.5.6:provided
    [INFO] |  +- org.apache.tomcat.embed:tomcat-embed-el:jar:8.5.6:provided
    [INFO] |  \- org.apache.tomcat.embed:tomcat-embed-websocket:jar:8.5.6:provided
    [INFO] +- org.apache.tomcat.embed:tomcat-embed-jasper:jar:8.5.6:provided
    [INFO] |  \- org.eclipse.jdt.core.compiler:ecj:jar:4.5.1:provided
    [INFO] \- javax.servlet:jstl:jar:1.2:compile

## 2\. Solution

No idea why `tomcat-embed-jasper` didn’t pick up the ecj compiler, to fix this, declares the Eclipse `ecj` manually :

pom.xml

    <parent>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-parent</artifactId>
    	<version>1.4.2.RELEASE</version>
    </parent>

    <dependencies>

    	<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-web</artifactId>
    	</dependency>
    	<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-tomcat</artifactId>
    		<scope>provided</scope>
    	</dependency>

    	<dependency>
    		<groupId>org.apache.tomcat.embed</groupId>
    		<artifactId>tomcat-embed-jasper</artifactId>
    		<scope>provided</scope>
    	</dependency>

    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>jstl</artifactId>
    	</dependency>

    	<!-- Add this -->
    	<dependency>
    		<groupId>org.eclipse.jdt.core.compiler</groupId>
    		<artifactId>ecj</artifactId>
    		<version>4.6.1</version>
    		<scope>provided</scope>
    	</dependency>

    </dependencies>

Show project dependencies :

    $ mvn dependency:tree

    [INFO] +- org.springframework.boot:spring-boot-starter-tomcat:jar:1.4.2.RELEASE:provided
    [INFO] |  +- org.apache.tomcat.embed:tomcat-embed-core:jar:8.5.6:provided
    [INFO] |  +- org.apache.tomcat.embed:tomcat-embed-el:jar:8.5.6:provided
    [INFO] |  \- org.apache.tomcat.embed:tomcat-embed-websocket:jar:8.5.6:provided
    [INFO] +- org.apache.tomcat.embed:tomcat-embed-jasper:jar:8.5.6:provided
    [INFO] +- javax.servlet:jstl:jar:1.2:compile
    [INFO] \- org.eclipse.jdt.core.compiler:ecj:jar:4.6.1:provided

[http://www.mkyong.com/spring-boot/spring-boot-web-jsp-no-java-compiler-available/](http://www.mkyong.com/spring-boot/spring-boot-web-jsp-no-java-compiler-available/)
