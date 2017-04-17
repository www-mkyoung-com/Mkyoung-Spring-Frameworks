## Problem

In Spring MVC application, the 404 error code is configured properly. See the following web.xml snippet.

_File : web.xml_

    <web-app ...>

      <servlet>
      	<servlet-name>mvc-dispatcher</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <load-on-startup>1</load-on-startup>
      </servlet>

      <servlet-mapping>
     	<servlet-name>mvc-dispatcher</servlet-name>
            <url-pattern>*.htm</url-pattern>
      </servlet-mapping>

      //...
      <error-page>
    	<error-code>404</error-code>
    	<location>/WEB-INF/pages/404.htm</location>
      </error-page>

    </web-app>

However, when user access any non-exist resources, it will **display a blank page instead of the 404.htm**.

## Solution

The 404 error code is configured properly, but it will caused the “**.htm**” extension handling **conflict** between the “**servlet container**” and Spring’s “**DispatcherServlet**“. To solve it, try change the 404.htm to other file extension, for example 404.jsp.

_File : web.xml_

    <web-app ...>

      <servlet>
      	<servlet-name>mvc-dispatcher</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <load-on-startup>1</load-on-startup>
      </servlet>

      <servlet-mapping>
     	<servlet-name>mvc-dispatcher</servlet-name>
            <url-pattern>*.htm</url-pattern>
      </servlet-mapping>

      //...
      <error-page>
    	<error-code>404</error-code>
    	<location>/WEB-INF/pages/404.jsp</location>
      </error-page>

    </web-app>

Now, when user access any non-exist resources, it will forward to the 404.jsp page now.
[http://www.mkyong.com/spring-mvc/404-error-code-is-not-working-in-spring-mvc/](http://www.mkyong.com/spring-mvc/404-error-code-is-not-working-in-spring-mvc/)
