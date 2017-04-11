Here are few ways to disable the Spring logo banner below :

    .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v1.5.1.RELEASE)

**Note**  
You may interest in this – [Spring Boot – Custom Banner example](http://www.mkyong.com/spring-boot/spring-boot-custom-banner-example/)

## Solution

1. `SpringApplication` main method.

SpringBootConsoleApplication.java

    package com.mkyong;

    import org.springframework.boot.Banner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class SpringBootConsoleApplication {

        public static void main(String[] args) throws Exception {

            SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
            app.setBannerMode(Banner.Mode.OFF);
            app.run(args);

        }

    }

**Note**  
There are 3 `Banner.Mode`

1.  OFF – Disable printing of the banner.
2.  CONSOLE – Print the banner to System.out.
3.  LOG – Print the banner to the log file.

2\. Application properties file.

application.properties

    spring.main.banner-mode=off

3\. Application YAML file.

application.yml

    spring:
      main:
        banner-mode:"off"

4\. Pass as a system property

Terminal

    $ java -Dspring.main.banner-mode=off -jar spring-boot-simple-1.0.jar

[http://www.mkyong.com/spring-boot/spring-boot-how-to-disable-the-spring-logo-banner/](http://www.mkyong.com/spring-boot/spring-boot-how-to-disable-the-spring-logo-banner/)
