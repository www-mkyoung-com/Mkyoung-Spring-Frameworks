Spring EL supports **ternary operator** , perform “**if then else**” conditional checking. For example,

    condition ? true : false

## Spring EL in Annotation

Spring EL ternary operator with **@Value** annotation. In this example, if “**itemBean.qtyOnHand**” is less than 100, then set “**customerBean.warning**” to true, else set it to false.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	@Value("#{itemBean.qtyOnHand < 100 ? true : false}")
    	private boolean warning;

    	public boolean isWarning() {
    		return warning;
    	}

    	public void setWarning(boolean warning) {
    		this.warning = warning;
    	}

    	@Override
    	public String toString() {
    		return "Customer [warning=" + warning + "]";
    	}

    }

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("itemBean")
    public class Item {

    	@Value("99")
    	private int qtyOnHand;

    	public int getQtyOnHand() {
    		return qtyOnHand;
    	}

    	public void setQtyOnHand(int qtyOnHand) {
    		this.qtyOnHand = qtyOnHand;
    	}

    }

_Output_

    Customer [warning=true]

## Spring EL in XML

See equivalent version in bean definition XML file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    		<property name="warning"
                              value="#{itemBean.qtyOnHand < 100 ? true : false}" />
    	</bean>

    	<bean id="itemBean" class="com.mkyong.core.Item">
    		<property name="qtyOnHand" value="99" />
    	</bean>

    </beans>

_Output_

    Customer [warning=true]

In XML, you need to replace less than operator "**<**" with "**&lt;**".

[http://www.mkyong.com/spring3/spring-el-ternary-operator-if-then-else-example/](http://www.mkyong.com/spring3/spring-el-ternary-operator-if-then-else-example/)
