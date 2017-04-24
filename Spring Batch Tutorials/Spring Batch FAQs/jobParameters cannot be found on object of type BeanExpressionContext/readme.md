Create a simple Spring batch job to write data to a csv file. The csv file name depends on the pass in job’s parameters, interprets by Spring EL .

job-sample.xml

    <bean id="csvFileItemWriter" class="org.springframework.batch.item.file.FlatFileItemWriter">

      <!-- write to this csv file -->
      <property name="resource"
          value="file:outputs/csv/domain.done.#{jobParameters['pid']}.csv" />

      <property name="appendAllowed" value="false" />
      <property name="lineAggregator">
          <bean
    	class="org.springframework.batch.item.file.transform.DelimitedLineAggregator">
    	<property name="delimiter" value="," />
    	<property name="fieldExtractor">
    	    <bean class="org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor">
    		<property name="names" value="id, domainName" />
    	    </bean>
    	</property>
          </bean>
      </property>
    </bean>

Unit test above job :

    public class TestSampleJob extends AbstractTestNGSpringContextTests {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Test
        public void launchJob() throws Exception {

        	JobParameters jobParameters =
        	    new JobParametersBuilder().addString("pid", "10").toJobParameters();

            JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
            Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);

        }
    }

## Problem

It prompts “jobParameters cannot be found” error message :

    Caused by: org.springframework.expression.spel.SpelEvaluationException:
    	EL1008E:(pos 0): Field or property 'jobParameters' cannot be found on object
    	of type 'org.springframework.beans.factory.config.BeanExpressionContext'
    	at org.springframework.expression.spel.ast.PropertyOrFieldReference.readProperty(PropertyOrFieldReference.java:208)
    	at org.springframework.expression.spel.ast.PropertyOrFieldReference.getValueInternal(PropertyOrFieldReference.java:72)
    	at org.springframework.expression.spel.ast.CompoundExpression.getValueInternal(CompoundExpression.java:52)

## Solution

The `jobParameters` bean can not actually be instantiated until the “Step” starts. To fix it, late binding with a scope of “Step” is required.

job-sample.xml

    <bean id="csvFileItemWriter"
    class="org.springframework.batch.item.file.FlatFileItemWriter" scope="step">
    <!-- ...... -->
      </bean>

## Reference

1.  [Spring Batch – Step Scope](http://static.springsource.org/spring-batch/reference/html/configureStep.html#step-scope)

[http://www.mkyong.com/spring-batch/jobparameters-cannot-be-found-on-object-of-type-beanexpressioncontext/](http://www.mkyong.com/spring-batch/jobparameters-cannot-be-found-on-object-of-type-beanexpressioncontext/)
