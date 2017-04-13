Spring EL supports most of the standard mathematical, logical or relational operators. For example,

1.  **Relational operators** – equal (==, eq), not equal (!=, ne), less than (<, lt), less than or equal (<= , le), greater than (>, gt), and greater than or equal (>=, ge).
2.  **Logical operators** – and, or, and not (!).
3.  **Mathematical operators** – addition (+), Subtraction (-), Multiplication (*), division (/), modulus (%) and exponential power (^).

## Spring EL in Annotation

This example demonstrates the use of operators in SpEL.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	//Relational operators

    	@Value("#{1 == 1}") //true
    	private boolean testEqual;

    	@Value("#{1 != 1}") //false
    	private boolean testNotEqual;

    	@Value("#{1 < 1}") //false
    	private boolean testLessThan;

    	@Value("#{1 <= 1}") //true
    	private boolean testLessThanOrEqual;

    	@Value("#{1 > 1}") //false
    	private boolean testGreaterThan;

    	@Value("#{1 >= 1}") //true
    	private boolean testGreaterThanOrEqual;

    	//Logical operators , numberBean.no == 999

    	@Value("#{numberBean.no == 999 and numberBean.no < 900}") //false
    	private boolean testAnd;

    	@Value("#{numberBean.no == 999 or numberBean.no < 900}") //true
    	private boolean testOr;

    	@Value("#{!(numberBean.no == 999)}") //false
    	private boolean testNot;

    	//Mathematical operators

    	@Value("#{1 + 1}") //2.0
    	private double testAdd;

    	@Value("#{'1' + '@' + '1'}") //1@1
    	private String testAddString;

    	@Value("#{1 - 1}") //0.0
    	private double testSubtraction;

    	@Value("#{1 * 1}") //1.0
    	private double testMultiplication;

    	@Value("#{10 / 2}") //5.0
    	private double testDivision;

    	@Value("#{10 % 10}") //0.0
    	private double testModulus ;

    	@Value("#{2 ^ 2}") //4.0
    	private double testExponentialPower;

    	@Override
    	public String toString() {
    		return "Customer [testEqual=" + testEqual + ", testNotEqual="
    				+ testNotEqual + ", testLessThan=" + testLessThan
    				+ ", testLessThanOrEqual=" + testLessThanOrEqual
    				+ ", testGreaterThan=" + testGreaterThan
    				+ ", testGreaterThanOrEqual=" + testGreaterThanOrEqual
    				+ ", testAnd=" + testAnd + ", testOr=" + testOr + ", testNot="
    				+ testNot + ", testAdd=" + testAdd + ", testAddString="
    				+ testAddString + ", testSubtraction=" + testSubtraction
    				+ ", testMultiplication=" + testMultiplication
    				+ ", testDivision=" + testDivision + ", testModulus="
    				+ testModulus + ", testExponentialPower="
    				+ testExponentialPower + "]";
    	}

    }

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("numberBean")
    public class Number {

    	@Value("999")
    	private int no;

    	public int getNo() {
    		return no;
    	}

    	public void setNo(int no) {
    		this.no = no;
    	}

    }

_Run it_

    Customer obj = (Customer) context.getBean("customerBean");
    System.out.println(obj);

_Output_

    Customer [
    	testEqual=true,
    	testNotEqual=false,
    	testLessThan=false,
    	testLessThanOrEqual=true,
    	testGreaterThan=false,
    	testGreaterThanOrEqual=true,
    	testAnd=false,
    	testOr=true,
    	testNot=false,
    	testAdd=2.0,
    	testAddString=1@1,
    	testSubtraction=0.0,
    	testMultiplication=1.0,
    	testDivision=5.0,
    	testModulus=0.0,
    	testExponentialPower=4.0
    ]

## Spring EL in XML

See equivalent version in bean definition XML file. In XML, symbol like "**less than**" is always not support, instead, you should use the textual equivalents shown above, for example, ('**<**' = '**lt**') and ('**<=**' = '**le**').

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">

    	  <property name="testEqual" value="#{1 == 1}" />
    	  <property name="testNotEqual" value="#{1 != 1}" />
    	  <property name="testLessThan" value="#{1 lt 1}" />
    	  <property name="testLessThanOrEqual" value="#{1 le 1}" />
    	  <property name="testGreaterThan" value="#{1 > 1}" />
    	  <property name="testGreaterThanOrEqual" value="#{1 >= 1}" />

    	  <property name="testAnd" value="#{numberBean.no == 999 and numberBean.no lt 900}" />
    	  <property name="testOr" value="#{numberBean.no == 999 or numberBean.no lt 900}" />
    	  <property name="testNot" value="#{!(numberBean.no == 999)}" />

    	  <property name="testAdd" value="#{1 + 1}" />
    	  <property name="testAddString" value="#{'1' + '@' + '1'}" />
    	  <property name="testSubtraction" value="#{1 - 1}" />
    	  <property name="testMultiplication" value="#{1 * 1}" />
    	  <property name="testDivision" value="#{10 / 2}" />
    	  <property name="testModulus" value="#{10 % 10}" />
    	  <property name="testExponentialPower" value="#{2 ^ 2}" />

    	</bean>

    	<bean id="numberBean" class="com.mkyong.core.Number">
    		<property name="no" value="999" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring3/spring-el-operators-example/](http://www.mkyong.com/spring3/spring-el-operators-example/)
