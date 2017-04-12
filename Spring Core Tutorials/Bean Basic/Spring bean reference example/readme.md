In Spring, beans can “access” to each other by specify the bean references in the same or different bean configuration file.

## 1\. Bean in different XML files

If you are referring to a bean in different XML file, you can reference it with a ‘`ref`‘ tag, ‘`bean`‘ attribute.

    <ref bean="someBean"/>

In this example, the bean “**OutputHelper**” declared in ‘`Spring-Common.xml`‘ can access to other beans in ‘`Spring-Output.xml`‘ – “**CsvOutputGenerator**” or “**JsonOutputGenerator**“, by using a ‘ref’ attribute in property tag.

_File : Spring-Common.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<property name="outputGenerator" >
    			<ref bean="CsvOutputGenerator"/>
    		</property>
    	</bean>

    </beans>

_File : Spring-Output.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CsvOutputGenerator" class="com.mkyong.output.impl.CsvOutputGenerator" />
    	<bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

## 2\. Bean in same XML file

If you are referring to a bean in same XML file, you can reference it with ‘`ref`‘ tag, ‘`local`‘ attribute.

    <ref local="someBean"/>

In this example, the bean “**OutputHelper**” declared in ‘`Spring-Common.xml`‘ can access to each other “**CsvOutputGenerator**” or “**JsonOutputGenerator**“.

_File : Spring-Common.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="OutputHelper" class="com.mkyong.output.OutputHelper">
    		<property name="outputGenerator" >
    			<ref local="CsvOutputGenerator"/>
    		</property>
    	</bean>

    	<bean id="CsvOutputGenerator" class="com.mkyong.output.impl.CsvOutputGenerator" />
    	<bean id="JsonOutputGenerator" class="com.mkyong.output.impl.JsonOutputGenerator" />

    </beans>

## Conclusion

Actually, the ‘ref’ tag can access to a bean either in same or different XML files, however, for the project readability, you should use the ‘local’ attribute if you reference to a bean which declared in the same XML file.

[http://www.mkyong.com/spring/spring-bean-reference-example/](http://www.mkyong.com/spring/spring-bean-reference-example/)
