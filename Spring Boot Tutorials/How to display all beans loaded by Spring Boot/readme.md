In Spring Boot, you can use `appContext.getBeanDefinitionNames()` to get all the beans loaded by the Spring container.

## 1\. CommandLineRunner as Interface

Application.java

    package com.mkyong;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.ApplicationContext;

    import java.util.Arrays;

    @SpringBootApplication
    public class Application implements CommandLineRunner {

        @Autowired
        private ApplicationContext appContext;

        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }

        @Override
        public void run(String... args) throws Exception {

            String[] beans = appContext.getBeanDefinitionNames();
            Arrays.sort(beans);
            for (String bean : beans) {
                System.out.println(bean);
            }

        }

    }

Output

Console.java

    application
    customerRepository
    customerRepositoryImpl
    dataSource
    dataSourceInitializedPublisher
    dataSourceInitializer
    dataSourceInitializerPostProcessor
    emBeanDefinitionRegistrarPostProcessor
    entityManagerFactory
    entityManagerFactoryBuilder
    hikariPoolDataSourceMetadataProvider
    jdbcTemplate
    jpaContext
    //...

## 2\. CommandLineRunner as Bean

Just different ways to print the loaded beans.

Application.java

    package com.mkyong;

    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.Bean;

    import java.util.Arrays;

    @SpringBootApplication
    public class Application {

        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }

        @Bean
        public CommandLineRunner run(ApplicationContext appContext) {
            return args -> {

                String[] beans = appContext.getBeanDefinitionNames();
                Arrays.stream(beans).sorted().forEach(System.out::println);

            };
        }

    }

[http://www.mkyong.com/spring-boot/how-to-display-all-beans-loaded-by-spring-boot/](http://www.mkyong.com/spring-boot/how-to-display-all-beans-loaded-by-spring-boot/)
