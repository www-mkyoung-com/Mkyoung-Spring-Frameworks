Following the [official Spring batch unit testing guide](http://static.springsource.org/spring-batch/reference/html/testing.html) to create a standard unit test case.

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {
        "classpath:spring/batch/jobs/job-abc.xml",
        "classpath:spring/batch/config/context.xml"})

    public class AppTest {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Test
        public void launchJob() throws Exception {

            JobExecution jobExecution = jobLauncherTestUtils.launchJob();
            assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        }
    }

_P.S `spring-batch-test.jar` is added to the classpath._

## Problem

When launches above unit test, it prompts `JobLauncherTestUtils` no such bean error message?

    org.springframework.beans.factory.BeanCreationException: Could not autowire field:
    	private org.springframework.batch.test.JobLauncherTestUtils com.mkyong.AppTest.jobLauncherTestUtils;
    	......
    org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type
    	[org.springframework.batch.test.JobLauncherTestUtils] found for dependency:
    	expected at least 1 bean which qualifies as autowire candidate for this dependency.
    	......
    Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
    	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:288)
    	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1122)
    	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireBeanProperties(AbstractAutowireCapableBeanFactory.java:379)
    	at org.springframework.test.context.support.DependencyInjectionTestExecutionListener.injectDependencies(DependencyInjectionTestExecutionListener.java:110)
    	at org.springframework.test.context.support.DependencyInjectionTestExecutionListener.prepareTestInstance(DependencyInjectionTestExecutionListener.java:75)
    	at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:313)
    	...

## Solution

Adding `spring-batch-test.jar` into the classpath will not create the `JobLauncherTestUtils` bean automatically.

To fix it, declares a `JobLauncherTestUtils` bean in one of your Spring configuration file.

spring/batch/config/test-context.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
    		http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

        <bean class="org.springframework.batch.test.JobLauncherTestUtils"/>

    </beans>

And loads it into the unit test.

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {
        "classpath:spring/batch/jobs/job-abc.xml",
        "classpath:spring/batch/config/context.xml",
        "classpath:spring/batch/config/test-context.xml"})
    public class AppTest {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Test
        public void launchJob() throws Exception {

            JobExecution jobExecution = jobLauncherTestUtils.launchJob();
            assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        }
    }

## References

1.  [Spring batch unit test examples – jUnit and TestNG](http://www.mkyong.com/spring-batch/spring-batch-unit-test-example/)
2.  [Spring batch unit testing official guide](http://static.springsource.org/spring-batch/reference/html/testing.html)

[http://www.mkyong.com/spring-batch/nosuchbeandefinitionexception-no-qualifying-bean-of-type-joblaunchertestutils/](http://www.mkyong.com/spring-batch/nosuchbeandefinitionexception-no-qualifying-bean-of-type-joblaunchertestutils/)
