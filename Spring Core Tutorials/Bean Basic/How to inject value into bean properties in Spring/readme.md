In Spring, there are three ways to inject value into bean properties.

*   Normal way
*   Shortcut
*   “p” schema

See a simple Java class, which contains two properties – name and type. Later you will use Spring to inject value into the bean properties.

    package com.mkyong.common;

    public class FileNameGenerator
    {
    	private String name;
    	private String type;

    	public String getName() {
    		return name;
    	}
    	public void setName(String name) {
    		this.name = name;
    	}
    	public String getType() {
    		return type;
    	}
    	public void setType(String type) {
    		this.type = type;
    	}
    }

## 1\. Normal way

Inject value within a ‘value’ tag and enclosed with ‘property’ tag.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="FileNameGenerator" class="com.mkyong.common.FileNameGenerator">
    		<property name="name">
    			<value>mkyong</value>
    		</property>
    		<property name="type">
    			<value>txt</value>
    		</property>
    	</bean>
    </beans>

## 2\. Shortcut

Inject value with “value” attribute.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="FileNameGenerator" class="com.mkyong.common.FileNameGenerator">
    		<property name="name" value="mkyong" />
    		<property name="type" value="txt" />
    	</bean>

    </beans>

## 3\. “p” schema

Inject value by using “p” schema as an attributes.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:p="http://www.springframework.org/schema/p"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="FileNameGenerator" class="com.mkyong.common.FileNameGenerator"
                 p:name="mkyong" p:type="txt" />

    </beans>

Remember declares the **xmlns:p=”http://www.springframework.org/schema/p** in the Spring XML bean configuration file.

## Conclusion

Which methods to use is totally base on personal preference, it will not affect the value inject into the bean properties.

[http://www.mkyong.com/spring/how-to-define-bean-properties-in-spring/](http://www.mkyong.com/spring/how-to-define-bean-properties-in-spring/)
