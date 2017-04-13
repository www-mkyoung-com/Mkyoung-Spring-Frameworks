In Spring EL, you can reference a bean, and nested properties using a ‘**dot (.)**‘ symbol. For example, “**bean.property_name**“.

    public class Customer {

    	@Value("#{addressBean.country}")
    	private String country;

In above code snippet, it inject the value of “**country**” property from “**addressBean**” bean into current “**customer**” class, “**country**” property.

## Spring EL in Annotation

See following example, show you how to use SpEL to reference a bean, bean property and also it’s method.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	@Value("#{addressBean}")
    	private Address address;

    	@Value("#{addressBean.country}")
    	private String country;

    	@Value("#{addressBean.getFullAddress('mkyong')}")
    	private String fullAddress;

    	//getter and setter methods

    	@Override
    	public String toString() {
    		return "Customer [address=" + address + "\n, country=" + country
    				+ "\n, fullAddress=" + fullAddress + "]";
    	}

    }

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("addressBean")
    public class Address {

    	@Value("Block ABC, LakeView")
    	private String street;

    	@Value("98700")
    	private int postcode;

    	@Value("US")
    	private String country;

    	public String getFullAddress(String prefix) {

    		return prefix + " : " + street + " " + postcode + " " + country;
    	}

    	//getter and setter methods

    	public void setCountry(String country) {
    		this.country = country;
    	}

    	@Override
    	public String toString() {
    		return "Address [street=" + street + ", postcode=" + postcode
    				+ ", country=" + country + "]";
    	}

    }

_Run it_

    Customer obj = (Customer) context.getBean("customerBean");
    System.out.println(obj);

_Output_

    Customer [address=Address [street=Block ABC, LakeView, postcode=98700, country=US]
    , country=US
    , fullAddress=mkyong : Block ABC, LakeView 98700 US]

## Spring EL in XML

See equivalent version in bean definition XML file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    		<property name="address" value="#{addressBean}" />
    		<property name="country" value="#{addressBean.country}" />
    		<property name="fullAddress" value="#{addressBean.getFullAddress('mkyong')}" />
    	</bean>

    	<bean id="addressBean" class="com.mkyong.core.Address">
    		<property name="street" value="Block ABC, LakeView" />
    		<property name="postcode" value="98700" />
    		<property name="country" value="US" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring3/spring-el-bean-reference-example/](http://www.mkyong.com/spring3/spring-el-bean-reference-example/)
