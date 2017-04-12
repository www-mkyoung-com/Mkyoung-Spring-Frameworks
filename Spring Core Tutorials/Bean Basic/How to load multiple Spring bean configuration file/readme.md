## Problem

In a large project structure, the Spring’s bean configuration files are located in different folders for easy maintainability and modular. For example, `Spring-Common.xml` in common folder, `Spring-Connection.xml` in connection folder, `Spring-ModuleA.xml` in ModuleA folder…and etc.

You may load multiple Spring bean configuration files in the code :

    ApplicationContext context =
        	new ClassPathXmlApplicationContext(new String[] {"Spring-Common.xml",
                  "Spring-Connection.xml","Spring-ModuleA.xml"});

Put all spring xml files under project classpath.

    project-classpath/Spring-Common.xml
    project-classpath/Spring-Connection.xml
    project-classpath/Spring-ModuleA.xml

## Solution

The above ways are lack of organizing and error prone, the better way should be organized all your Spring bean configuration files into a single XML file. For example, create a `Spring-All-Module.xml` file, and import the entire Spring bean files like this :

File : Spring-All-Module.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<import resource="common/Spring-Common.xml"/>
            <import resource="connection/Spring-Connection.xml"/>
            <import resource="moduleA/Spring-ModuleA.xml"/>

    </beans>

Now you can load a single xml file like this :

    ApplicationContext context =
        		new ClassPathXmlApplicationContext(Spring-All-Module.xml);

Put this file under project classpath.

    project-classpath/Spring-All-Module.xml

[http://www.mkyong.com/spring/load-multiple-spring-bean-configuration-file/](http://www.mkyong.com/spring/load-multiple-spring-bean-configuration-file/)
