package com.mkyong;

import com.mkyong.dao.CustomerRepository;
import com.mkyong.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.util.List;

import static java.lang.System.exit;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);

        /// Get dbcp2 datasource settings
        // BasicDataSource newds = (BasicDataSource) dataSource;
        // System.out.println("BasicDataSource = " + newds.getInitialSize());

        System.out.println("Display all customers...");
        List<Customer> list = customerRepository.findAll();
        list.forEach(x -> System.out.println(x));

        System.out.println("Done!");

        exit(0);
    }
}