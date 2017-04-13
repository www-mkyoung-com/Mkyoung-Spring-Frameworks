In Spring, “**Autowiring by Constructor**” is actually [autowiring by Type](http://www.mkyong.com/spring/spring-autowiring-by-type/) in constructor argument. It means, if data type of a bean is same as the data type of other bean constructor argument, auto wire it.

See a full example of Spring auto wiring by constructor.

## 1\. Beans

Two beans, developer and language.

    package com.mkyong.common;

    public class Developer {
    	private Language language;

    	//autowire by constructor
    	public Developer(Language language) {
    		this.language = language;
    	}

    	//...

    }

    package com.mkyong.common;

    public class Language {
    	private String name;
    	//...
    }

## 2\. Spring Wiring

Normally, you wire the bean via constructor like this :

    <bean id="developer" class="com.mkyong.common.Developer">
    	<constructor-arg>
    		<ref bean="language" />
    	</constructor-arg>
    </bean>

    <bean id="language" class="com.mkyong.common.Language" >
    	<property name="name" value="Java" />
    </bean>

_Output_

    Developer [language=Language [name=Java]]

With **autowire by constructor enabled**, you can leave the constructor property unset. Spring will find the compatible data type and wire it automatcailly.

    <bean id="developer" class="com.mkyong.common.Developer" autowire="constructor" />

    <bean id="language" class="com.mkyong.common.Language" >
    	<property name="name" value="Java" />
    </bean>

_Output_

    Developer [language=Language [name=Java]]

[http://www.mkyong.com/spring/spring-autowiring-by-constructor/](http://www.mkyong.com/spring/spring-autowiring-by-constructor/)
