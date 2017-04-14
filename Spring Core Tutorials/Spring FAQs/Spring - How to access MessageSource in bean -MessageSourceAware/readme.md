In last tutorial, you are able to get the [MessageSource via ApplicationContext](http://www.mkyong.com/spring/spring-resource-bundle-with-resourcebundlemessagesource-example/). But for a bean to get the MessageSource, you have to implement the **MessageSourceAware** interface.

## Example

A **CustomerService** class, implement the **MessageSourceAware** interface, has a setter method to set the **MessageSource** property.

During Spring container initialization, if any class which implements the **MessageSourceAware** interface, Spring will automatically inject the MessageSource into the class via **setMessageSource(MessageSource messageSource)** setter method.

    package com.mkyong.customer.services;

    import java.util.Locale;

    import org.springframework.context.MessageSource;
    import org.springframework.context.MessageSourceAware;

    public class CustomerService implements MessageSourceAware
    {
    	private MessageSource messageSource;

    	public void setMessageSource(MessageSource messageSource) {
    		this.messageSource = messageSource;
    	}

    	public void printMessage(){
    		String name = messageSource.getMessage("customer.name",
        			new Object[] { 28, "http://www.mkyong.com" }, Locale.US);

        	System.out.println("Customer name (English) : " + name);

        	String namechinese = messageSource.getMessage("customer.name",
        			new Object[] { 28, "http://www.mkyong.com" },
                            Locale.SIMPLIFIED_CHINESE);

        	System.out.println("Customer name (Chinese) : " + namechinese);
    	}

    }

Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        		new ClassPathXmlApplicationContext(
    			    new String[] {"locale.xml","Spring-Customer.xml"});

        	CustomerService cust = (CustomerService)context.getBean("customerService");
        	cust.printMessage();
        }
    }

[http://www.mkyong.com/spring/spring-how-to-access-messagesource-in-bean-messagesourceaware/](http://www.mkyong.com/spring/spring-how-to-access-messagesource-in-bean-messagesourceaware/)
