package com.mkyong.customer.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mkyong.customer.model.Customer;

public class RadioButtonValidator implements Validator{

	@Override
	public boolean supports(Class clazz) {
		//just validate the Customer instances
		return Customer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Customer cust = (Customer)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "required.sex");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "favNumber", "required.favNumber");
		
	}
	
}