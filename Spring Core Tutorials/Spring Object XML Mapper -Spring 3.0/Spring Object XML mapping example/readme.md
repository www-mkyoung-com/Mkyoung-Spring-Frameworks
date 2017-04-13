The Spring’s Object/XML Mapping, is converting Object to XML or vice verse. This process is also known as

1.  **XML Marshalling** – Convert Object to XML.
2.  **XML UnMarshalling** – Convert XML to Object.

In this tutorial, we show you how to use Spring’s oxm to do the conversion, **Object <--- Spring oxm ---> XML**.

**Note**  
No nonsense, for why and what benefits of using Spring’s oxm, read this [official Spring Object/XML mapping](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/oxm.html) article.

## 1\. Project Dependency

Dependencies in this example.

**Note**  
Spring’s oxm itself doesn’t handle the XML marshalling or UnMarshalling, it depends developer to inject their prefer XML binding framework. In this case, you will use Castor binding framework.

    <properties>
    	<spring.version>3.0.5.RELEASE</spring.version>
    </properties>

    <dependencies>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-context</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- spring oxm -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-oxm</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- Uses Castor for XML -->
    	<dependency>
    		<groupId>org.codehaus.castor</groupId>
    		<artifactId>castor</artifactId>
    		<version>1.2</version>
    	</dependency>

    	<!-- Castor need this -->
    	<dependency>
    		<groupId>xerces</groupId>
    		<artifactId>xercesImpl</artifactId>
    		<version>2.8.1</version>
    	</dependency>

    </dependencies>

## 2\. Simple Object

A simple object, later convert it into XML file.

    package com.mkyong.core.model;

    public class Customer {

    	String name;
    	int age;
    	boolean flag;
    	String address;

    	//standard getter, setter and toString() methods.
    }

## 3\. Marshaller and Unmarshaller

This class will handle the conversion via Spring’s oxm interfaces : `Marshaller` and `Unmarshaller`.

    package com.mkyong.core;

    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import javax.xml.transform.stream.StreamResult;
    import javax.xml.transform.stream.StreamSource;
    import org.springframework.oxm.Marshaller;
    import org.springframework.oxm.Unmarshaller;

    public class XMLConverter {

    	private Marshaller marshaller;
    	private Unmarshaller unmarshaller;

    	public Marshaller getMarshaller() {
    		return marshaller;
    	}

    	public void setMarshaller(Marshaller marshaller) {
    		this.marshaller = marshaller;
    	}

    	public Unmarshaller getUnmarshaller() {
    		return unmarshaller;
    	}

    	public void setUnmarshaller(Unmarshaller unmarshaller) {
    		this.unmarshaller = unmarshaller;
    	}

    	public void convertFromObjectToXML(Object object, String filepath)
    		throws IOException {

    		FileOutputStream os = null;
    		try {
    			os = new FileOutputStream(filepath);
    			getMarshaller().marshal(object, new StreamResult(os));
    		} finally {
    			if (os != null) {
    				os.close();
    			}
    		}
    	}

    	public Object convertFromXMLToObject(String xmlfile) throws IOException {

    		FileInputStream is = null;
    		try {
    			is = new FileInputStream(xmlfile);
    			return getUnmarshaller().unmarshal(new StreamSource(is));
    		} finally {
    			if (is != null) {
    				is.close();
    			}
    		}
    	}

    }

## 4\. Spring Configuration

In Spring’s bean configuration file, inject `CastorMarshaller` as the XML binding framework.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="XMLConverter" class="com.mkyong.core.XMLConverter">
    		<property name="marshaller" ref="castorMarshaller" />
    		<property name="unmarshaller" ref="castorMarshaller" />
    	</bean>
    	<bean id="castorMarshaller" class="org.springframework.oxm.castor.CastorMarshaller" />

    </beans>

## 5\. Test

Run it.

    package com.mkyong.core;

    import java.io.IOException;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import com.mkyong.core.model.Customer;

    public class App {
    	private static final String XML_FILE_NAME = "customer.xml";

    	public static void main(String[] args) throws IOException {
    		ApplicationContext appContext = new ClassPathXmlApplicationContext("App.xml");
    		XMLConverter converter = (XMLConverter) appContext.getBean("XMLConverter");

    		Customer customer = new Customer();
    		customer.setName("mkyong");
    		customer.setAge(30);
    		customer.setFlag(true);
    		customer.setAddress("This is address");

    		System.out.println("Convert Object to XML!");
    		//from object to XML file
    		converter.convertFromObjectToXML(customer, XML_FILE_NAME);
    		System.out.println("Done \n");

    		System.out.println("Convert XML back to Object!");
    		//from XML to object
    		Customer customer2 = (Customer)converter.convertFromXMLToObject(XML_FILE_NAME);
    		System.out.println(customer2);
    		System.out.println("Done");

    	}
    }

Output

    Convert Object to XML!
    Done

    Convert XML back to Object!
    Customer [name=mkyong, age=30, flag=true, address=This is address]
    Done

The following XML file “**customer.xml**” is generated in your project root folder.

_File : customer.xml_

    <?xml version="1.0" encoding="UTF-8"?>
    <customer flag="true" age="30">
    	<address>This is address</address>
    	<name>mkyong</name>
    </customer>

## Castor XML Mapping

Wait, why flag and age are converted as attribute? Is that a way to control which field should use as attribute or element? Of course, you can use [Castor XML mapping](http://www.castor.org/xml-mapping.html%20target=) to define the relationship between Object and XML.

Create following mapping file, and put it into your project classpath.

_File : mapping.xml_

    <mapping>
    	<class name="com.mkyong.core.model.Customer">

    		<map-to xml="customer" />

    		<field name="age" type="integer">
    			<bind-xml name="age" node="attribute" />
    		</field>

    		<field name="flag" type="boolean">
    			<bind-xml name="flag" node="element" />
    		</field>

    		<field name="name" type="string">
    			<bind-xml name="name" node="element" />
    		</field>

    		<field name="address" type="string">
    			<bind-xml name="address" node="element" />
    		</field>
    	</class>
    </mapping>

In Spring bean configuration file, inject above **mapping.xml** into CastorMarshaller via “**mappingLocation**“.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="XMLConverter" class="com.mkyong.core.XMLConverter">
    		<property name="marshaller" ref="castorMarshaller" />
    		<property name="unmarshaller" ref="castorMarshaller" />
    	</bean>
    	<bean id="castorMarshaller" class="org.springframework.oxm.castor.CastorMarshaller" >
    		<property name="mappingLocation" value="classpath:mapping.xml" />
    	</bean>

    </beans>

Test it again, the XML file “**customer.xml**” will be updated.

_File : customer.xml_

    <?xml version="1.0" encoding="UTF-8"?>
    <customer age="30">
    	<flag>true</flag>
    	<name>mkyong</name>
    	<address>This is address</address>
    </customer>

[http://www.mkyong.com/spring3/spring-objectxml-mapping-example/](http://www.mkyong.com/spring3/spring-objectxml-mapping-example/)
