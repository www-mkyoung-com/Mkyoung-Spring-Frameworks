package com.mkyong.common.controller;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomerController{
 
	@RequestMapping("/customer/add.htm")
	public ModelAndView add(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		return new ModelAndView("CustomerPage", "msg","add() method");
 
	}
 
	@RequestMapping("/customer/delete.htm")
	public ModelAndView delete(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		return new ModelAndView("CustomerPage", "msg","delete() method");
 
	}
 
	@RequestMapping("/customer/update.htm")
	public ModelAndView update(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		return new ModelAndView("CustomerPage", "msg","update() method");
 
	}
 
	@RequestMapping("/customer/list.htm")
	public ModelAndView list(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		return new ModelAndView("CustomerPage", "msg","list() method");
 
	}
 
}