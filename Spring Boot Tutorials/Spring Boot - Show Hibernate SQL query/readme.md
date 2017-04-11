Add the following lines in `application.properties` to log the Hibernate SQL query.

application.properties

    #show sql statement
    logging.level.org.hibernate.SQL=debug

    #show sql values
    logging.level.org.hibernate.type.descriptor.sql=trace

## 1\. org.hibernate.SQL=debug

application.properties

    logging.level.org.hibernate.SQL=debug

1.1 Select query.

Console

    2017-02-23 21:36:42 DEBUG org.hibernate.SQL - select customer0_.id as id1_0_,
    	customer0_.created_date as created_date2_0_, customer0_.email as email3_0_,
    	customer0_.name as name4_0_ from customer customer0_

## 2\. org.hibernate.type.descriptor.sql=trace

application.properties

    logging.level.org.hibernate.SQL=debug
    logging.level.org.hibernate.type.descriptor.sql=trace

2.1 Select query and its values.

Console

    2017-02-23 21:39:23 DEBUG org.hibernate.SQL - select customer0_.id as id1_0_,
    	customer0_.created_date as created_date2_0_, customer0_.email as email3_0_,
    	customer0_.name as name4_0_ from customer customer0_

    	2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([id1_0_] : [BIGINT]) - [1]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([created_date2_0_] : [TIMESTAMP]) - [2017-02-11 00:00:00.0]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([email3_0_] : [VARCHAR]) - [111@yahoo.com]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([name4_0_] : [VARCHAR]) - [mkyong]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([id1_0_] : [BIGINT]) - [2]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([created_date2_0_] : [TIMESTAMP]) - [2017-02-12 00:00:00.0]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([email3_0_] : [VARCHAR]) - [222@yahoo.com]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([name4_0_] : [VARCHAR]) - [yflow]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([id1_0_] : [BIGINT]) - [3]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([created_date2_0_] : [TIMESTAMP]) - [2017-02-13 00:00:00.0]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([email3_0_] : [VARCHAR]) - [333@yahoo.com]
    2017-02-23 21:39:23 TRACE o.h.t.descriptor.sql.BasicExtractor - extracted value ([name4_0_] : [VARCHAR]) - [zilap]

2.2 Insert query and its values.

Console

    2017-02-23 21:44:15 DEBUG org.hibernate.SQL - select customer_seq.nextval from dual
    2017-02-23 21:44:15 DEBUG org.hibernate.SQL - insert into customer (created_date, email, name, id) values (?, ?, ?, ?)

    2017-02-23 21:44:15 TRACE o.h.type.descriptor.sql.BasicBinder - binding parameter [1] as [TIMESTAMP] - [Thu Feb 23 21:44:15 SGT 2017]
    2017-02-23 21:44:15 TRACE o.h.type.descriptor.sql.BasicBinder - binding parameter [2] as [VARCHAR] - [aa]
    2017-02-23 21:44:15 TRACE o.h.type.descriptor.sql.BasicBinder - binding parameter [3] as [VARCHAR] - [a]
    2017-02-23 21:44:15 TRACE o.h.type.descriptor.sql.BasicBinder - binding parameter [4] as [BIGINT] - [1]

[http://www.mkyong.com/spring-boot/spring-boot-show-hibernate-sql-query/](http://www.mkyong.com/spring-boot/spring-boot-show-hibernate-sql-query/)
