In Spring, bean scope is used to decide which type of bean instance should be return from Spring container back to the caller.

5 types of bean scopes supported :

1.  singleton – Return a single bean instance per Spring IoC container
2.  prototype – Return a new bean instance each time when requested
3.  request – Return a single bean instance per HTTP request. *
4.  session – Return a single bean instance per HTTP session. *
5.  globalSession – Return a single bean instance per global HTTP session. *

In most cases, you may only deal with the Spring’s core scope – singleton and prototype, and the default scope is singleton.

_P.S * means only valid in the context of a web-aware Spring ApplicationContext_

## Singleton vs Prototype

Here’s an example to show you what’s the different between bean scope : **singleton** and **prototype**.

    package com.mkyong.customer.services;

    public class CustomerService
    {
    	String message;

    	public String getMessage() {
    		return message;
    	}

    	public void setMessage(String message) {
    		this.message = message;
    	}
    }

## 1\. Singleton example

If no bean scope is specified in bean configuration file, default to singleton.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

           <bean id="customerService"
                class="com.mkyong.customer.services.CustomerService" />

    </beans>

Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    import com.mkyong.customer.services.CustomerService;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        	 new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});

        	CustomerService custA = (CustomerService)context.getBean("customerService");
        	custA.setMessage("Message by custA");
        	System.out.println("Message : " + custA.getMessage());

        	//retrieve it again
        	CustomerService custB = (CustomerService)context.getBean("customerService");
        	System.out.println("Message : " + custB.getMessage());
        }
    }

Output

    Message : Message by custA
    Message : Message by custA

Since the bean ‘customerService’ is in singleton scope, the second retrieval by ‘custB’ will display the message set by ‘custA’ also, even it’s retrieve by a new getBean() method. In singleton, only a single instance per Spring IoC container, no matter how many time you retrieve it with getBean(), it will always return the same instance.

## 2\. Prototype example

If you want a new ‘customerService’ bean instance, every time you call it, use prototype instead.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       <bean id="customerService" class="com.mkyong.customer.services.CustomerService"
             scope="prototype"/>

    </beans>

Run it again

    Message : Message by custA
    Message : null

In prototype scope, you will have a new instance for each `getBean()` method called.

## 3\. Bean scopes annotation

You can also use annotation to define your bean scope.

    package com.mkyong.customer.services;

    import org.springframework.context.annotation.Scope;
    import org.springframework.stereotype.Service;

    @Service
    @Scope("prototype")
    public class CustomerService
    {
    	String message;

    	public String getMessage() {
    		return message;
    	}

    	public void setMessage(String message) {
    		this.message = message;
    	}
    }

Enable auto component scanning

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">

           <context:component-scan base-package="com.mkyong.customer" />

    </beans>

[http://www.mkyong.com/spring/spring-bean-scopes-examples/](http://www.mkyong.com/spring/spring-bean-scopes-examples/)
