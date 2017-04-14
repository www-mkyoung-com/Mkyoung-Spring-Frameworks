Spring comes with a “**ContextLoaderListener**” listener to enable Spring dependency injection into session listener. In this tutorial, it revises this [HttpSessionListener example](http://www.mkyong.com/servlet/a-simple-httpsessionlistener-example-active-sessions-counter/) by adding a Spring dependency injection a bean into the session listener.

## 1\. Spring Beans

Create a simple counter service to print total number of sessions created.

_File : CounterService.java_

    package com.mkyong.common;

    public class CounterService{

    	public void printCounter(int count){
    		System.out.println("Total session created : " + count);
    	}

    }

_File : counter.xml_ – Bean configuration file.

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="counterService" class="com.mkyong.common.CounterService" />

    </beans>

## 2\. WebApplicationContextUtils

Uses “`WebApplicationContextUtils`” to get the Spring’s context, and later you can get any declared Spring’s bean in a normal Spring’s way.

_File : SessionCounterListener.java_

    package com.mkyong.common;

    import javax.servlet.http.HttpSession;
    import javax.servlet.http.HttpSessionEvent;
    import javax.servlet.http.HttpSessionListener;
    import org.springframework.context.ApplicationContext;
    import org.springframework.web.context.support.WebApplicationContextUtils;

    public class SessionCounterListener implements HttpSessionListener {

         private static int totalActiveSessions;

         public static int getTotalActiveSession(){
               return totalActiveSessions;
         }

        @Override
        public void sessionCreated(HttpSessionEvent arg0) {
               totalActiveSessions++;
               System.out.println("sessionCreated - add one session into counter");
               printCounter(arg0);
        }

        @Override
        public void sessionDestroyed(HttpSessionEvent arg0) {
               totalActiveSessions--;
               System.out.println("sessionDestroyed - deduct one session from counter");
               printCounter(arg0);
        }

        private void printCounter(HttpSessionEvent sessionEvent){

              HttpSession session = sessionEvent.getSession();

              ApplicationContext ctx =
                    WebApplicationContextUtils.
                          getWebApplicationContext(session.getServletContext());

              CounterService counterService =
                          (CounterService) ctx.getBean("counterService");

              counterService.printCounter(totalActiveSessions);
        }
    }

## 3\. Integration

The only problem is, how your web application know where to load the Spring bean configuration file? The secret is inside the “web.xml” file.

1.  Register “`ContextLoaderListener`” as the first listener to make your web application aware of the Spring context loader.
2.  Configure the “`contextConfigLocation`” and define your Spring’s bean configuration file.

_File : web.xml_

    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >

    <web-app>
      <display-name>Archetype Created Web Application</display-name>

      <context-param>
    	<param-name>contextConfigLocation</param-name>
    	<param-value>/WEB-INF/Spring/counter.xml</param-value>
      </context-param>

      <listener>
            <listener-class>
                org.springframework.web.context.ContextLoaderListener
            </listener-class>
      </listener>

      <listener>
    	<listener-class>
                com.mkyong.common.SessionCounterListener
            </listener-class>
      </listener>

      <servlet>
    	<servlet-name>Spring DI Servlet Listener</servlet-name>
    	<servlet-class>com.mkyong.common.App</servlet-class>
      </servlet>

      <servlet-mapping>
    	<servlet-name>Spring DI Servlet Listener</servlet-name>
    	<url-pattern>/Demo</url-pattern>
      </servlet-mapping>

    </web-app>

_File : App.java_

    package com.mkyong.common;

    import java.io.IOException;
    import java.io.PrintWriter;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.servlet.http.HttpSession;

    public class App extends HttpServlet{

      public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException{

            HttpSession session = request.getSession(); //sessionCreated() is executed
            session.setAttribute("url", "mkyong.com");
            session.invalidate();  //sessionDestroyed() is executed

            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<body>");
            out.println("<h1>Spring Dependency Injection into Servlet Listenner</h1>");
            out.println("</body>");
            out.println("</html>");

       }
    }

Start Tomcat, and access the URL “_http://localhost:8080/SpringWebExample/Demo_“.

_output_

    sessionCreated - add one session into counter
    Total session created : 1
    sessionDestroyed - deduct one session from counter
    Total session created : 0

See the console output, you get the counter service bean via Spring DI, and print the total number of sessions.

## Conclusion

In Spring, the “`ContextLoaderListener`” is a generic way to **integrate Spring Dependency Injection to almost all of the web application**.

[http://www.mkyong.com/spring/spring-how-to-do-dependency-injection-in-your-session-listener/](http://www.mkyong.com/spring/spring-how-to-do-dependency-injection-in-your-session-listener/)
