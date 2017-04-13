In last Spring AOP examples – [advice](http://www.mkyong.com/spring/spring-aop-examples-advice/), [pointcut and advisor](http://www.mkyong.com/spring/spring-aop-example-pointcut-advisor/), you have to manually create a proxy bean (ProxyFactoryBean) for each beans whose need AOP support.

This is not an efficient way, for example, if you want all of your DAO classes in customer module to implement the AOP feature with SQL logging support (advise), then you have to create many proxy factory beans manually, as a result you just flood your bean configuration file with tons of proxy beans.

Fortunately, Spring comes with two auto proxy creators to create proxies for your beans automatically.

## 1\. BeanNameAutoProxyCreator example

Before that, you have to create a proxy bean (ProxyFactoryBean) manually.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerService" class="com.mkyong.customer.services.CustomerService">
    		<property name="name" value="Yong Mook Kim" />
    		<property name="url" value="http://www.mkyong.com" />
    	</bean>

    	<bean id="hijackAroundMethodBeanAdvice" class="com.mkyong.aop.HijackAroundMethod" />

    	<bean id="customerServiceProxy"
    	    class="org.springframework.aop.framework.ProxyFactoryBean">

    		<property name="target" ref="customerService" />

    		<property name="interceptorNames">
    			<list>
    				<value>customerAdvisor</value>
    			</list>
    		</property>
    	</bean>

    	<bean id="customerAdvisor"
    		class="org.springframework.aop.support.NameMatchMethodPointcutAdvisor">
    		<property name="mappedName" value="printName" />
    		<property name="advice" ref="hijackAroundMethodBeanAdvice" />
    	</bean>
    </beans>

And get the bean with the bean with proxy name “customerServiceProxy”.

    CustomerService cust = (CustomerService)appContext.getBean("customerServiceProxy");

In auto proxy mechanism, you just need to create a **BeanNameAutoProxyCreator**, and include all your beans (via bean name, or regular expression name) and ‘advisor’ into a single unit.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerService" class="com.mkyong.customer.services.CustomerService">
    		<property name="name" value="Yong Mook Kim" />
    		<property name="url" value="http://www.mkyong.com" />
    	</bean>

    	<bean id="hijackAroundMethodBeanAdvice" class="com.mkyong.aop.HijackAroundMethod" />

    	<bean id="customerAdvisor"
    		class="org.springframework.aop.support.NameMatchMethodPointcutAdvisor">
    		<property name="mappedName" value="printName" />
    		<property name="advice" ref="hijackAroundMethodBeanAdvice" />
    	</bean>

    	<bean
    		class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    		<property name="beanNames">
    			<list>
    				<value>*Service</value>
    			</list>
    		</property>
    		<property name="interceptorNames">
    			<list>
    				<value>customerAdvisor</value>
    			</list>
    		</property>
    	</bean>
    </beans>

Now, you can get the bean via original name “customerService”, you dun even know this bean has been proxies.

    CustomerService cust = (CustomerService)appContext.getBean("customerService");

## 2\. DefaultAdvisorAutoProxyCreator example

This **DefaultAdvisorAutoProxyCreator** is extremely powerful, if any of the beans is matched by an advisor, Spring will create a proxy for it automatically.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerService" class="com.mkyong.customer.services.CustomerService">
    		<property name="name" value="Yong Mook Kim" />
    		<property name="url" value="http://www.mkyong.com" />
    	</bean>

    	<bean id="hijackAroundMethodBeanAdvice" class="com.mkyong.aop.HijackAroundMethod" />

    	<bean id="customerAdvisor"
    		class="org.springframework.aop.support.NameMatchMethodPointcutAdvisor">
    		<property name="mappedName" value="printName" />
    		<property name="advice" ref="hijackAroundMethodBeanAdvice" />
    	</bean>

           <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" />

    </beans>

This is just over power, since you don’t have control what bean should be proxy, what you can do is just trust Spring will do the best for you. Please take good care if you want to implement this into your project.

[http://www.mkyong.com/spring/spring-auto-proxy-creator-example/](http://www.mkyong.com/spring/spring-auto-proxy-creator-example/)
