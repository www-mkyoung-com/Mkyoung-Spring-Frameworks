If `jobRepository` is created with `MapJobRepositoryFactoryBean` (metadata in memory), the Spring batch jobs are running successfully.

spring-config.xml

    <bean id="jobRepository"
      class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean">
      <property name="transactionManager" ref="transactionManager" />
    </bean>

## Problem

After changing the `jobRepository` to store metadata into database :

spring-config.xml

    <bean id="jobRepository"
    class="org.springframework.batch.core.repository.support.JobRepositoryFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="transactionManager" ref="transactionManager" />
    <property name="databaseType" value="mysql" />
      </bean>

      <bean id="jobLauncher"
    class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
    <property name="jobRepository" ref="jobRepository" />
      </bean>

      <bean id="dataSource"
    class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://localhost:3306/test" />
    <property name="username" value="root" />
    <property name="password" value="" />
      </bean>

It prompts the `batch_job_instance` table doesn’t exist? Why Spring didn’t create those meta tables automatically?

    Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table db.batch_job_instance' doesn't exist
    	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
    	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
    	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
    	at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
    	at com.mysql.jdbc.Util.handleNewInstance(Util.java:411)

## Solution

The meta table’s scripts are stored in the `spring-batch.jar`, you need to create it manually.

![spring-batch-meta-data-tables](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-meta-data-tables.png)

Normally, you can automatic the table script creation via the Spring XML configuration file. For example,

spring-config.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:jdbc="http://www.springframework.org/schema/jdbc"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    	http://www.springframework.org/schema/jdbc
    	http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd">

      <!-- connect to database -->
      <bean id="dataSource"
    	class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    	<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    	<property name="url" value="jdbc:mysql://localhost:3306/test" />
    	<property name="username" value="root" />
    	<property name="password" value="" />
      </bean>

      <!-- create job-meta tables automatically -->
      <jdbc:initialize-database data-source="dataSource">
    	<jdbc:script location="org/springframework/batch/core/schema-drop-mysql.sql" />
    	<jdbc:script location="org/springframework/batch/core/schema-mysql.sql" />
      </jdbc:initialize-database>

    </beans>

Run your Spring batch jobs again, those meta tables will be created automatically.

## References

1.  [Spring Batch – Meta-Data Schema](http://static.springsource.org/spring-batch/reference/html/metaDataSchema.html)

[http://www.mkyong.com/spring-batch/spring-batch-metadata-tables-are-not-created-automatically/](http://www.mkyong.com/spring-batch/spring-batch-metadata-tables-are-not-created-automatically/)
