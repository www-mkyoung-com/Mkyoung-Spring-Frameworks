In Spring batch, the `Tasklet` is an interface, which will be called to perform a single task only, like clean or set up resources before or after any step execution. In this example, we will show you how to use `Tasklet` to clean up the resource (folders) after a batch job is completed.

_P.S The `FileDeletingTasklet` example below is taken from the [Spring Batch samples](http://static.springsource.org/spring-batch/trunk/spring-batch-samples/) project._

## 1\. Tasklet Example

A Java class to implement `Tasklet` interface, and delete all the files in the given directory.

FileDeletingTasklet.java

    package com.mkyong.tasklet;

    import java.io.File;

    import org.springframework.batch.core.StepContribution;
    import org.springframework.batch.core.UnexpectedJobExecutionException;
    import org.springframework.batch.core.scope.context.ChunkContext;
    import org.springframework.batch.core.step.tasklet.Tasklet;
    import org.springframework.batch.repeat.RepeatStatus;
    import org.springframework.beans.factory.InitializingBean;
    import org.springframework.core.io.Resource;
    import org.springframework.util.Assert;

    public class FileDeletingTasklet implements Tasklet, InitializingBean {

      private Resource directory;

      @Override
      public void afterPropertiesSet() throws Exception {
    	Assert.notNull(directory, "directory must be set");
      }

      @Override
      public RepeatStatus execute(StepContribution contribution,
                   ChunkContext chunkContext) throws Exception {

    	File dir = directory.getFile();
    	Assert.state(dir.isDirectory());

    	File[] files = dir.listFiles();
    	for (int i = 0; i < files.length; i++) {
    	  boolean deleted = files[i].delete();
    	  if (!deleted) {
    		throw new UnexpectedJobExecutionException(
                           "Could not delete file " + files[i].getPath());
    	  } else {
    	        System.out.println(files[i].getPath() + " is deleted!");
    	  }
    	}
    	return RepeatStatus.FINISHED;
      }

      public Resource getDirectory() {
    	return directory;
      }

      public void setDirectory(Resource directory) {
    	this.directory = directory;
      }

    }

## 2\. Batch Jobs

A batch job to perform following steps :

**Step 1** - To read multiple files from `csv/inputs/`, and write it to somewhere.  
**Step 2** - After step 1 is completed, run `fileDeletingTasklet` to delete all the files from directory `csv/inputs/`.

spring-batch-job.xml

    <job id="readMultiFileJob" xmlns="http://www.springframework.org/schema/batch">
    <step id="step1" next="deleteDir">
      <tasklet>
    	<chunk reader="multiResourceReader" writer="flatFileItemWriter"
    		commit-interval="1" />
      </tasklet>
    </step>
    <step id="deleteDir">
    	<tasklet ref="fileDeletingTasklet" />
    </step>
      </job>

      <bean id="fileDeletingTasklet" class="com.mkyong.tasklet.FileDeletingTasklet" >
    <property name="directory" value="file:csv/inputs/" />
      </bean>

      <bean id="multiResourceReader"
    class=" org.springframework.batch.item.file.MultiResourceItemReader">
    <property name="resources" value="file:csv/inputs/domain-*.csv" />
    <property name="delegate" ref="flatFileItemReader" />
      </bean>

[http://www.mkyong.com/spring-batch/spring-batch-tasklet-example/](http://www.mkyong.com/spring-batch/spring-batch-tasklet-example/)
