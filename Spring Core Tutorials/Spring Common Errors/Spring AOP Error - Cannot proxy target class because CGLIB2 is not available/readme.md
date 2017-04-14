In Spring AOP, you have to include the **cglib** library into your build path to avoid the “**Cannot proxy target class because CGLIB2 is not available**” error message.

    Exception in thread "main" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'customerServiceProxy': FactoryBean threw exception on object creation; nested exception is org.springframework.aop.framework.AopConfigException: Cannot proxy target class because CGLIB2 is not available. Add CGLIB to the class path or specify proxy interfaces.
    	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport$1.run(FactoryBeanRegistrySupport.java:127)
    	at java.security.AccessController.doPrivileged(Native Method)
    	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport.doGetObjectFromFactoryBean(FactoryBeanRegistrySupport.java:116)
    	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport.getObjectFromFactoryBean(FactoryBeanRegistrySupport.java:91)
    	at org.springframework.beans.factory.support.AbstractBeanFactory.getObjectForBeanInstance(AbstractBeanFactory.java:1288)
    	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:217)
    	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:185)
    	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:164)
    	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:880)
    	at com.mkyong.common.App.main(App.java:14)
    Caused by: org.springframework.aop.framework.AopConfigException: Cannot proxy target class because CGLIB2 is not available. Add CGLIB to the class path or specify proxy interfaces.
    	at org.springframework.aop.framework.DefaultAopProxyFactory.createAopProxy(DefaultAopProxyFactory.java:67)
    	at org.springframework.aop.framework.ProxyCreatorSupport.createAopProxy(ProxyCreatorSupport.java:106)
    	at org.springframework.aop.framework.ProxyFactoryBean.getSingletonInstance(ProxyFactoryBean.java:317)
    	at org.springframework.aop.framework.ProxyFactoryBean.getObject(ProxyFactoryBean.java:243)
    	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport$1.run(FactoryBeanRegistrySupport.java:121)
    	... 9 more

## Solution

You can download the cglib library from …

1\. Cglib official web site  
[http://cglib.sourceforge.net/](http://cglib.sourceforge.net/)

2\. Maven repository  
[http://repo1.maven.org/maven2/cglib/cglib/2.2/](http://repo1.maven.org/maven2/cglib/cglib/2.2/)

If you are using Maven, you can just include the Maven dependency.

    <!-- AOP dependency -->
        <dependency>
        	<groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>2.2</version>
        </dependency>

[http://www.mkyong.com/spring/spring-aop-error-cannot-proxy-target-class-because-cglib2-is-not-available/](http://www.mkyong.com/spring/spring-aop-error-cannot-proxy-target-class-because-cglib2-is-not-available/)
