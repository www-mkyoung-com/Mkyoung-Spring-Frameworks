In Spring MVC application, the **SimpleUrlHandlerMapping** is the most flexible handler mapping class, which allow developer to specify the mapping of URL pattern and handlers explicitly.

The **SimpleUrlHandlerMapping** can be declared in two ways.

## 1\. Method 1 – prop key

The property keys are the URL patterns while the property values are the handler IDs or names.

    <beans ...>

    	<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    	   <property name="mappings">
    		<props>
    		   <prop key="/welcome.htm">welcomeController</prop>
    		   <prop key="/*/welcome.htm">welcomeController</prop>
    		   <prop key="/helloGuest.htm">helloGuestController</prop>
    		 </props>
    	   </property>
    	</bean>

    	<bean id="welcomeController"
    		class="com.mkyong.common.controller.WelcomeController" />

    	<bean id="helloGuestController"
    		class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

## 2\. Method 1 – value

The left side are the URL patterns while the right side are the handler IDs or names, separate by a equal symbol “=”.

    <beans ...>

    	<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    	   <property name="mappings">
    		<value>
    		   /welcome.htm=welcomeController
    		   /*/welcome.htm=welcomeController
    		   /helloGuest.htm=helloGuestController
    		</value>
    	   </property>
    	</bean>

    	<bean id="welcomeController"
    		class="com.mkyong.common.controller.WelcomeController" />

    	<bean id="helloGuestController"
    		class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

## 3\. Demo

Both are defined the same handler mappings.

1.  /welcome.htm –> welcomeController.
2.  /{anything}/welcome.htm –> welcomeController.
3.  /helloGuest.htm –> helloGuestController.

[http://www.mkyong.com/spring-mvc/spring-mvc-simpleurlhandlermapping-example/](http://www.mkyong.com/spring-mvc/spring-mvc-simpleurlhandlermapping-example/)
