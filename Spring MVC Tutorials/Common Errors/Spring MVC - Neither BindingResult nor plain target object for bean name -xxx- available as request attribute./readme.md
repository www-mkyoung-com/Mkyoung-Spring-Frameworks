## Problem

Recently, just converted the [Spring MVC xml-based form controller](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/) to [annotation-based form controller](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-annotation-example/), and hits the following error message.  

_SEVERE: Neither BindingResult nor plain target object for bean name ‘customerForm’ available as request attribute  
java.lang.IllegalStateException: Neither BindingResult nor plain target object for bean name ‘customerForm’ available as request attribute_  

Above error message is clearly indicated that the “customerForm” bean is not exists, and i 100% sure the view resolver is configured properly and the “CustomerForm.jsp” view page is existed.

_Form Controller_

    @Controller
    @RequestMapping("/customer.htm")
    public class CustomerController{

           @RequestMapping(method = RequestMethod.GET)
    	public String initForm(ModelMap model){
    		//return form view
    		return "CustomerForm";
    	}

_View Resolver_

    ...
    <bean id="viewResolver"
          class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
                  <property name="prefix">
                      <value>/WEB-INF/pages/</value>
                  </property>
                  <property name="suffix">
                     <value>.jsp</value>
                 </property>
            </bean>

## Solution

The root caused is the incorrect view name in JSP page, see below.

    <form:form method="POST" commandName="customerForm">

The “customerForm” is not exists in the controller mapping anymore, see annotation mapping **@RequestMapping(“/customer.htm”)**, it should change to “customer”.

    <form:form method="POST" commandName="customer">

## Similar Cases

I’ve seen quite many similar cases happened in validator or SimpleFormController class as well. To solve it, just make sure the mapping name is matched or existed.

[http://www.mkyong.com/spring-mvc/spring-mvc-neither-bindingresult-nor-plain-target-object-for-bean-name-xxx-available-as-request-attribute/](http://www.mkyong.com/spring-mvc/spring-mvc-neither-bindingresult-nor-plain-target-object-for-bean-name-xxx-available-as-request-attribute/)
