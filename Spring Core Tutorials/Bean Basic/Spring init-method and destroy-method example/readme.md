In Spring, you can use **init-method** and **destroy-method** as attribute in bean configuration file for bean to perform certain actions upon initialization and destruction. Alternative to [InitializingBean and DisposableBean interface](http://www.mkyong.com/spring/spring-initializingbean-and-disposablebean-example/).

## Example

Hereâ€™s an example to show you how to use **init-method** and **destroy-method**.

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

    	public void initIt() throws Exception {
    	  System.out.println("Init method after properties are set : " + message);
    	}

    	public void cleanUp() throws Exception {
    	  System.out.println("Spring Container is destroy! Customer clean up");
    	}

    }

_File : Spring-Customer.xml_, define **init-method** and **destroy-method** attribute in your bean.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerService" class="com.mkyong.customer.services.CustomerService"
    		init-method="initIt" destroy-method="cleanUp">

    		<property name="message" value="i'm property message" />
    	</bean>

    </beans>

Run it

    package com.mkyong.common;

    import org.springframework.context.ConfigurableApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    import com.mkyong.customer.services.CustomerService;

    public class App
    {
        public static void main( String[] args )
        {
        	ConfigurableApplicationContext context =
    		new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});

        	CustomerService cust = (CustomerService)context.getBean("customerService");

        	System.out.println(cust);

        	context.close();
        }
    }

The ConfigurableApplicationContext.close will close the application context, releasing all resources and destroying all cached singleton beans.  

_Output_

    Init method after properties are set : i'm property message
    com.mkyong.customer.services.CustomerService@47393f
    ...
    INFO: Destroying singletons in org.springframework.beans.factory.
    support.DefaultListableBeanFactory@77158a:
    defining beans [customerService]; root of factory hierarchy
    Spring Container is destroy! Customer clean up

The **initIt()** method is called, after the message property is set, and the **cleanUp()** method is called after the context.close();

[http://www.mkyong.com/spring/spring-init-method-and-destroy-method-example/](http://www.mkyong.com/spring/spring-init-method-and-destroy-method-example/)
