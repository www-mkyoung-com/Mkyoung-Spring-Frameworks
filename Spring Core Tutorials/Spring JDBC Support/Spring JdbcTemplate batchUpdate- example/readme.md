In some cases, you may required to insert a batch of records into database in one shot. If you call a single insert method for every record, the SQL statement will be compiled repeatedly and causing your system slow to perform.

In above case, you can use JdbcTemplate `batchUpdate()` method to perform the batch insert operations. With this method, the statement is compiled only once and executed multiple times.

See `batchUpdate()` example in JdbcTemplate class.

    //insert batch example
    public void insertBatch(final List<Customer> customers){

      String sql = "INSERT INTO CUSTOMER " +
    	"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";

      getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

    	@Override
    	public void setValues(PreparedStatement ps, int i) throws SQLException {
    		Customer customer = customers.get(i);
    		ps.setLong(1, customer.getCustId());
    		ps.setString(2, customer.getName());
    		ps.setInt(3, customer.getAge() );
    	}

    	@Override
    	public int getBatchSize() {
    		return customers.size();
    	}
      });
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

    	<bean id="customerDAO" class="com.mkyong.customer.dao.impl.JdbcCustomerDAO">
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

            CustomerDAO customerDAO = (CustomerDAO) context.getBean("customerDAO");
            Customer customer1 = new Customer(1, "mkyong1",21);
            Customer customer3 = new Customer(2, "mkyong2",22);
            Customer customer2 = new Customer(3, "mkyong3",23);

            List<Customer>customers = new ArrayList<Customer>();
            customers.add(customer1);
            customers.add(customer2);
            customers.add(customer3);

            customerDAO.insertBatch(customers);

            String sql = "UPDATE CUSTOMER SET NAME ='BATCHUPDATE'";
            customerDAO.insertBatchSQL(sql);

        }
    }

In this example, you are inserted three customers’ records and update all customer’s name in batch.

[http://www.mkyong.com/spring/spring-jdbctemplate-batchupdate-example/](http://www.mkyong.com/spring/spring-jdbctemplate-batchupdate-example/)
