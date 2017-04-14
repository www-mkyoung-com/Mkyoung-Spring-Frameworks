package com.mkyong.customer.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mkyong.customer.bo.CustomerBo;
import com.mkyong.customer.model.Customer;
import com.opensymphony.xwork2.ModelDriven;
 
public class CustomerAction implements ModelDriven{

	Customer customer = new Customer();
	List<Customer> customerList = new ArrayList<Customer>();
	
	CustomerBo customerBo;
	//DI via Spring
	public void setCustomerBo(CustomerBo customerBo) {
		this.customerBo = customerBo;
	}

	public Object getModel() {
		return customer;
	}
	
	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	//save customer
	public String addCustomer() throws Exception{
		
		//save it
		customer.setCreatedDate(new Date());
		customerBo.addCustomer(customer);
	 
		//reload the customer list
		customerList = null;
		customerList = customerBo.listCustomer();
		
		return "success";
	
	}
	
	//list all customers
	public String listCustomer() throws Exception{
		
		customerList = customerBo.listCustomer();
		
		return "success";
	
	}
	
}