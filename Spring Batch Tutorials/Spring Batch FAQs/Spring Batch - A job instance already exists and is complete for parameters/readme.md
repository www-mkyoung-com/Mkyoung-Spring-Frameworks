  Working with Spring Batch 2.2.0.RELEASE, and launches the job with Spring Scheduler.

CustomJobLauncher.java

    package com.mkyong.batch;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecution;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    @Component
    public class CustomJobLauncher {

    	@Autowired
    	JobLauncher jobLauncher;

    	@Autowired
    	Job job;

    	public void run() {

    	  try {

    		JobExecution execution = jobLauncher.run(job, new JobParameters());
    		System.out.println("Exit Status : " + execution.getStatus());

    	  } catch (Exception e) {
    		e.printStackTrace();
    	  }

    	}

    }

job-config.xml

    <bean id="customJobLauncher" class="com.mkyong.batch.CustomJobLauncher" />

      <task:scheduled-tasks>
    <task:scheduled ref="customJobLauncher" method="run" fixed-delay="10000" />
      </task:scheduled-tasks>

## Problem

The batch job is running successful in the first time only, when it launches the second time (after 10 seconds) it prompts following error messages.

    org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException:
    	A job instance already exists and is complete for parameters={}.
            If you want to run this job again, change the parameters.
    	at org.springframework.batch.core.repository.support.SimpleJobRepository.createJobExecution(SimpleJobRepository.java:126)
    	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
    	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)

## Solution

Refer error message above “**If you want to run this job again, change the parameters.**” The formula is `JobInstance = JobParameters + Job`. If you do not have any parameters for `JobParameters`, just pass a current time as parameter to create a new `JobInstance`. For example,

CustomJobLauncher.java

    //...

    @Component
    public class CustomJobLauncher {

    	@Autowired
    	JobLauncher jobLauncher;

    	@Autowired
    	Job job;

    	public void run() {

    	  try {
    		JobParameters jobParameters =
    		  new JobParametersBuilder()
    		  .addLong("time",System.currentTimeMillis()).toJobParameters();

    		JobExecution execution = jobLauncher.run(job, jobParameters);
    		System.out.println("Exit Status : " + execution.getStatus());

    	  } catch (Exception e) {
    		e.printStackTrace();
    	  }

    	}

    }

## References

1.  [Spring Batch : Configuring and Running a Job](http://static.springsource.org/spring-batch/reference/html/configureJob.html)
2.  [How to create new job instance](http://forum.springsource.org/showthread.php?58319-How-to-create-new-job-instance)

[http://www.mkyong.com/spring-batch/spring-batch-a-job-instance-already-exists-and-is-complete-for-parameters/](http://www.mkyong.com/spring-batch/spring-batch-a-job-instance-already-exists-and-is-complete-for-parameters/)
