## Problem

Using Spring3 `@Configuration` to create an application configuration file like below :

    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class AppConfig {

    	@Bean
       //...

    }

However, when run it, it hits following error message :

    org.springframework.context.support.AbstractApplicationContext prepareRefresh
    //...
    Exception in thread "main" java.lang.IllegalStateException:
    CGLIB is required to process @Configuration classes.
    Either add CGLIB to the classpath or remove the following
    @Configuration bean definitions: [appConfig]
    //...
    at com.mkyong.core.App.main(App.java:12)

## Solution

To use `@Configuration` in Spring 3, you need to include the **CGLIB** library manually, just declares it in Maven `pom.xml` file.

    <dependency>
    	<groupId>cglib</groupId>
    	<artifactId>cglib</artifactId>
    	<version>2.2.2</version>
    </dependency>

[http://www.mkyong.com/spring3/cglib-is-required-to-process-configuration-classes/](http://www.mkyong.com/spring3/cglib-is-required-to-process-configuration-classes/)
