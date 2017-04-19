A quick guide to show you how to run a Spring batch job with `CommandLineJobRunner`.

## 1\. Spring Batch Job Example

A simple job.

resources/spring/batch/jobs/job-read-files.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans ...
       <import resource="../config/context.xml"/>

       <job id="readJob" xmlns="http://www.springframework.org/schema/batch">
          <step id="step1">
    	<tasklet>
    		<chunk reader="flatFileItemReader"
                              writer="flatFileItemWriter" commit-interval="1" />
    	</tasklet>
          </step>
       </job>

    	<!-- ... -->
    </beans>

## 2\. Package Project

Use Maven to package your project into a single jar file – **target/your-project.jar**, and copy all the dependencies into **target/dependency-jars/**.

pom.xml

    <!-- ... -->
      <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.5.1</version>
    <executions>
      <execution>
    	<id>copy-dependencies</id>
    	<phase>package</phase>
    	<goals>
    		<goal>copy-dependencies</goal>
    	</goals>
    	<configuration>
    		<outputDirectory>
    			${project.build.directory}/dependency-jars/
    		</outputDirectory>
    	</configuration>
      </execution>
    </executions>
      </plugin>

    $ mvn package

## 3\. CommandLineJobRunner example

Usage :

    CommandLineJobRunner jobPath <options> jobIdentifier (jobParameters)

To run above spring batch job, type following command :

    $ java -cp "target/dependency-jars/*:target/your-project.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner spring/batch/jobs/job-read-files.xml readJob

For `jobParameters`, append to the end of the command :

    $ java -cp "target/dependency-jars/*:target/your-project.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner spring/batch/jobs/job-read-files.xml readJob file.name=testing.cvs

To run it on a schedule, normally, you can copy above commands into a `.sh` file, and run it with any scheduler commands, like `cron` in *nix. Refer to this example – [Add Jobs To cron Under Linux](http://www.cyberciti.biz/faq/how-do-i-add-jobs-to-cron-under-linux-or-unix-oses/).

_P.S When batch job is running under system scheduler, make sure it can locate your project’s classpath._

[http://www.mkyong.com/spring-batch/run-spring-batch-job-with-commandlinejobrunner/](http://www.mkyong.com/spring-batch/run-spring-batch-job-with-commandlinejobrunner/)
