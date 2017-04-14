In this tutorial, we show you how to use `batchUpdate()` in SimpleJdbcTemplate class.

See `batchUpdate()` example in SimpleJdbcTemplate class.

    //insert batch example
    public void insertBatch(final List<Customer> customers){
    	String sql = "INSERT INTO CUSTOMER " +
    		"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";

    	List<Object[]> parameters = new ArrayList<Object[]>();

    	for (Customer cust : customers) {
            parameters.add(new Object[] {cust.getCustId(),
                cust.getName(), cust.getAge()}
            );
        }
        getSimpleJdbcTemplate().batchUpdate(sql, parameters);
    }

Alternatively, you can execute the SQL directly.

    //insert batch example with SQL
    public void insertBatchSQL(final String sql){

    	getJdbcTemplate().batchUpdate(new String[]{sql});

    }

Spring’s bean configuration file

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerSimpleDAO"
            class="com.mkyong.customer.dao.impl.SimpleJdbcCustomerDAO">

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

Run it

    package com.mkyong.common;

    import java.util.ArrayList;
    import java.util.List;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import com.mkyong.customer.dao.CustomerDAO;
    import com.mkyong.customer.model.Customer;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        		new ClassPathXmlApplicationContext("Spring-Customer.xml");

            CustomerDAO customerSimpleDAO =
                          (CustomerDAO) context.getBean("customerSimpleDAO");

            Customer customer1 = new Customer(1, "mkyong1",21);
            Customer customer3 = new Customer(2, "mkyong2",22);
            Customer customer2 = new Customer(3, "mkyong3",23);

            List<Customer>customers = new ArrayList<Customer>();
            customers.add(customer1);
            customers.add(customer2);
            customers.add(customer3);

            customerSimpleDAO.insertBatch(customers);

            String sql = "UPDATE CUSTOMER SET NAME ='BATCHUPDATE'";
            customerSimpleDAO.insertBatchSQL(sql);

        }
    }

In this example, you are inserted three customers’ records and update all customer’s name in batch.

[http://www.mkyong.com/spring/spring-simplejdbctemplate-batchupdate-example/](http://www.mkyong.com/spring/spring-simplejdbctemplate-batchupdate-example/)
