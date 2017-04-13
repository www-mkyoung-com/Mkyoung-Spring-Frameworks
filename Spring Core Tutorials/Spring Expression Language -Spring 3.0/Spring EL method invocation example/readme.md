Spring expression language (SpEL) allow developer uses expression to execute method and inject the method returned value into property, or so called “**SpEL method invocation**“.

## Spring EL in Annotation

See how to do Spring EL method invocation with **@Value** annotation.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	@Value("#{'mkyong'.toUpperCase()}")
    	private String name;

    	@Value("#{priceBean.getSpecialPrice()}")
    	private double amount;

    	public String getName() {
    		return name;
    	}

    	public void setName(String name) {
    		this.name = name;
    	}

    	public double getAmount() {
    		return amount;
    	}

    	public void setAmount(double amount) {
    		this.amount = amount;
    	}

    	@Override
    	public String toString() {
    		return "Customer [name=" + name + ", amount=" + amount + "]";
    	}

    }

    package com.mkyong.core;

    import org.springframework.stereotype.Component;

    @Component("priceBean")
    public class Price {

    	public double getSpecialPrice() {
    		return new Double(99.99);
    	}

    }

_Output_

    Customer [name=MKYONG, amount=99.99]

Explanation

Call the ‘`toUpperCase()`‘ method on the **string** literal.

    @Value("#{'mkyong'.toUpperCase()}")
    private String name;

Call the ‘`getSpecialPrice()`‘ method on bean ‘**priceBean**‘.

    @Value("#{priceBean.getSpecialPrice()}")
    private double amount;

## Spring EL in XML

This is the equivalent version in bean definition XML file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    		<property name="name" value="#{'mkyong'.toUpperCase()}" />
    		<property name="amount" value="#{priceBean.getSpecialPrice()}" />
    	</bean>

    	<bean id="priceBean" class="com.mkyong.core.Price" />

    </beans>

_Output_

    Customer [name=MKYONG, amount=99.99]

[http://www.mkyong.com/spring3/spring-el-method-invocation-example/](http://www.mkyong.com/spring3/spring-el-method-invocation-example/)
