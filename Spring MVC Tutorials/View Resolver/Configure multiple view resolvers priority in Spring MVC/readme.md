## Problem

In Spring MVC application, often times, you may applying few view resolver strategies to resolve the view name. For example, combine three view resolvers together : **InternalResourceViewResolver**, **ResourceBundleViewResolver** and **XmlViewResolver**.

    <beans ...>
    	<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    	      <property name="location">
    	         <value>/WEB-INF/spring-views.xml</value>
    	      </property>
    	</bean>

    	<bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
    	      <property name="basename" value="spring-views" />
    	</bean>

    	<bean id="viewResolver"
    	      class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
                  <property name="prefix">
                     <value>/WEB-INF/pages/</value>
                  </property>
                  <property name="suffix">
                     <value>.jsp</value>
                  </property>
            </bean>
    </beans>

But, if a view name is returned, which view resolver strategy will be used?

## Solution

If multiple view resolver strategies are applied, you have to declare the priority through “**order**” property, where the **lower order value has a higher priority**, for example :

    <beans ...>
    	<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    	     <property name="location">
    	        <value>/WEB-INF/spring-views.xml</value>
    	     </property>
    	     <property name="order" value="0" />
    	</bean>

    	<bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
    	     <property name="basename" value="spring-views" />
    	     <property name="order" value="1" />
    	</bean>

    	<bean id="viewResolver"
    	      class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
                  <property name="prefix">
                     <value>/WEB-INF/pages/</value>
                  </property>
                  <property name="suffix">
                     <value>.jsp</value>
                  </property>
    	      <property name="order" value="2" />
            </bean>
    </beans>

Now, if a view name is returned, the view resolving strategy works in the following order :

    XmlViewResolver --> ResourceBundleViewResolver --> InternalResourceViewResolver

**Note**  
The **InternalResourceViewResolver** must always **assign with the lowest priority** (largest order number), because it will resolve the view no matter what view name is returned. It caused other view resolvers have no chance to resolve the view if they have lower priority.

[http://www.mkyong.com/spring-mvc/configure-multiple-view-resolvers-priority-in-spring-mvc/](http://www.mkyong.com/spring-mvc/configure-multiple-view-resolvers-priority-in-spring-mvc/)
