In Spring Boot, to change the embedded Tomcat initialized port (8080), update `server.port` properties.

_P.S Tested with Spring Boot 1.4.2.RELEASE_

## 1\. Properties & Yaml

1.1 Update via a properties file.

/src/main/resources/application.properties

    server.port=8888

1.2 Update via a yaml file.

/src/main/resources/application.yml

    server:
      port: 8888

## 2\. EmbeddedServletContainerCustomizer

Update via code, this overrides properties and yaml settings.

CustomContainer.java

    package com.mkyong;

    import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
    import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
    import org.springframework.stereotype.Component;

    @Component
    public class CustomContainer implements EmbeddedServletContainerCustomizer {

    	@Override
    	public void customize(ConfigurableEmbeddedServletContainer container) {

    		container.setPort(8888);

    	}

    }

## 3\. Command Line

Update the port by passing the system properties directly.

Terminal

    java -jar -Dserver.port=8888 spring-boot-example-1.0.jar

[http://www.mkyong.com/spring-boot/spring-boot-how-to-change-tomcat-port/](http://www.mkyong.com/spring-boot/spring-boot-how-to-change-tomcat-port/)
