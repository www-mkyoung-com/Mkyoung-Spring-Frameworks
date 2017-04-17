In this tutorial, we show you how to do form handling by using annotation in Spring MVC web application.

**Note**  
This annotation-based example is converted from the last Spring MVC [form handling XML-based example](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/). So, please compare and spots the different.

## 1\. SimpleFormController vs @Controller

In XML-based Spring MVC web application, you create a form controller by extending the `SimpleFormController` class.

In annotation-based, you can use _@Controller_ instead.

_SimpleFormController_

    public class CustomerController extends SimpleFormController{
          //...
    }

_Annotation_

    @Controller
    @RequestMapping("/customer.htm")
    public class CustomerController{
          //...
    }

## 2\. formBackingObject() vs RequestMethod.GET

In SimpleFormController, you can initialize the command object for binding in the **formBackingObject()** method. In annotation-based, you can do the same by annotated the method name with **@RequestMapping(method = RequestMethod.GET)**.

_SimpleFormController_

    @Override
    protected Object formBackingObject(HttpServletRequest request)
    	throws Exception {

    	Customer cust = new Customer();
    	//Make "Spring MVC" as default checked value
    	cust.setFavFramework(new String []{"Spring MVC"});

    	return cust;
    }

_Annotation_

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model){

    	Customer cust = new Customer();
    	//Make "Spring MVC" as default checked value
    	cust.setFavFramework(new String []{"Spring MVC"});

    	//command object
    	model.addAttribute("customer", cust);

    	//return form view
    	return "CustomerForm";
    }

## 3\. onSubmit() vs RequestMethod.POST

In SimpleFormController, the form submission is handle by the **onSubmit()** method. In annotation-based, you can do the same by annotated the method name with **@RequestMapping(method = RequestMethod.POST)**.

_SimpleFormController_

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
    	HttpServletResponse response, Object command, BindException errors)
    	throws Exception {

    	Customer customer = (Customer)command;
    	return new ModelAndView("CustomerSuccess");

    }

_Annotation_

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(
    	@ModelAttribute("customer") Customer customer,
    	BindingResult result, SessionStatus status) {

    	//clear the command object from the session
    	status.setComplete();

    	//return form success view
    	return "CustomerSuccess";

    }

## 4\. referenceData() vs @ModelAttribute

In SimpleFormController, usually you put the reference data in model via **referenceData()** method, so that the form view can access it. In annotation-based, you can do the same by annotated the method name with **@ModelAttribute**.

_SimpleFormController_

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

    	Map referenceData = new HashMap();

    	//Data referencing for web framework checkboxes
    	List<String> webFrameworkList = new ArrayList<String>();
    	webFrameworkList.add("Spring MVC");
    	webFrameworkList.add("Struts 1");
    	webFrameworkList.add("Struts 2");
    	webFrameworkList.add("JSF");
    	webFrameworkList.add("Apache Wicket");
    	referenceData.put("webFrameworkList", webFrameworkList);

    	return referenceData;
    }

_Spring’s form_

    <form:checkboxes items="${webFrameworkList}" path="favFramework" />

**Annotation**

    @ModelAttribute("webFrameworkList")
    public List<String> populateWebFrameworkList() {

    	//Data referencing for web framework checkboxes
    	List<String> webFrameworkList = new ArrayList<String>();
    	webFrameworkList.add("Spring MVC");
    	webFrameworkList.add("Struts 1");
    	webFrameworkList.add("Struts 2");
    	webFrameworkList.add("JSF");
    	webFrameworkList.add("Apache Wicket");

    	return webFrameworkList;
    }

_Spring’s form_

    <form:checkboxes items="${webFrameworkList}" path="favFramework" />

## 5\. initBinder() vs @InitBinder

In SimpleFormController, you define the binding or register the custom property editor via **initBinder()** method. In annotation-based, you can do the same by annotated the method name with **@InitBinder**.

_SimpleFormController_

    protected void initBinder(HttpServletRequest request,
    ServletRequestDataBinder binder) throws Exception {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        }

_Annotation_

    @InitBinder
    public void initBinder(WebDataBinder binder) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    	binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

## From Validation

In SimpleFormController, you have to register and map the validator class to the controller class via XML bean configuration file, and the validation checking and work flows will be executed automatically.

In annotation-based, you have to explicitly execute the validator and define the validation flow in the **@Controller** class manually. See the different :

_SimpleFormController_

    <bean class="com.mkyong.customer.controller.CustomerController">
                    <property name="formView" value="CustomerForm" />
    	<property name="successView" value="CustomerSuccess" />

    	<!-- Map a validator -->
    	<property name="validator">
    		<bean class="com.mkyong.customer.validator.CustomerValidator" />
    	</property>
    </bean>

