Often times, you may mix use of multiple handler mappings strategy in Spring MVC development.

For example, use [ControllerClassNameHandlerMapping](http://www.mkyong.com/spring-mvc/spring-mvc-controllerclassnamehandlermapping-example/) to map all the convention handler mappings, and [SimpleUrlHandlerMapping](http://www.mkyong.com/spring-mvc/spring-mvc-simpleurlhandlermapping-example/) to map other special handler mappings explicitly.

    <beans ...>

       <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
          <property name="mappings">
    		<value>
    			/index.htm=welcomeController
    			/welcome.htm=welcomeController
    			/main.htm=welcomeController
    			/home.htm=welcomeController
    		</value>
          </property>
          <property name="order" value="0" />
       </bean>

       <bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
          <property name="caseSensitive" value="true" />
          <property name="order" value="1" />
       </bean>

       <bean id="welcomeController"
          class="com.mkyong.common.controller.WelcomeController" />

       <bean class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

In above case, it’s important to specify the handler mapping priority, so that it won’t cause the conflict. You can set the priority via the “**order**” property, where the lower order value has the higher priority.

[http://www.mkyong.com/spring-mvc/configure-the-handler-mapping-priority-in-spring-mvc/](http://www.mkyong.com/spring-mvc/configure-the-handler-mapping-priority-in-spring-mvc/)
