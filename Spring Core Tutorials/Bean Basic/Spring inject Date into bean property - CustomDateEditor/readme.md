    <constructor-arg value="true" />

    	</bean>

    	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    		<property name="customEditors">
    			<map>
    				<entry key="java.util.Date">
    					<ref local="dateEditor" />
    				</entry>
    			</map>
    		</property>
    	</bean>

    	<bean id="customer" class="com.mkyong.common.Customer">
    		<property name="date" value="2010-02-31" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring/spring-how-to-pass-a-date-into-bean-property-customdateeditor/](http://www.mkyong.com/spring/spring-how-to-pass-a-date-into-bean-property-customdateeditor/)
