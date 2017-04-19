package com.mkyong;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CustomWriter<T> implements ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {

		for (int i = 0; items.size() > i; i++) {
			Domain obj = (Domain) items.get(i);
			System.out.println(obj.getId() + ":" + obj.getDomain());
		}
		
	}
}