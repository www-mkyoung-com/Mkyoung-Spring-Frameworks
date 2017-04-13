In Spring JDBC development, you can use `JdbcTemplate` and `JdbcDaoSupport` classes to simplify the overall database operation processes.

In this tutorial, we will reuse the last [Spring + JDBC example](http://www.mkyong.com/spring/maven-spring-jdbc-example/), to see the different between a before (No JdbcTemplate support) and after (With JdbcTemplate support) example.

## 1\. Example Without JdbcTemplate

Witout JdbcTemplate, you have to create many redundant codes (create connection , close connection , handle exception) in all the DAO database operation methods – insert, update and delete. It just not efficient, ugly, error prone and tedious.

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    public void insert(Customer customer){

    	String sql = "INSERT INTO CUSTOMER " +
    			"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";
    	Connection conn = null;

    	try {
    		conn = dataSource.getConnection();
    		PreparedStatement ps = conn.prepareStatement(sql);
    		ps.setInt(1, customer.getCustId());
    		ps.setString(2, customer.getName());
    		ps.setInt(3, customer.getAge());
    		ps.executeUpdate();
    		ps.close();

    	} catch (SQLException e) {
    		throw new RuntimeException(e);

    	} finally {
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (SQLException e) {}
    		}
    	}
    }

## 2\. Example With JdbcTemplate

With JdbcTemplate, you save a lot of typing on the redundant codes, becuase JdbcTemplate will handle it automatically.

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    public void insert(Customer customer){

    	String sql = "INSERT INTO CUSTOMER " +
    		"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";

    	jdbcTemplate = new JdbcTemplate(dataSource);

    	jdbcTemplate.update(sql, new Object[] { customer.getCustId(),
    		customer.getName(),customer.getAge()
    	});

    }

See the different?

## 3\. Example With JdbcDaoSupport

By extended the JdbcDaoSupport, set the datasource and JdbcTemplate in your class is no longer required, you just need to inject the correct datasource into JdbcCustomerDAO. And you can get the JdbcTemplate by using a getJdbcTemplate() method.

    public class JdbcCustomerDAO extends JdbcDaoSupport implements CustomerDAO
    {
       //no need to set datasource here
       public void insert(Customer customer){

    	String sql = "INSERT INTO CUSTOMER " +
    		"(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";

    	getJdbcTemplate().update(sql, new Object[] { customer.getCustId(),
    			customer.getName(),customer.getAge()
    	});

    }

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="dataSource"
             class="org.springframework.jdbc.datasource.DriverManagerDataSource">

    		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    		<property name="url" value="jdbc:mysql://localhost:3306/mkyongjava" />
    		<property name="username" value="root" />
    		<property name="password" value="password" />
    	</bean>

    </beans>

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="customerDAO" class="com.mkyong.customer.dao.impl.JdbcCustomerDAO">
    		<property name="dataSource" ref="dataSource" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring/spring-jdbctemplate-jdbcdaosupport-examples/](http://www.mkyong.com/spring/spring-jdbctemplate-jdbcdaosupport-examples/)
