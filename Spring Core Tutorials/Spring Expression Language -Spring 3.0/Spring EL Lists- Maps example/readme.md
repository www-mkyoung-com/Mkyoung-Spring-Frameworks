In this article, we show you how to use Spring EL to get value from **Map** and **List**. Actually, the way of SpEL works with Map and List is exactly same with Java. See example :

    //get map whete key = 'MapA'
    @Value("#{testBean.map['MapA']}")
    private String mapA;

    //get first value from list, list is 0-based.
    @Value("#{testBean.list[0]}")
    private String list;

## Spring EL in Annotation

Here, created a `HashMap` and `ArrayList`, with some initial data for testing.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	@Value("#{testBean.map['MapA']}")
    	private String mapA;

    	@Value("#{testBean.list[0]}")
    	private String list;

    	public String getMapA() {
    		return mapA;
    	}

    	public void setMapA(String mapA) {
    		this.mapA = mapA;
    	}

    	public String getList() {
    		return list;
    	}

    	public void setList(String list) {
    		this.list = list;
    	}

    	@Override
    	public String toString() {
    		return "Customer [mapA=" + mapA + ", list=" + list + "]";
    	}

    }

    package com.mkyong.core;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import org.springframework.stereotype.Component;

    @Component("testBean")
    public class Test {

    	private Map<String, String> map;
    	private List<String> list;

    	public Test() {
    		map = new HashMap<String, String>();
    		map.put("MapA", "This is A");
    		map.put("MapB", "This is B");
    		map.put("MapC", "This is C");

    		list = new ArrayList<String>();
    		list.add("List0");
    		list.add("List1");
    		list.add("List2");

    	}

    	public Map<String, String> getMap() {
    		return map;
    	}

    	public void setMap(Map<String, String> map) {
    		this.map = map;
    	}

    	public List<String> getList() {
    		return list;
    	}

    	public void setList(List<String> list) {
    		this.list = list;
    	}

    }

_Run it_

    Customer obj = (Customer) context.getBean("customerBean");
    System.out.println(obj);

_Output_

    Customer [mapA=This is A, list=List0]

## Spring EL in XML

See equivalent version in bean definition XML file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    		<property name="mapA" value="#{testBean.map['MapA']}" />
    		<property name="list" value="#{testBean.list[0]}" />
    	</bean>

    	<bean id="testBean" class="com.mkyong.core.Test" />

    </beans>

[http://www.mkyong.com/spring3/spring-el-lists-maps-example/](http://www.mkyong.com/spring3/spring-el-lists-maps-example/)