_Annotation_

    @Controller
    @RequestMapping("/customer.htm")
    public class CustomerController{

    	CustomerValidator customerValidator;

    	@Autowired
    	public CustomerController(CustomerValidator customerValidator){
    		this.customerValidator = customerValidator;
    	}

    	@RequestMapping(method = RequestMethod.POST)
    	public String processSubmit(
    		@ModelAttribute("customer") Customer customer,
    		BindingResult result, SessionStatus status) {

    		customerValidator.validate(customer, result);

    		if (result.hasErrors()) {
    			//if validator failed
    			return "CustomerForm";
    		} else {
    			status.setComplete();
    			//form success
    			return "CustomerSuccess";
    		}
    	}
    	//...

## Full Example

See a complete @Controller example.

    package com.mkyong.customer.controller;

    import java.sql.Date;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.LinkedHashMap;
    import java.util.List;
    import java.util.Map;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.propertyeditors.CustomDateEditor;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.WebDataBinder;
    import org.springframework.web.bind.annotation.InitBinder;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.support.SessionStatus;

    import com.mkyong.customer.model.Customer;
    import com.mkyong.customer.validator.CustomerValidator;

    @Controller
    @RequestMapping("/customer.htm")
    public class CustomerController{

    	CustomerValidator customerValidator;

    	@Autowired
    	public CustomerController(CustomerValidator customerValidator){
    		this.customerValidator = customerValidator;
    	}

    	@RequestMapping(method = RequestMethod.POST)
    	public String processSubmit(
    		@ModelAttribute("customer") Customer customer,
    		BindingResult result, SessionStatus status) {

    		customerValidator.validate(customer, result);

    		if (result.hasErrors()) {
    			//if validator failed
    			return "CustomerForm";
    		} else {
    			status.setComplete();
    			//form success
    			return "CustomerSuccess";
    		}
    	}

    	@RequestMapping(method = RequestMethod.GET)
    	public String initForm(ModelMap model){

    		Customer cust = new Customer();
    		//Make "Spring MVC" as default checked value
    		cust.setFavFramework(new String []{"Spring MVC"});

    		//Make "Make" as default radio button selected value
    		cust.setSex("M");

    		//make "Hibernate" as the default java skills selection
    		cust.setJavaSkills("Hibernate");

    		//initilize a hidden value
    		cust.setSecretValue("I'm hidden value");

    		//command object
    		model.addAttribute("customer", cust);

    		//return form view
    		return "CustomerForm";
    	}

    	@ModelAttribute("webFrameworkList")
    	public List<String> populateWebFrameworkList() {

    		//Data referencing for web framework checkboxes
    		List<String> webFrameworkList = new ArrayList<String>();
    		webFrameworkList.add("Spring MVC");
    		webFrameworkList.add("Struts 1");
    		webFrameworkList.add("Struts 2");
    		webFrameworkList.add("JSF");
    		webFrameworkList.add("Apache Wicket");

    		return webFrameworkList;
    	}

    	@InitBinder
    	public void initBinder(WebDataBinder binder) {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

    	}

    	@ModelAttribute("numberList")
    	public List<String> populateNumberList() {

    		//Data referencing for number radiobuttons
    		List<String> numberList = new ArrayList<String>();
    		numberList.add("Number 1");
    		numberList.add("Number 2");
    		numberList.add("Number 3");
    		numberList.add("Number 4");
    		numberList.add("Number 5");

    		return numberList;
    	}

    	@ModelAttribute("javaSkillsList")
    	public Map<String,String> populateJavaSkillList() {

    		//Data referencing for java skills list box
    		Map<String,String> javaSkill = new LinkedHashMap<String,String>();
    		javaSkill.put("Hibernate", "Hibernate");
    		javaSkill.put("Spring", "Spring");
    		javaSkill.put("Apache Wicket", "Apache Wicket");
    		javaSkill.put("Struts", "Struts");

    		return javaSkill;
    	}

    	@ModelAttribute("countryList")
    	public Map<String,String> populateCountryList() {

    		//Data referencing for java skills list box
    		Map<String,String> country = new LinkedHashMap<String,String>();
    		country.put("US", "United Stated");
    		country.put("CHINA", "China");
    		country.put("SG", "Singapore");
    		country.put("MY", "Malaysia");

    		return country;
    	}
    }

To make annotation work, you have to enable the component auto scanning feature in Spring.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">

    	<context:component-scan base-package="com.mkyong.customer.controller" />

    	<bean class="com.mkyong.customer.validator.CustomerValidator" />

     	<!-- Register the Customer.properties -->
    	<bean id="messageSource"
    		class="org.springframework.context.support.ResourceBundleMessageSource">
    		<property name="basename" value="com/mkyong/customer/properties/Customer" />
    	</bean>

    	<bean id="viewResolver"
    	      class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
                  <property name="prefix">
                     <value>/WEB-INF/pages/</value>
                  </property>
                  <property name="suffix">
                     <value>.jsp</value>
                  </property>
            </bean>
    </beans>

[http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-annotation-example/](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-annotation-example/)
