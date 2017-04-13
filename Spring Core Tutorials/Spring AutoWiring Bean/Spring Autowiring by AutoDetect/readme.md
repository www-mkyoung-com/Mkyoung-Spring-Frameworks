In Spring, “**Autowiring by AutoDetect**“, means chooses “[autowire by constructor](http://www.mkyong.com/spring/spring-autowiring-by-constructor/)” if default constructor (argument with any data type), otherwise uses “[autowire by type](http://www.mkyong.com/spring/spring-autowiring-by-type/)“.

See an example of Spring “auto wiring by autodetect”. Auto wiring the “kungfu” bean into “panda”, via constructor or type (base on the implementation of panda bean).

    <bean id="panda" class="com.mkyong.common.Panda" autowire="autodetect" />

    <bean id="kungfu" class="com.mkyong.common.KungFu" >
    	<property name="name" value="Shao lin" />
    </bean>

## 1\. AutoDetect – by Constructor

If a default constructor is supplied, auto detect will chooses wire by constructor.

    package com.mkyong.common;

    public class Panda {
    	private KungFu kungfu;

    	public Panda(KungFu kungfu) {
    		System.out.println("autowiring by constructor");
    		this.kungfu = kungfu;
    	}

    	public KungFu getKungfu() {
    		return kungfu;
    	}

    	public void setKungfu(KungFu kungfu) {
    		System.out.println("autowiring by type");
    		this.kungfu = kungfu;
    	}

    	//...
    }

_Output_

    autowiring by constructor
    Person [kungfu=Language [name=Shao lin]]

## 2\. AutoDetect – by Type

If a default constructor is not found, auto detect will chooses wire by type.

    package com.mkyong.common;

    public class Panda {
    	private KungFu kungfu;

    	public KungFu getKungfu() {
    		return kungfu;
    	}

    	public void setKungfu(KungFu kungfu) {
    		System.out.println("autowiring by type");
    		this.kungfu = kungfu;
    	}

    	//...
    }

_Output_

    autowiring by type
    Person [kungfu=Language [name=Shao lin]]

[http://www.mkyong.com/spring/spring-autowiring-by-autodetect/](http://www.mkyong.com/spring/spring-autowiring-by-autodetect/)
