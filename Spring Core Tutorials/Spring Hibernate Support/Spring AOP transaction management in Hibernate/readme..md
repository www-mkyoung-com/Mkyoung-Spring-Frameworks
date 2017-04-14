Transaction management is required to ensure the data integrity and consistency in database. Spring’s AOP technique is allow developers to manage the transaction declarative.

Here’s an example to show how to manage the Hibernate transaction with Spring AOP.

_P.S Many Hibernate and Spring configuration files are hidden, only some important files are shown, if you want hand-on, download the full project at the end of the article._

## 1\. Table creation

MySQL table scripts, a **‘product**‘ table and a ‘**product quantity on hand**‘ table.

    CREATE TABLE  `mkyong`.`product` (
      `PRODUCT_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
      `PRODUCT_CODE` varchar(20) NOT NULL,
      `PRODUCT_DESC` varchar(255) NOT NULL,
      PRIMARY KEY (`PRODUCT_ID`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

    CREATE TABLE  `mkyong`.`product_qoh` (
      `QOH_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
      `PRODUCT_ID` bigint(20) unsigned NOT NULL,
      `QTY` int(10) unsigned NOT NULL,
      PRIMARY KEY (`QOH_ID`),
      KEY `FK_product_qoh_product_id` (`PRODUCT_ID`),
      CONSTRAINT `FK_product_qoh_product_id` FOREIGN KEY (`PRODUCT_ID`)
      REFERENCES `product` (`PRODUCT_ID`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

## 2\. Product Business Object

In this ‘**productBo**‘ implementation, the **save()** method will insert a record into the ‘**product**‘ table via **‘productDao**‘ class and a quantity on hand record into the ‘**productQoh**‘ table via ‘**productQohBo**‘ class.

    package com.mkyong.product.bo.impl;

    import com.mkyong.product.bo.ProductBo;
    import com.mkyong.product.bo.ProductQohBo;
    import com.mkyong.product.dao.ProductDao;
    import com.mkyong.product.model.Product;
    import com.mkyong.product.model.ProductQoh;

    public class ProductBoImpl implements ProductBo{

    	ProductDao productDao;
    	ProductQohBo productQohBo;

    	public void setProductDao(ProductDao productDao) {
    		this.productDao = productDao;
    	}

    	public void setProductQohBo(ProductQohBo productQohBo) {
    		this.productQohBo = productQohBo;
    	}

    	//this method need to be transactional
    	public void save(Product product, int qoh){

    		productDao.save(product);
    		System.out.println("Product Inserted");

    		ProductQoh productQoh = new ProductQoh();
    		productQoh.setProductId(product.getProductId());
    		productQoh.setQty(qoh);

    		productQohBo.save(productQoh);
    		System.out.println("ProductQoh Inserted");
    	}
    }

Spring’s bean configuration file.

    <!-- Product business object -->
    <bean id="productBo" class="com.mkyong.product.bo.impl.ProductBoImpl" >
    	<property name="productDao" ref="productDao" />
    	<property name="productQohBo" ref="productQohBo" />
    </bean>

    <!-- Product Data Access Object -->
    <bean id="productDao" class="com.mkyong.product.dao.impl.ProductDaoImpl" >
    	<property name="sessionFactory" ref="sessionFactory"></property>
    </bean>

Run it

    Product product = new Product();
        product.setProductCode("ABC");
        product.setProductDesc("This is product ABC");

        ProductBo productBo = (ProductBo)appContext.getBean("productBo");
        productBo.save(product, 100);

Assume the **save()** does not has the transactional feature, if an Exception throw by **productQohBo.save()**, you will insert a record into ‘**product**‘ table only, no record will be insert into the ‘**productQoh**‘ table. This is a serious problem and break the data consistency in your database.

## 3\. Transaction Management

Declared a ‘**TransactionInterceptor**‘ bean, and a ‘**HibernateTransactionManager**‘ for the Hibernate transaction, and passing the necessary property.

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

        <bean id="transactionInterceptor"
           class="org.springframework.transaction.interceptor.TransactionInterceptor">
    	<property name="transactionManager" ref="transactionManager" />
    	<property name="transactionAttributes">
    	   <props>
    		<prop key="save">PROPAGATION_REQUIRED</prop>
    	   </props>
    	</property>
        </bean>

        <bean id="transactionManager"
            class="org.springframework.orm.hibernate3.HibernateTransactionManager">
    	  <property name="dataSource" ref="dataSource" />
    	  <property name="sessionFactory" ref="sessionFactory" />
        </bean>

    </beans>

Transaction Attributes

In transaction interceptor, you have to define which transaction’s attributes ‘**propagation behavior**‘ should be use. It means if a transactional **‘ProductBoImpl.save()**‘ method is called another method ‘**productQohBo.save()**‘, how the transaction should be propagated? Should it continue to run within the existing transaction? or start a new transaction for its own.

There are 7 types of propagation supported by Spring :

*   **PROPAGATION_REQUIRED** – Support a current transaction; create a new one if none exists.
*   **PROPAGATION_SUPPORTS** – Support a current transaction; execute non-transactionally if none exists.
*   **PROPAGATION_MANDATORY** – Support a current transaction; throw an exception if no current transaction exists.
*   **PROPAGATION_REQUIRES_NEW** – Create a new transaction, suspending the current transaction if one exists.
*   **PROPAGATION_NOT_SUPPORTED** – Do not support a current transaction; rather always execute non-transactionally.
*   **PROPAGATION_NEVER** – Do not support a current transaction; throw an exception if a current transaction exists.
*   **PROPAGATION_NESTED** – Execute within a nested transaction if a current transaction exists, behave like PROPAGATION_REQUIRED else.

In most cases, you may just need to use the PROPAGATION_REQUIRED.

In addition, you have to define the method to support this transaction attributes as well. The method name is supported wild card format, a **save*** will match all method name start with save(…).

Transaction Manager

In Hibernate transaction, you need to use **HibernateTransactionManager**. If you only deal with pure JDBC, use **DataSourceTransactionManager**; while JTA, use **JtaTransactionManager**.

## 4\. Proxy Factory Bean

Create a new proxy factory bean for **ProductBo**, and set the ‘**interceptorNames**‘ property.

    <!-- Product business object -->
       <bean id="productBo" class="com.mkyong.product.bo.impl.ProductBoImpl" >
       	<property name="productDao" ref="productDao" />
       	<property name="productQohBo" ref="productQohBo" />
       </bean>

       <!-- Product Data Access Object -->
       <bean id="productDao" class="com.mkyong.product.dao.impl.ProductDaoImpl" >
       	<property name="sessionFactory" ref="sessionFactory"></property>
       </bean>

       <bean id="productBoProxy"
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target" ref="productBo" />
    <property name="interceptorNames">
    	<list>
    		<value>transactionInterceptor</value>
    	</list>
    </property>
      </bean>

Run it

    Product product = new Product();
    product.setProductCode("ABC");
    product.setProductDesc("This is product ABC");

    ProductBo productBo = (ProductBo)appContext.getBean("productBoProxy");
    productBo.save(product, 100);

Get your proxy bean ‘**productBoProxy**‘, and your **save()** method is support transactional now, any exceptions inside **productBo.save()** method will cause the whole transaction to rollback, no data will be insert into the database.

[http://www.mkyong.com/spring/spring-aop-transaction-management-in-hibernate/](http://www.mkyong.com/spring/spring-aop-transaction-management-in-hibernate/)
