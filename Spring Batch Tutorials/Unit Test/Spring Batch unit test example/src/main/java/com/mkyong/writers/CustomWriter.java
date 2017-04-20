package com.mkyong.writers;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.mkyong.model.Report;

public class CustomWriter<T> implements ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {
		
		System.out.println("writer..." + items.size());
		Report report = (Report)items.get(0);
		System.out.println(report);
		
	}

}