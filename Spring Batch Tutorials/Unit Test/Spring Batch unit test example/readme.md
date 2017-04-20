In this tutorial, we will show you how to unit test Spring batch jobs with jUnit and TestNG frameworks. To unit test batch job, declares `spring-batch-test.jar`, _@autowired_ the `JobLauncherTestUtils`, launch the job or step, and assert the execution status.

## 1\. Unit Test Dependencies

To unit test Spring batch, declares following dependencies :

pom.xml

    <!-- Spring Batch dependencies -->
    <dependency>
    	<groupId>org.springframework.batch</groupId>
    	<artifactId>spring-batch-core</artifactId>
    	<version>2.2.0.RELEASE</version>
    </dependency>
    <dependency>
    	<groupId>org.springframework.batch</groupId>
    	<artifactId>spring-batch-infrastructure</artifactId>
    	<version>2.2.0.RELEASE</version>
    </dependency>

    <!-- Spring Batch unit test -->
    <dependency>
    	<groupId>org.springframework.batch</groupId>
    	<artifactId>spring-batch-test</artifactId>
    	<version>2.2.0.RELEASE</version>
    </dependency>

    <!-- Junit -->
    <dependency>
    	<groupId>junit</groupId>
    	<artifactId>junit</artifactId>
    	<version>4.11</version>
    	<scope>test</scope>
    </dependency>

    <!-- Testng -->
    <dependency>
    	<groupId>org.testng</groupId>
    	<artifactId>testng</artifactId>
    	<version>6.8.5</version>
    	<scope>test</scope>
    </dependency>

## 2\. Spring Batch Jobs

A simple job, later unit test the execution status.

spring-batch-job.xml

    <!-- ...... -->
        <batch:job id="testJob">
    <batch:step id="step1">
        <batch:tasklet>
    	<batch:chunk reader="xmlItemReader"
    		writer="oracleItemWriter"
    		commit-interval="1">
    	</batch:chunk>
        </batch:tasklet>
    </batch:step>
        </batch:job>

## 3\. jUnit Examples

Launches a the job and assert the execution status.

AppTest.java

    package com.mkyong;

    import static org.junit.Assert.assertEquals;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.batch.core.BatchStatus;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.test.JobLauncherTestUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {
        "classpath:spring/batch/jobs/spring-batch-job.xml",
        "classpath:spring/batch/config/context.xml",
        "classpath:spring/batch/config/test-context.xml"})
    public class AppTest {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Test
        public void launchJob() throws Exception {

    	//testing a job
            JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    	//Testing a individual step
            //JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");

            assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        }
    }

_P.S Assume `context.xml` is declared all the require Spring batch core components, like jobRepository and etc._

This `JobLauncherTestUtils` must be declares manually.

test-context.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
    	http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

        <!-- Spring should auto load this bean -->
        <bean class="org.springframework.batch.test.JobLauncherTestUtils"/>

    </beans>

## 4\. TestNG Examples

Equivalent example in TestNG framework.

AppTest2.java

    package com.mkyong;

    import org.springframework.batch.core.BatchStatus;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.test.JobLauncherTestUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
    import org.testng.Assert;
    import org.testng.annotations.Test;

    @ContextConfiguration(locations = {
        "classpath:spring/batch/jobs/spring-batch-job.xml",
        "classpath:spring/batch/config/context.xml",
        "classpath:spring/batch/config/test-context.xml"})
    public class AppTest2 extends AbstractTestNGSpringContextTests {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Test
        public void launchJob() throws Exception {

            JobExecution jobExecution = jobLauncherTestUtils.launchJob();
            Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);

        }
    }

Done.

[http://www.mkyong.com/spring-batch/spring-batch-unit-test-example/](http://www.mkyong.com/spring-batch/spring-batch-unit-test-example/)
