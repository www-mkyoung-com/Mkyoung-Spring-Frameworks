In Spring MVC, **ControllerClassNameHandlerMapping** use convention to map requested URL to Controller (convention over configuration). It takes the Class name, remove the ‘Controller’ suffix if exists and return the remaining text, lower-cased and with a leading “/”.

See following few examples to demonstrate the use of this `ControllerClassNameHandlerMapping` class.

## 1\. Before and After

By default, Spring MVC is using the `BeanNameUrlHandlerMapping` handler mapping.

    <beans ...>

      <bean name="/welcome.htm"
            class="com.mkyong.common.controller.WelcomeController" />

      <bean name="/helloGuest.htm"
            class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

To enable the **ControllerClassNameHandlerMapping**, declared it in the bean configuration file, and now **the controller’s bean’s name is no longer required**.

    <beans ...>

      <bean
       class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

      <bean class="com.mkyong.common.controller.WelcomeController" />

      <bean class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

Now, Spring MVC is mapping the requested URL by following conventions :

    WelcomeController -> /welcome*
    HelloGuestController -> /helloguest*

1.  /welcome.htm –> WelcomeController.
2.  /welcomeHome.htm –> WelcomeController.
3.  /helloguest.htm –> HelloGuestController.
4.  /helloguest12345.htm –> HelloGuestController.
5.  /helloGuest.htm, failed to map **/helloguest***, the “g” case is not match.

## 2\. Case sensitive

To solve the case sensitive issue stated above, declared the “**caseSensitive**” property and set it to true.

    <beans ...>

      <bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
           <property name="caseSensitive" value="true" />
      </bean>

      <bean class="com.mkyong.common.controller.WelcomeController" />

      <bean class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

Now, Spring MVC is mapping the requested URL by the following conventions :

    WelcomeController -> /welcome*
    HelloGuestController -> /helloGuest*

1.  /helloGuest.htm –> HelloGuestController.
2.  /helloguest.htm, failed to map “/helloGuest*”, the “G” case is not match.

## 3\. pathPrefix

Additionally, you can specify a prefix to maps the requested URL, declared a “**pathPrefix**” property.

    <beans ...>

      <bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
    	 <property name="caseSensitive" value="true" />
    	 <property name="pathPrefix" value="/customer" />
      </bean>

      <bean class="com.mkyong.common.controller.WelcomeController" />

      <bean class="com.mkyong.common.controller.HelloGuestController" />

    </beans>

Now, Spring MVC is mapping the requested URL by the following conventions :

    WelcomeController -> /customer/welcome*
    HelloGuestController -> /customer/helloGuest*

1.  /customer/welcome.htm –> WelcomeController.
2.  /customer/helloGuest.htm –> HelloGuestController.
3.  /welcome.htm, failed.
4.  /helloGuest.htm, failed.

[http://www.mkyong.com/spring-mvc/spring-mvc-controllerclassnamehandlermapping-example/](http://www.mkyong.com/spring-mvc/spring-mvc-controllerclassnamehandlermapping-example/)
