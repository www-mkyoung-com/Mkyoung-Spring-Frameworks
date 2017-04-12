Normally, you will [split a large Spring XML bean files](http://www.mkyong.com/spring/load-multiple-spring-bean-configuration-file/) into multiple small files, group by module or category, to make things more maintainable and modular. For example,

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<import resource="config/customer.xml"/>
            <import resource="config/scheduler.xml"/>

    </beans>

In Spring3 JavaConfig, the equivalent functionality is **@Import**.

    package com.mkyong.config;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Import;

    @Configuration
    @Import({ CustomerConfig.class, SchedulerConfig.class })
    public class AppConfig {

    }

## @Import Example

See a full example of using JavaConfig **@Import**.

## 1\. Directory Structure

Directory structure of this example.

![directory structure of this example](http://www.mkyong.com/wp-content/uploads/2011/06/spring-javaconfig-import.png)

## 2\. Spring Beans

Two simple Spring beans.

_File : CustomerBo.java_

    package com.mkyong.core;

    public class CustomerBo {

    	public void printMsg(String msg) {

    		System.out.println("CustomerBo : " + msg);
    	}

    }

_File : SchedulerBo.java_

    package com.mkyong.core;

    public class SchedulerBo {

    	public void printMsg(String msg) {

    		System.out.println("SchedulerBo : " + msg);
    	}

    }

## 3\. @Configuration example

Now, use JavaConfig **@Configuration** to declare above beans.

_File : CustomerConfig.java_

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import com.mkyong.core.CustomerBo;

    @Configuration
    public class CustomerConfig {

    	@Bean(name="customer")
    	public CustomerBo customerBo(){

    		return new CustomerBo();

    	}
    }

_File : SchedulerConfig.java_

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import com.mkyong.core.SchedulerBo;

    @Configuration
    public class SchedulerConfig {

    	@Bean(name="scheduler")
    	public SchedulerBo suchedulerBo(){

    		return new SchedulerBo();

    	}

    }

## 4\. @Import example

Use **@Import** to load multiple configuration files.

_File : AppConfig.java_

    package com.mkyong.config;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Import;

    @Configuration
    @Import({ CustomerConfig.class, SchedulerConfig.class })
    public class AppConfig {

    }

## 5\. Run it

Load the main configuration file , and test it.

    package com.mkyong.core;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.AnnotationConfigApplicationContext;
    import com.mkyong.config.AppConfig;

    public class App {
    	public static void main(String[] args) {

    		ApplicationContext context = new AnnotationConfigApplicationContext(
    				AppConfig.class);

    		CustomerBo customer = (CustomerBo) context.getBean("customer");
    		customer.printMsg("Hello 1");

    		SchedulerBo scheduler = (SchedulerBo) context.getBean("scheduler");
    		scheduler.printMsg("Hello 2");

    	}
    }

Output

    CustomerBo : Hello 1
    SchedulerBo : Hello 2

[http://www.mkyong.com/spring3/spring-3-javaconfig-import-example/](http://www.mkyong.com/spring3/spring-3-javaconfig-import-example/)
