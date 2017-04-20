package com.mkyong.listeners;

import org.springframework.batch.core.ItemReadListener;

import com.mkyong.Domain;

public class CustomItemReaderListener implements ItemReadListener<Domain> {

	@Override
	public void beforeRead() {
		System.out.println("ItemReadListener - beforeRead");
	}

	@Override
	public void afterRead(Domain item) {
		System.out.println("ItemReadListener - afterRead");
	}

	@Override
	public void onReadError(Exception ex) {
		System.out.println("ItemReadListener - onReadError");
	}

}
