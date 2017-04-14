Here’s an example to use Spring to send e-mail that has attachments via Gmail SMTP server. In order to contains the attachment in your e-mail, you have to use Spring’s **JavaMailSender & MimeMessage** , instead of **MailSender & SimpleMailMessage**.

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

You have to use **JavaMailSender** instead of MailSender to send attachments, and attach the resources with **MimeMessageHelper**. In this example, it will get the “c:\\log.txt” text file from your file system (FileSystemResource) as an e-mail attachment.

Beside file system, you can also get any resources from URL path(**UrlResource**), Classpath (**ClassPathResource**), InputStream (**InputStreamResource**)… please refer to Spring’s **AbstractResource** implemented classes.

_File : MailMail.java_

    package com.mkyong.common;

    import javax.mail.MessagingException;
    import javax.mail.internet.MimeMessage;

    import org.springframework.core.io.FileSystemResource;
    import org.springframework.mail.MailParseException;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.mail.javamail.MimeMessageHelper;

    public class MailMail
    {
    	private JavaMailSender mailSender;
    	private SimpleMailMessage simpleMailMessage;

    	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
    		this.simpleMailMessage = simpleMailMessage;
    	}

    	public void setMailSender(JavaMailSender mailSender) {
    		this.mailSender = mailSender;
    	}

    	public void sendMail(String dear, String content) {

    	   MimeMessage message = mailSender.createMimeMessage();

    	   try{
    		MimeMessageHelper helper = new MimeMessageHelper(message, true);

    		helper.setFrom(simpleMailMessage.getFrom());
    		helper.setTo(simpleMailMessage.getTo());
    		helper.setSubject(simpleMailMessage.getSubject());
    		helper.setText(String.format(
    			simpleMailMessage.getText(), dear, content));

    		FileSystemResource file = new FileSystemResource("C:\\log.txt");
    		helper.addAttachment(file.getFilename(), file);

    	     }catch (MessagingException e) {
    		throw new MailParseException(e);
    	     }
    	     mailSender.send(message);
             }
    }

## 3\. Bean configuration file

Configure the mailSender bean, email template and specify the email details for the Gmail SMTP server.

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

    Attachment : log.txt

[http://www.mkyong.com/spring/spring-sending-e-mail-with-attachment/](http://www.mkyong.com/spring/spring-sending-e-mail-with-attachment/)
