In Spring Security, when authentication is failed, following predefined error messages will be displayed :

    Spring display : Bad credentials

In this article, we show you how to override above error message and display your custom error message. For example,

    Spring display : Bad credentials
    You want override it with this message : Invalid username or password

## Solution

Spring Security stored messages in “**messages.properties**” inside “**spring-security-core.jar**“, see figure below :

![message.properties](http://www.mkyong.com/wp-content/uploads/2011/08/message-properties-spring-security.png)

To override it, find which key generate what error message in spring security **message.properties** file, and redefine it with your own properties file.

## 1\. Override Key and Message

Create a new properties file, put it on project classpath, and override the Spring’s “key” with your custom error message. In this case, just override “`AbstractUserDetailsAuthenticationProvider.badCredentials`“.

_File : mymessages.properties_

    AbstractUserDetailsAuthenticationProvider.badCredentials=Invalid username or password

## 2\. Register ResourceBundleMessageSource

To load above properties file, define `ResourceBundleMessageSource` in Spring bean configuration file.

    <bean id="messageSource"
    class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basenames">
        <list>
    	<value>mymessages</value>
        </list>
    </property>
      </bean>

Now, when authentication is failed, it will display your custom error message “**Invalid username or password**“, instead of the default “**Bad credentials**“.

**Note**  
With this trick, you can override any Spring Security messages easily.

[http://www.mkyong.com/spring-security/display-custom-error-message-in-spring-security/](http://www.mkyong.com/spring-security/display-custom-error-message-in-spring-security/)
