In this [Spring auto component scanning tutorial](http://www.mkyong.com/spring/spring-auto-scanning-components/), you learn about how to make Spring auto scan your components. In this article, we show you how to do component filter in auto scanning process.

## 1\. Filter component – include

See following example to use Spring “**filtering**” to scan and register components’ name which matched defined “regex”, even the class is not annotated with @Component.

DAO layer

    package com.mkyong.customer.dao;

    public class CustomerDAO
    {
    	@Override
    	public String toString() {
    		return "Hello , This is CustomerDAO";
    	}
    }

Service layer

    package com.mkyong.customer.services;

    import org.springframework.beans.factory.annotation.Autowired;
    import com.mkyong.customer.dao.CustomerDAO;

    public class CustomerService
    {
    	@Autowired
    	CustomerDAO customerDAO;

    	@Override
    	public String toString() {
    		return "CustomerService [customerDAO=" + customerDAO + "]";
    	}

    }

Spring filtering.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">

    	<context:component-scan base-package="com.mkyong" >

    		<context:include-filter type="regex"
                           expression="com.mkyong.customer.dao.*DAO.*" />

    		<context:include-filter type="regex"
                           expression="com.mkyong.customer.services.*Service.*" />

    	</context:component-scan>

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

Output

    CustomerService [customerDAO=Hello , This is CustomerDAO]

In this XML filtering, all files’s name contains DAO or Service (*DAO.*, *Services.*) word will be detect and register in Spring container.

## 2\. Filter component – exclude

On the other hand, you can also exclude specified components, to avoid Spring to detect and register it in Spring container.

Exclude those files annotated with @Service.

    <context:component-scan base-package="com.mkyong.customer" >
    	<context:exclude-filter type="annotation"
    		expression="org.springframework.stereotype.Service" />
    </context:component-scan>

Exclude those files name contains DAO word.

    <context:component-scan base-package="com.mkyong" >
    	<context:exclude-filter type="regex"
    		expression="com.mkyong.customer.dao.*DAO.*" />
    </context:component-scan>

[http://www.mkyong.com/spring/spring-filtering-components-in-auto-scanning/](http://www.mkyong.com/spring/spring-filtering-components-in-auto-scanning/)
