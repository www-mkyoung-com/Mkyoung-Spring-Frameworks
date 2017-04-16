In Spring MVC, **BeanNameUrlHandlerMapping** is the default handler mapping mechanism, which maps **URL requests to the name of the beans**. For example,

    <beans ...>

       <bean
    	class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>

       <bean name="/welcome.htm"
            class="com.mkyong.common.controller.WelcomeController" />

       <bean name="/streetName.htm"
            class="com.mkyong.common.controller.StreetNameController" />

       <bean name="/process*.htm"
            class="com.mkyong.common.controller.ProcessController" />

    </beans>

In above example, If URI pattern

1.  **/welcome.htm** is requested, DispatcherServlet will forward the request to the “`WelcomeController`“.
2.  **/streetName.htm** is requested, DispatcherServlet will forward the request to the “`StreetNameController`“.
3.  **/processCreditCard.htm** or **/process{any thing}.htm** is requested, DispatcherServlet will forward the request to the “`ProcessController`“.

**Note**  
Additionally, this mapping is support Ant style regex pattern match, see this [AntPathMatcher javadoc](http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/util/AntPathMatcher.html) for details.

Actually, declare **BeanNameUrlHandlerMapping** is optional, by default, if Spring can’t found handler mapping, the DispatcherServlet will creates a **BeanNameUrlHandlerMapping** automatically.

So, the above web.xml file is equivalence to the following web.xml:

    <beans ...>

       <bean name="/welcome.htm"
                class="com.mkyong.common.controller.WelcomeController" />

       <bean name="/streetName.htm"
                class="com.mkyong.common.controller.StreetNameController" />

       <bean name="/process*.htm"
                class="com.mkyong.common.controller.ProcessController" />

    </beans>

[http://www.mkyong.com/spring-mvc/spring-mvc-beannameurlhandlermapping-example/](http://www.mkyong.com/spring-mvc/spring-mvc-beannameurlhandlermapping-example/)
