package com.mkyong;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import com.mkyong.user.SimplePage;

public class WicketApplication extends WebApplication {

	@Override
	public Class<SimplePage> getHomePage() {

		return SimplePage.class; // return default page
	}
	
	@Override
	protected void init() {
		
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
		 
	}

}
