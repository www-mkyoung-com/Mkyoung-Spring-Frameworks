## Problem

In Spring MVC application, while changing the locale with “**org.springframework.web.servlet.i18n.LocaleChangeInterceptor**“, it hits the following error

    java.lang.UnsupportedOperationException:
         Cannot change HTTP accept header - use a different locale resolution strategy
         ...AcceptHeaderLocaleResolver.setLocale(AcceptHeaderLocaleResolver.java:45)

## Solution

In Spring MVC application, if you do not configure the Spring’s LocaleResolver, it will use the default **AcceptHeaderLocaleResolver**, which does not allow to change the locale. To solve it, try declare a **SessionLocaleResolver** bean in the Spring bean configuration file, it should be suits in most cases.

    <beans ...

    	<bean id="localeResolver"
    		class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
    		<property name="defaultLocale" value="en" />
    	</bean>

    	<bean id="localeChangeInterceptor"
    		class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
    		<property name="paramName" value="language" />
    	</bean>

    	<bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
    		<property name="interceptors">
    			<list>
    				<ref bean="localeChangeInterceptor" />
    			</list>
    		</property>
    	</bean>

    </beans>

[http://www.mkyong.com/spring-mvc/cannot-change-http-accept-header-use-a-different-locale-resolution-strategy/](http://www.mkyong.com/spring-mvc/cannot-change-http-accept-header-use-a-different-locale-resolution-strategy/)
