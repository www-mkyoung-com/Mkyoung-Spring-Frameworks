<p>In Spring Boot + Spring Data JPA application, to support the JSR310&nbsp;<code>java.time.*</code>&nbsp;APIs, we need to register this&nbsp;<code>Jsr310JpaConverters</code>&nbsp;manually.</p>

<pre>
<code>import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
        basePackageClasses = {Application.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class Application {
	//...
}</code></pre>

<p><em>P.S Tested with Spring Boot 1.5.1.RELEASE, Spring Data JPA 1.11.0.RELEASE</em></p>

<h2>1. Full Example</h2>

<p>1.1 A model contains a&nbsp;<code>java.time.LocalDate</code>&nbsp;field.</p>

<pre>
<code>package com.mkyong.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "customer_seq", allocationSize = 1, name = "CUST_SEQ")
    Long id;

    String name;

    @Column(name = "CREATED_DATE")
    LocalDate date;

	//...</code></pre>

<p>&nbsp;</p>

<p>&nbsp;</p>

<pre>
<code>package com.mkyong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.util.Arrays;

//for jsr310 java 8 java.time.*
@EntityScan(
        basePackageClasses = {Application.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext appContext) {
        return args -&gt; {

            System.out.println("hello World!");

        };
    }

}</code></pre>
<p><a href="http://www.mkyong.com/spring-boot/spring-boot-spring-data-jpa-java-8-date-and-time-jsr310/">http://www.mkyong.com/spring-boot/spring-boot-spring-data-jpa-java-8-date-and-time-jsr310/</a></p>
