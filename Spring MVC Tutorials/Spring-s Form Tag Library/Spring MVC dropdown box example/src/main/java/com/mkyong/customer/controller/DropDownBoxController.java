package com.mkyong.customer.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.mkyong.customer.model.Customer;

public class DropDownBoxController extends SimpleFormController{
	
	public DropDownBoxController(){
		setCommandClass(Customer.class);
		setCommandName("customerForm");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		Customer cust = new Customer();
		
		//make "Spring" as the default java skills selection
		cust.setJavaSkills("Spring");
		
		return cust;
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		Customer customer = (Customer)command;
		return new ModelAndView("CustomerSuccess","customer",customer);
	
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		
		Map referenceData = new HashMap();
		
		Map<String,String> country = new LinkedHashMap<String,String>();
		country.put("US", "United Stated");
		country.put("CHINA", "China");
		country.put("SG", "Singapore");
		country.put("MY", "Malaysia");
		referenceData.put("countryList", country);
		
		Map<String,String> javaSkill = new LinkedHashMap<String,String>();
		javaSkill.put("Hibernate", "Hibernate");
		javaSkill.put("Spring", "Spring");
		javaSkill.put("Apache Wicket", "Apache Wicket");
		javaSkill.put("Struts", "Struts");
		referenceData.put("javaSkillsList", javaSkill);
		
		return referenceData;
		
	}
	
}