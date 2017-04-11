<p>In Spring Boot,&nbsp;<code>@Autowired</code>&nbsp;a&nbsp;<code>javax.sql.DataSource</code>, and you will know which database connection pool is used in the application.</p>

## 1\. Test Default

Spring Boot example to print a `javax.sql.DataSource`

**Note**  
Read this official Spring Boot doc – [Connection to a production database](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), to understand the algorithm for choosing a `DataSource` implementations – Tomcat pooling, HikariCP, Commons DBCP and Commons DBCP2.

    package com.mkyong;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    import javax.sql.DataSource;

    @SpringBootApplication
    public class SpringBootConsoleApplication implements CommandLineRunner {

        @Autowired
        DataSource dataSource;

        public static void main(String[] args) throws Exception {
            SpringApplication.run(SpringBootConsoleApplication.class, args);
        }

        @Override
        public void run(String... args) throws Exception {

            System.out.println("DATASOURCE = " + dataSource);

        }

    }

Output, Spring Boot is using Tomcat pooling by default.

    DATASOURCE = org.apache.tomcat.jdbc.pool.DataSource@7c541c15...

## 2\. Test HikariCP

To switch to another connection pool, for example HikariCP, just exclude the default and include the HikariCP in the classpath.

pom.xml

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.apache.tomcat</groupId>
                <artifactId>tomcat-jdbc</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <!-- connection pools -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>2.6.0</version>
    </dependency>

Output

    DATASOURCE = HikariDataSource (HikariPool-1)

[http://www.mkyong.com/spring-boot/spring-boot-how-to-know-which-connection-pool-is-used/](http://www.mkyong.com/spring-boot/spring-boot-how-to-know-which-connection-pool-is-used/)
