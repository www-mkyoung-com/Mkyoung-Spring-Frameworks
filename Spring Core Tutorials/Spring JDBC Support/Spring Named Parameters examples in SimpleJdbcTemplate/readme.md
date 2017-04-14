In JdbcTemplate, SQL parameters are represented by a special placeholder “?” symbol and bind it by position. The problem is whenever the order of parameter is changed, you have to change the parameters bindings as well, it’s error prone and cumbersome to maintain it.

To fix it, you can use “Named Parameter“, whereas SQL parameters are defined by a starting colon follow by a name, rather than by position. In additional, the named parameters are only support in SimpleJdbcTemplate and NamedParameterJdbcTemplate.

See following three examples to use named parameters in Spring.

Example 1
Example to show you how to use named parameters in a single insert statement.

//insert with named parameter
public void insertNamedParameter(Customer customer){

	String sql = "INSERT INTO CUSTOMER " +
		"(CUST_ID, NAME, AGE) VALUES (:custId, :name, :age)";

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("custId", customer.getCustId());
	parameters.put("name", customer.getName());
	parameters.put("age", customer.getAge());

	getSimpleJdbcTemplate().update(sql, parameters);

}

 
Example 2
Examples to show how to use named parameters in a batch operation statement.

public void insertBatchNamedParameter(final List<Customer> customers){

	String sql = "INSERT INTO CUSTOMER " +
	"(CUST_ID, NAME, AGE) VALUES (:custId, :name, :age)";

	List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>();
	for (Customer cust : customers) {
		parameters.add(new BeanPropertySqlParameterSource(cust));
	}

	getSimpleJdbcTemplate().batchUpdate(sql,
		parameters.toArray(new SqlParameterSource[0]));
}

 
Example 3
Another examples to use named parameters in a batch operation statement.

public void insertBatchNamedParameter2(final List<Customer> customers){

   SqlParameterSource[] params =
	SqlParameterSourceUtils.createBatch(customers.toArray());

   getSimpleJdbcTemplate().batchUpdate(
	"INSERT INTO CUSTOMER (CUST_ID, NAME, AGE) VALUES (:custId, :name, :age)",
	params);

}
