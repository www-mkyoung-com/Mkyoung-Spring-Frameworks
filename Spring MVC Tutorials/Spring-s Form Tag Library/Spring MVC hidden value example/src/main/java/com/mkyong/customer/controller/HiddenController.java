package com.mkyong.customer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.mkyong.customer.model.Customer;

public class HiddenController extends SimpleFormController{
	
	public HiddenController(){
		setCommandClass(Customer.class);
		setCommandName("customerForm");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		Customer cust = new Customer();
		cust.setSecretValue("I'm hidden value, hehe");
		return cust;
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		Customer customer = (Customer)command;
		return new ModelAndView("CustomerSuccess","customer",customer);
	
	}
	
}