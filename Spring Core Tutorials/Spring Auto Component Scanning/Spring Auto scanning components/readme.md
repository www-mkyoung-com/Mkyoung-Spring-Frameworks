Normally you declare all the beans or components in XML bean configuration file, so that Spring container can detect and register your beans or components. Actually, Spring is able to auto scan, detect and instantiate your beans from pre-defined project package, no more tedious beans declaration in in XML file.

Following is a simple Spring project, including a customer service and dao layer. Let’s explore the different between declare components manually and auto components scanning in Spring.

## 1\. Declares Components Manually

See a normal way to declare a bean in Spring.

Normal bean.

    package com.mkyong.customer.dao;

    public class CustomerDAO
    {
    	@Override
    	public String toString() {
    		return "Hello , This is CustomerDAO";
    	}
    }

DAO layer.

    package com.mkyong.customer.services;

    import com.mkyong.customer.dao.CustomerDAO;

    public class CustomerService
    {
    	CustomerDAO customerDAO;

    	public void setCustomerDAO(CustomerDAO customerDAO) {
    		this.customerDAO = customerDAO;
    	}

    	@Override
    	public String toString() {
    		return "CustomerService [customerDAO=" + customerDAO + "]";
    	}

    }

Bean configuration file (Spring-Customer.xml), a normal bean configuration in Spring.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerService" class="com.mkyong.customer.services.CustomerService">
    		<property name="customerDAO" ref="customerDAO" />
    	</bean>

    	<bean id="customerDAO" class="com.mkyong.customer.dao.CustomerDAO" />

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

        	CustomerService cust = (CustomerService)context.getBean("customerService");
        	System.out.println(cust);

        }
    }

output

    CustomerService [customerDAO=Hello , This is CustomerDAO]

## 2\. Auto Components Scanning

Now, enable Spring auto component scanning features.

Annotate with **@Component** to indicate this is class is an auto scan component.

    package com.mkyong.customer.dao;

    import org.springframework.stereotype.Component;

    @Component
    public class CustomerDAO
    {
    	@Override
    	public String toString() {
    		return "Hello , This is CustomerDAO";
    	}
    }

DAO layer, add **@Component** to indicate this is an auto scan component also.

    package com.mkyong.customer.services;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import com.mkyong.customer.dao.CustomerDAO;

    @Component
    public class CustomerService
    {
    	@Autowired
    	CustomerDAO customerDAO;

    	@Override
    	public String toString() {
    		return "CustomerService [customerDAO=" + customerDAO + "]";
    	}
    }

Put this “`context:component`” in bean configuration file, it means, enable auto scanning feature in Spring. The **base-package** is indicate where are your components stored, Spring will scan this folder and find out the bean (annotated with @Component) and register it in Spring container.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">

    	<context:component-scan base-package="com.mkyong.customer" />

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
        	   new ClassPathXmlApplicationContext(new String[] {"Spring-AutoScan.xml"});

        	CustomerService cust = (CustomerService)context.getBean("customerService");
        	System.out.println(cust);

        }
    }

output

    CustomerService [customerDAO=Hello , This is CustomerDAO]

This is how auto components scanning works in Spring.

## Custom auto scan component name

By default, Spring will lower case the first character of the component – from ‘CustomerService’ to ‘customerService’. And you can retrieve this component with name ‘customerService’.

    CustomerService cust = (CustomerService)context.getBean("customerService");

To create a custom name for component, you can put custom name like this :

    @Service("AAA")
    public class CustomerService
    ...

Now, you can retrieve it with this name ‘AAA’.

    CustomerService cust = (CustomerService)context.getBean("AAA");

## Auto Components Scan Annotation Types

In Spring 2.5, there are 4 types of auto components scan annotation types

*   @Component – Indicates a auto scan component.
*   @Repository – Indicates DAO component in the persistence layer.
*   @Service – Indicates a Service component in the business layer.
*   @Controller – Indicates a controller component in the presentation layer.

So, which one to use? It’s really doesn’t matter. Let see the source code of `@Repository`,`@Service` or `@Controller`.

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface Repository {

    	String value() default "";

    }

You will noticed that all `@Repository`,`@Service` or `@Controller` are annotated with `@Component`. So, can we use just @Component for all the components for auto scanning? Yes, you can, and Spring will auto scan all your components with @Component annotated.

It’s working fine, but not a good practice, for readability, you should always declare @Repository,@Service or @Controller for a specified layer to make your code more easier to read, as following :

_DAO layer_

    package com.mkyong.customer.dao;

    import org.springframework.stereotype.Repository;

    @Repository
    public class CustomerDAO
    {
    	@Override
    	public String toString() {
    		return "Hello , This is CustomerDAO";
    	}
    }

_Service layer_

    package com.mkyong.customer.services;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import com.mkyong.customer.dao.CustomerDAO;

    @Service
    public class CustomerService
    {
    	@Autowired
    	CustomerDAO customerDAO;

    	@Override
    	public String toString() {
    		return "CustomerService [customerDAO=" + customerDAO + "]";
    	}

    }

[http://www.mkyong.com/spring/spring-auto-scanning-components/](http://www.mkyong.com/spring/spring-auto-scanning-components/)
