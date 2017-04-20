![spring-batch-listeners](http://www.mkyong.com/wp-content/uploads/2013/07/spring-batch-listeners.png)

In Spring batch, there are six “listeners” to intercept the step execution, I believe the class name should be self-explanatory.

1.  StepExecutionListener
2.  ItemReadListener
3.  ItemProcessListener
4.  ItemWriteListener
5.  ChunkListener
6.  SkipListener

## 1\. Listener Example

Three listener examples, do nothing but print out a message.

CustomStepListener.java

    package com.mkyong.listeners;

    import org.springframework.batch.core.ExitStatus;
    import org.springframework.batch.core.StepExecution;
    import org.springframework.batch.core.StepExecutionListener;

    public class CustomStepListener implements StepExecutionListener {

    	@Override
    	public void beforeStep(StepExecution stepExecution) {
    		System.out.println("StepExecutionListener - beforeStep");
    	}

    	@Override
    	public ExitStatus afterStep(StepExecution stepExecution) {
    		System.out.println("StepExecutionListener - afterStep");
    		return null;
    	}

    }

CustomItemReaderListener.java

    package com.mkyong.listeners;

    import org.springframework.batch.core.ItemReadListener;
    import com.mkyong.Domain;

    public class CustomItemReaderListener implements ItemReadListener<Domain> {

    	@Override
    	public void beforeRead() {
    		System.out.println("ItemReadListener - beforeRead");
    	}

    	@Override
    	public void afterRead(Domain item) {
    		System.out.println("ItemReadListener - afterRead");
    	}

    	@Override
    	public void onReadError(Exception ex) {
    		System.out.println("ItemReadListener - onReadError");
    	}

    }

CustomItemWriterListener.java

    package com.mkyong.listeners;

    import java.util.List;
    import org.springframework.batch.core.ItemWriteListener;
    import com.mkyong.Domain;

    public class CustomItemWriterListener implements ItemWriteListener<Domain> {

    	@Override
    	public void beforeWrite(List<? extends Domain> items) {
    		System.out.println("ItemWriteListener - beforeWrite");
    	}

    	@Override
    	public void afterWrite(List<? extends Domain> items) {
    		System.out.println("ItemWriteListener - afterWrite");
    	}

    	@Override
    	public void onWriteError(Exception exception, List<? extends Domain> items) {
    		System.out.println("ItemWriteListener - onWriteError");
    	}

    }

## 2\. Batch Jobs

A batch job, attached above three listeners.

spring-batch-job.xml

    <bean id="customStepListener"
              class="com.mkyong.listeners.CustomStepListener" />
        <bean id="customItemReaderListener"
              class="com.mkyong.listeners.CustomItemReaderListener" />
        <bean id="customItemWriterListener"
              class="com.mkyong.listeners.CustomItemWriterListener" />

        <job id="readFileJob" xmlns="http://www.springframework.org/schema/batch">
    <step id="step1">
        <tasklet>
    	<chunk reader="multiResourceReader" writer="flatFileItemWriter"
    		commit-interval="1" />
    	    <listeners>
    		<listener ref="customStepListener" />
    		<listener ref="customItemReaderListener" />
    		<listener ref="customItemWriterListener" />
    	    </listeners>
        </tasklet>
    </step>
        </job>

Assume 3 records from a csv file are loaded and write it somewhere, here’s the console output :

    StepExecutionListener - beforeStep

    ItemReadListener - beforeRead
    ItemReadListener - afterRead
    ItemWriteListener - beforeWrite
    ItemWriteListener - afterWrite

    ItemReadListener - beforeRead
    ItemReadListener - afterRead
    ItemWriteListener - beforeWrite
    ItemWriteListener - afterWrite

    ItemReadListener - beforeRead
    ItemReadListener - afterRead
    ItemWriteListener - beforeWrite
    ItemWriteListener - afterWrite

    StepExecutionListener - afterStep

[http://www.mkyong.com/spring-batch/spring-batch-listeners-example/](http://www.mkyong.com/spring-batch/spring-batch-listeners-example/)
