Read following Spring batch job, it reads data from “_domain.csv_“, and map it to a domain object.

job-example.xml

    <bean class="org.springframework.batch.item.file.FlatFileItemReader" >

      <property name="resource" value="file:outputs/csv/domain.csv" />
      <property name="lineMapper">
        <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">

    	<property name="lineTokenizer">
    	  <bean
    		class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    		<property name="names" value="id, domainName, lastModifiedDate" />
    	  </bean>
    	</property>
    	<property name="fieldSetMapper">
    	  <bean
    		class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    		<property name="prototypeBeanName" value="domain" />
    	  </bean>
    	</property>

        </bean>
      </property>

    </bean>

     <bean id="domain" class="com.mkyong.batch.Domain" scope="prototype" />

domain.csv

    1,facebook.com,Mon Jul 15 16:32:21 MYT 2013
    2,google.com,Mon Jul 15 16:32:21 MYT 2013
    3,youtube.com,Mon Jul 15 16:32:21 MYT 2013
    4,yahoo.com,Mon Jul 15 16:32:21 MYT 2013
    5,amazon.com,Mon Jul 15 16:32:21 MYT 2013

Domain.java

    import java.util.Date;

    public class DomainRanking {

    	private int id;
    	private String domainName;
    	private Date lastModifiedDate;

    	//...
    }

## Problem

The problem is how to map/convert the String Date `Mon Jul 15 16:32:21 MYT 2013` to `java.util.Date`? Running above job will prompts following error messages :

    Cannot convert value of type [java.lang.String] to required type [java.util.Date]
            for property 'lastModifiedDate':
    	no matching editors or conversion strategy found

## Solution

Refer to the [BeanWrapperFieldSetMapper JavaDoc](http://static.springsource.org/spring-batch/apidocs/org/springframework/batch/item/file/mapping/BeanWrapperFieldSetMapper.html) :

> To customize the way that FieldSet values are converted to the desired type for injecting into the prototype there are several choices. You can inject PropertyEditor instances directly through the customEditors property…

To fix it, declares a `CustomDateEditor` and inject into `BeanWrapperFieldSetMapper` via `customEditors` property.

job-example.xml

    <bean id="dateEditor"
      class="org.springframework.beans.propertyeditors.CustomDateEditor">
      <constructor-arg>
    	<bean class="java.text.SimpleDateFormat">
                  <constructor-arg value="EEE MMM dd HH:mm:ss z yyyy" />
    	</bean>
      </constructor-arg>
      <constructor-arg value="true" />
    </bean>

    <bean class="org.springframework.batch.item.file.FlatFileItemReader" >

      <property name="resource" value="file:outputs/csv/domain.csv" />
      <property name="lineMapper">
        <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
    	<property name="lineTokenizer">
    	  <bean
    		class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
    		<property name="names" value="id, domainName, lastModifiedDate" />
    	  </bean>
    	</property>
    	<property name="fieldSetMapper">
    	  <bean
    		class="org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper">
    		<property name="prototypeBeanName" value="domain" />
    		<property name="customEditors">
    		  <map>
    			<entry key="java.util.Date">
    			     <ref local="dateEditor" />
    			</entry>
    		  </map>
    		</property>
    	  </bean>
    	</property>

        </bean>
      </property>
    </bean>

    <bean id="domain" class="com.mkyong.batch.Domain" scope="prototype" />

_P.S The String Date “Mon Jul 15 16:32:21 MYT 2013” is represented by “EEE MMM dd HH:mm:ss z yyyy”._

**Note**  
Do not injects the `CustomDateEditor` via `CustomEditorConfigurer` (globally), it will not works.

    <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="customEditors">
      <map>
    	<entry key="java.util.Date">
    		<ref local="dateEditor" />
    	</entry>
      </map>
    </property>
        </bean>

## References

1.  [BeanWrapperFieldSetMapper JavaDoc](http://static.springsource.org/spring-batch/apidocs/org/springframework/batch/item/file/mapping/BeanWrapperFieldSetMapper.html)
2.  [BeanWrapperFieldSetMapper not working for Dates](http://forum.springsource.org/showthread.php?68551-BeanWrapperFieldSetMapper-not-working-for-Dates)
3.  [java.text.SimpleDateFormat JavaDoc](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html)
4.  [Spring Batch – how to convert String from file to Date](http://stackoverflow.com/questions/9059481/spring-batch-how-to-convert-string-from-file-to-date)
5.  [Spring Inject Date Into Bean Property – CustomDateEditor](http://www.mkyong.com/spring/spring-how-to-pass-a-date-into-bean-property-customdateeditor/)
6.  [How To Convert String To Date – Java](http://www.mkyong.com/java/how-to-convert-string-to-date-java/)

[http://www.mkyong.com/spring-batch/how-to-convert-date-in-beanwrapperfieldsetmapper/](http://www.mkyong.com/spring-batch/how-to-convert-date-in-beanwrapperfieldsetmapper/)
