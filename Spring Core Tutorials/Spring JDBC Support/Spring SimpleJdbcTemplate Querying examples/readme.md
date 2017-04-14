Here are few examples to show how to use SimpleJdbcTemplate `query()` methods to query or extract data from database. In JdbcTemplate `query()`, you need to manually cast the returned result to desire object type, and pass an Object array as parameters. In SimpleJdbcTemplate, it is more user friendly and simple.

**jdbctemplate vesus simplejdbctemplate**  
Please compare this [SimpleJdbcTemplate example](http://www.mkyong.com/spring/spring-simplejdbctemplate-querying-examples/) with this [JdbcTemplate example](http://www.mkyong.com/spring/spring-jdbctemplate-querying-examples/).

## 1\. Querying for Single Row

Here’s two ways to show you how to query or extract a single row from database, and convert it into a model class.

## 1.1 Custom RowMapper

In general, It’s always recommend to implement the RowMapper interface to create a custom RowMapper to suit your needs.

    package com.mkyong.customer.model;

    import java.sql.ResultSet;
    import java.sql.SQLException;

    import org.springframework.jdbc.core.RowMapper;

    public class CustomerRowMapper implements RowMapper
    {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Customer customer = new Customer();
    		customer.setCustId(rs.getInt("CUST_ID"));
    		customer.setName(rs.getString("NAME"));
    		customer.setAge(rs.getInt("AGE"));
    		return customer;
    	}

    }

    public Customer findByCustomerId(int custId){

    	String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";

    	Customer customer = getSimpleJdbcTemplate().queryForObject(
    			sql,  new CustomerParameterizedRowMapper(), custId);

    	return customer;
    }

## 1.2 BeanPropertyRowMapper

In SimpleJdbcTemplate, you need to use ‘ParameterizedBeanPropertyRowMapper’ instead of ‘BeanPropertyRowMapper’.

    public Customer findByCustomerId2(int custId){

    	String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";

    	Customer customer = getSimpleJdbcTemplate().queryForObject(sql,
              ParameterizedBeanPropertyRowMapper.newInstance(Customer.class), custId);

    	return customer;
    }

## 2\. Querying for Multiple Rows

Query or extract multiple rows from database, and convert it into a List.

## 2.1 ParameterizedBeanPropertyRowMapper

    public List<Customer> findAll(){

    	String sql = "SELECT * FROM CUSTOMER";

    	List<Customer> customers =
    		getSimpleJdbcTemplate().query(sql,
    		   ParameterizedBeanPropertyRowMapper.newInstance(Customer.class));

    	return customers;
    }

## 3\. Querying for a Single Value

Query or extract a single column value from database.

## 3.1 Single column name

It shows how to query a single column name as String.

    public String findCustomerNameById(int custId){

    	String sql = "SELECT NAME FROM CUSTOMER WHERE CUST_ID = ?";

    	String name = getSimpleJdbcTemplate().queryForObject(
    		sql, String.class, custId);

    	return name;

    }

## 3.2 Total number of rows

It shows how to query a total number of rows from database.

    public int findTotalCustomer(){

    	String sql = "SELECT COUNT(*) FROM CUSTOMER";

    	int total = getSimpleJdbcTemplate().queryForInt(sql);

    	return total;
    }

Run it

    package com.mkyong.common;

    import java.util.ArrayList;
    import java.util.List;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import com.mkyong.customer.dao.CustomerDAO;
    import com.mkyong.customer.model.Customer;

    public class SimpleJdbcTemplateApp
    {
        public static void main( String[] args )
        {
        	 ApplicationContext context =
        		new ClassPathXmlApplicationContext("Spring-Customer.xml");

             CustomerDAO customerSimpleDAO =
                    (CustomerDAO) context.getBean("customerSimpleDAO");

             Customer customerA = customerSimpleDAO.findByCustomerId(1);
             System.out.println("Customer A : " + customerA);

             Customer customerB = customerSimpleDAO.findByCustomerId2(1);
             System.out.println("Customer B : " + customerB);

             List<Customer> customerAs = customerSimpleDAO.findAll();
             for(Customer cust: customerAs){
             	 System.out.println("Customer As : " + customerAs);
             }

             List<Customer> customerBs = customerSimpleDAO.findAll2();
             for(Customer cust: customerBs){
             	 System.out.println("Customer Bs : " + customerBs);
             }

             String customerName = customerSimpleDAO.findCustomerNameById(1);
             System.out.println("Customer Name : " + customerName);

             int total = customerSimpleDAO.findTotalCustomer();
             System.out.println("Total : " + total);

        }
    }

## Conclusion

The SimpleJdbcTemplate isn’t a replacement for JdbcTemplate, it’s just a java5-friendly supplement to it.

[http://www.mkyong.com/spring/spring-simplejdbctemplate-querying-examples/](http://www.mkyong.com/spring/spring-simplejdbctemplate-querying-examples/)
