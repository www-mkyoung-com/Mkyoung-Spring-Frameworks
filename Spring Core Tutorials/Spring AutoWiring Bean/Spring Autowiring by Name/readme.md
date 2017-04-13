In Spring, “**Autowiring by Name**” means, if the name of a bean is same as the name of other bean property, auto wire it.

For example, if a “customer” bean exposes an “address” property, Spring will find the “address” bean in current container and wire it automatically. And if no matching found, just do nothing.

You can enable this feature via `autowire="byName"` like below :

    <!-- customer has a property name "address" -->
    <bean id="customer" class="com.mkyong.common.Customer" autowire="byName" />

    <bean id="address" class="com.mkyong.common.Address" >
    	<property name="fulladdress" value="Block A 888, CA" />
    </bean>

See a full example of Spring auto wiring by name.

## 1\. Beans

Two beans, customer and address.

    package com.mkyong.common;

    public class Customer
    {
    	private Address address;
    	//...
    }

    package com.mkyong.common;

    public class Address
    {
    	private String fulladdress;
    	//...
    }

## 2\. Spring Wiring

Normally, you wire the bean explicitly, via ref attribute like this :

    <bean id="customer" class="com.mkyong.common.Customer" >
    	<property name="address" ref="address" />
    </bean>

    <bean id="address" class="com.mkyong.common.Address" >
    	<property name="fulladdress" value="Block A 888, CA" />
    </bean>

_Output_

    Customer [address=Address [fulladdress=Block A 888, CA]]

With **autowire by name enabled**, you do not need to declares the property tag anymore. As long as the “address” bean is same name as the property of “customer” bean, which is “address”, Spring will wire it automatically.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="byName" />

    <bean id="address" class="com.mkyong.common.Address" >
    	<property name="fulladdress" value="Block A 888, CA" />
    </bean>

_Output_

    Customer [address=Address [fulladdress=Block A 888, CA]]

See another example, this time, the wiring will failed, caused the bean “addressABC” is not match the property name of bean “customer”.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="byName" />

    <bean id="addressABC" class="com.mkyong.common.Address" >
    	<property name="fulladdress" value="Block A 888, CA" />
    </bean>

_Output_

    Customer [address=null]

[http://www.mkyong.com/spring/spring-autowiring-by-name/](http://www.mkyong.com/spring/spring-autowiring-by-name/)
