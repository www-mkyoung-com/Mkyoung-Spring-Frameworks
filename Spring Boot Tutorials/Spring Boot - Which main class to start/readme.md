If Spring Boot project contains multiple main classes, Spring Boot will fail to start or packag for deployment.

Terminal

    $ mvn package
    #or
    $ mvn spring-boot:run

    Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run (default-cli)
    Execution default-cli of goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run failed:
    Unable to find a single main class from the following candidates
     [com.mkyong.Test, com.mkyong.SpringBootWebApplication] -> [Help 1]

## Maven example

1.1 Define single main class via `start-class` properties

pom.xml

    <properties>
        <!-- The main class to start by executing java -jar -->
        <start-class>com.mkyong.SpringBootWebApplication</start-class>
    </properties>

1.2 Alternatively, define the main class in the `spring-boot-maven-plugin`

pom.xml

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.mkyong.SpringBootWebApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

[http://www.mkyong.com/spring-boot/spring-boot-which-main-class-to-start/](http://www.mkyong.com/spring-boot/spring-boot-which-main-class-to-start/)
