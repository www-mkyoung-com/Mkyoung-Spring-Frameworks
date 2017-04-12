Often times, most Spring developers just put the entire deployment details (database details, log file path) in XML bean configuration file as following :

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerDAO" class="com.mkyong.customer.dao.impl.JdbcCustomerDAO">

    		<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="customerSimpleDAO" class="com.mkyong.customer.dao.impl.SimpleJdbcCustomerDAO">

    		<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="dataSource"
    		class="org.springframework.jdbc.datasource.DriverManagerDataSource">

    		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    		<property name="url" value="jdbc:mysql://localhost:3306/mkyongjava" />
    		<property name="username" value="root" />
    		<property name="password" value="password" />
    	</bean>

    </beans>

But, in a corporate environment, deployment detail is usually only can ‘touch’ by your system or database administrator, they just refuse to access your bean configuration file directly, and they will request a separate file for deployment configuration, for example, a simple properties, with deployment detail only.

## PropertyPlaceholderConfigurer example

To fix it, you can use **PropertyPlaceholderConfigurer** class to externalize the deployment details into a properties file, and access from bean configuration file via a special format – **${variable}**.

Create a properties file (database.properties), include your database details, put it into your project class path.

    jdbc.driverClassName=com.mysql.jdbc.Driver
    jdbc.url=jdbc:mysql://localhost:3306/mkyongjava
    jdbc.username=root
    jdbc.password=password

Declare a **PropertyPlaceholderConfigurer** in bean configuration file and map to the ‘`database.properties`‘ properties file you created just now.

    <bean
    	class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">

    	<property name="location">
    		<value>database.properties</value>
    	</property>
    </bean>

Full example

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean
    		class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">

    		<property name="location">
    			<value>database.properties</value>
    		</property>
    	</bean>

    	<bean id="customerDAO" class="com.mkyong.customer.dao.impl.JdbcCustomerDAO">

    		<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="customerSimpleDAO"
                    class="com.mkyong.customer.dao.impl.SimpleJdbcCustomerDAO">

    		<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="dataSource"
    		class="org.springframework.jdbc.datasource.DriverManagerDataSource">

    		<property name="driverClassName" value="${jdbc.driverClassName}" />
    		<property name="url" value="${jdbc.url}" />
    		<property name="username" value="${jdbc.username}" />
    		<property name="password" value="${jdbc.password}" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring/spring-propertyplaceholderconfigurer-example/](http://www.mkyong.com/spring/spring-propertyplaceholderconfigurer-example/)
