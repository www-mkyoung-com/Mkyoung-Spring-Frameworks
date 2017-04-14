Spring’s resource loader provides a very generic **getResource()** method to get the resources like (text file, media file, image file…) from file system , classpath or URL. You can get the **getResource()** method from the application context.

Here’s an example to show how to use **getResource()** to load a text file from

**1\. File system**

    Resource resource = appContext.getResource("file:c:\\testing.txt");

**2\. URL path**

    Resource resource =
              appContext.getResource("url:http://www.yourdomain.com/testing.txt");

**3\. Class path**

    Resource resource =
              appContext.getResource("classpath:com/mkyong/common/testing.txt");

You just need to specify the resource location, and the Spring will handle the rest and return you a Resource object.

Full example with `getResource()` method.

    package com.mkyong.common;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import org.springframework.core.io.Resource;
    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext appContext =
        	   new ClassPathXmlApplicationContext(new String[] {"If-you-have-any.xml"});

        	Resource resource =
               appContext.getResource("classpath:com/mkyong/common/testing.txt");

        try{
         	  InputStream is = resource.getInputStream();
              BufferedReader br = new BufferedReader(new InputStreamReader(is));

              String line;
              while ((line = br.readLine()) != null) {
                 System.out.println(line);
           	  }
              br.close();

        	}catch(IOException e){
        		e.printStackTrace();
        	}

        }
    }

## Bean resource loader (ResourceLoaderAware)

Since bean does not have the application context access, how can a bean access a resources? The workaround is implement the **ResourceLoaderAware** interface and create setter method for **ResourceLoader** object. Spring will DI the resource loader into your bean.

    package com.mkyong.customer.services;

    import org.springframework.context.ResourceLoaderAware;
    import org.springframework.core.io.Resource;
    import org.springframework.core.io.ResourceLoader;

    public class CustomerService implements ResourceLoaderAware
    {
    	private ResourceLoader resourceLoader;

    	public void setResourceLoader(ResourceLoader resourceLoader) {
    		this.resourceLoader = resourceLoader;
    	}

    	public Resource getResource(String location){
    		return resourceLoader.getResource(location);
    	}
    }

Bean configuration file

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       <bean id="customerService"
               class="com.mkyong.customer.services.CustomerService" />

    </beans>

Run it

    package com.mkyong.common;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import org.springframework.core.io.Resource;

    import com.mkyong.customer.services.CustomerService;
    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext appContext =
        	   new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});

        	CustomerService cust =
               (CustomerService)appContext.getBean("customerService");

        	Resource resource =
                cust.getResource("classpath:com/mkyong/common/testing.txt");

        try{
              InputStream is = resource.getInputStream();
              BufferedReader br = new BufferedReader(new InputStreamReader(is));

              String line;
              while ((line = br.readLine()) != null) {
         	       System.out.println(line);
              }
              br.close();

        	}catch(IOException e){
        		e.printStackTrace();
        	}

        }
    }

Now you can get the resources from a bean.

## Conclusion

Without this getResource() method, you will need to deal with different resources with different solution, like File object for file system resource, URL object for URL resource. Spring really did a good job with this super generic **getResource()** method, it really save our time to deal with resources.

[http://www.mkyong.com/spring/spring-resource-loader-with-getresource-example/](http://www.mkyong.com/spring/spring-resource-loader-with-getresource-example/)
