Run the Spring Boot tests and find out the console is full of DEBUG logs, try to control the logging level in the `application.yml`, but the unit tests just ignore the settings.

    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.junit4.SpringRunner;
    //...

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class TestSequenceService {

        @Autowired
        SequenceService sequenceService;

        @Test
        public void testSequence() {

            //...
        }

    }

Console

    21:34:19.647 [main] DEBUG org.springframework.test.context.junit4.SpringJUnit4ClassRunner - SpringJUnit4ClassRunner ...
    21:34:19.653 [main] DEBUG org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class ...
    21:34:19.659 [main] DEBUG org.springframework...
    21:34:19.673 [main] DEBUG org.springframework...
    21:34:19.683 [main] DEBUG org.springframework...
    //...

## Solution

To fix it, create a `logback-test.xml` in the `src/test/resources` folder.

![logback-test.xml](http://www.mkyong.com/wp-content/uploads/2017/03/spring-boot-test-logback.png)

src/test/resources/logback-test.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <configuration>
        <include resource="org/springframework/boot/logging/logback/base.xml" />
    	<logger name="org.springframework" level="ERROR"/>
        <logger name="com.mkyong" level="DEBUG"/>
        <logger name="org.mongodb" level="ERROR"/>
    </configuration>

[http://www.mkyong.com/spring-boot/spring-boot-test-how-to-stop-debug-logs/](http://www.mkyong.com/spring-boot/spring-boot-test-how-to-stop-debug-logs/)
