## Problem

The Spring AOP transaction is not working in following interceptors?

    <bean id="testAutoProxyCreator"
        class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    	<property name="interceptorNames">
    		<list>
    			<idref bean="urlInterceptorInsert" />
    			<idref bean="urlInterceptorCommit" />
    			<idref bean="urlInterceptorRelease" />
    			<idref bean="matchGenericTxInterceptor" />
    		</list>
    	</property>
    	<property name="beanNames">
    		<list>
    			<idref local="urlBo" />
    		</list>
    	</property>
    </bean>

The “**matchGenericTxInterceptor**” transaction interceptor, suppose to intercept `urlInterceptorInsert`, `urlInterceptorCommit`,`urlInterceptorRelease`, but it’s not work as expected?

## Solution

The 3 interceptors are executed before transaction manager interceptor (**matchGenericTxInterceptor**).

To fix it, you have to change the sequence of the interceptor xml file like following (put **matchGenericTxInterceptor** on top).

    <bean id="testAutoProxyCreator"
            class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    	<property name="interceptorNames">
    		<list>
                <idref bean="matchGenericTxInterceptor" />
    			<idref bean="urlInterceptorInsert" />
    			<idref bean="urlInterceptorCommit" />
    			<idref bean="urlInterceptorRelease" />
    		</list>
    	</property>
    	<property name="beanNames">
    		<list>
    			<idref local="urlBo" />
    		</list>
    	</property>
    </bean>

[http://www.mkyong.com/spring/spring-aop-interceptor-transaction-is-not-working/](http://www.mkyong.com/spring/spring-aop-interceptor-transaction-is-not-working/)
