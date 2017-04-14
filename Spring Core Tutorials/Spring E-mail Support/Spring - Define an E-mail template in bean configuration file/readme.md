In last [Spring’s email tutorial](http://www.mkyong.com/spring/spring-sending-e-mail-via-gmail-smtp-server-with-mailsender/), you hard-code all the email properties and message content in the method body, it’s not practical and should be avoid. You should consider define the email message template in the Spring’s bean configuration file.

## 1\. Project dependency

Add the JavaMail and Spring’s dependency.

_File : pom.xml_

    <project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.mkyong.common</groupId>
      <artifactId>SpringExample</artifactId>
      <packaging>jar</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>SpringExample</name>
      <url>http://maven.apache.org</url>

      <repositories>
      	<repository>
      		<id>Java.Net</id>
      		<url>http://download.java.net/maven/2/</url>
      	</repository>
      </repositories>

      <dependencies>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>

        <!-- Java Mail API -->
        <dependency>
    	    <groupId>javax.mail</groupId>
    	    <artifactId>mail</artifactId>
    	    <version>1.4.3</version>
        </dependency>

        <!-- Spring framework -->
        <dependency>
         	<groupId>org.springframework</groupId>
    	    <artifactId>spring</artifactId>
    	    <version>2.5.6</version>
        </dependency>

      </dependencies>
    </project>

## 2\. Spring’s Mail Sender

A Java class to send email with the Spring’s MailSender interface, and use the **String.format** to replace the email message ‘**%s**‘ with passing variable in bean configuration file.

_File : MailMail.java_

    package com.mkyong.common;

    import org.springframework.mail.MailSender;
    import org.springframework.mail.SimpleMailMessage;

    public class MailMail
    {
    	private MailSender mailSender;
    	private SimpleMailMessage simpleMailMessage;

    	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
    		this.simpleMailMessage = simpleMailMessage;
    	}

    	public void setMailSender(MailSender mailSender) {
    		this.mailSender = mailSender;
    	}

    	public void sendMail(String dear, String content) {

    	   SimpleMailMessage message = new SimpleMailMessage(simpleMailMessage);

    	   message.setText(String.format(
    			simpleMailMessage.getText(), dear, content));

    	   mailSender.send(message);

    	}
    }

## 3\. Bean configuration file

Define the email template ‘**customeMailMessage**‘ and mail sender details in the bean configuration file.

_File : Spring-Mail.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    	<property name="host" value="smtp.gmail.com" />
    	<property name="port" value="587" />
    	<property name="username" value="username" />
    	<property name="password" value="password" />

    	<property name="javaMailProperties">
    	     <props>
               	<prop key="mail.smtp.auth">true</prop>
               	<prop key="mail.smtp.starttls.enable">true</prop>
           	     </props>
    	</property>
    </bean>

    <bean id="mailMail" class="com.mkyong.common.MailMail">
    	<property name="mailSender" ref="mailSender" />
    	<property name="simpleMailMessage" ref="customeMailMessage" />
    </bean>

    <bean id="customeMailMessage"
    	class="org.springframework.mail.SimpleMailMessage">

    	<property name="from" value="from@no-spam.com" />
    	<property name="to" value="to@no-spam.com" />
    	<property name="subject" value="Testing Subject" />
    	<property name="text">
    	   <value>
    		<![CDATA[
    			Dear %s,
    			Mail Content : %s
    		]]>
    	   </value>
            </property>
    </bean>

    </beans>

## 4\. Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
               new ClassPathXmlApplicationContext("Spring-Mail.xml");

        	MailMail mm = (MailMail) context.getBean("mailMail");
            mm.sendMail("Yong Mook Kim", "This is text content");

        }
    }

output

    Dear Yong Mook Kim,
    Mail Content : This is text content

[http://www.mkyong.com/spring/spring-define-an-e-mail-template-in-bean-configuration-file/](http://www.mkyong.com/spring/spring-define-an-e-mail-template-in-bean-configuration-file/)
