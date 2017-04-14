package com.mkyong.user;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SimplePage extends WebPage {

	@SpringBean
	private HelloService helloService;
	
	public SimplePage(final PageParameters parameters) {

		add(new Label("msg", helloService.getHelloWorldMsg()));

	}

}
