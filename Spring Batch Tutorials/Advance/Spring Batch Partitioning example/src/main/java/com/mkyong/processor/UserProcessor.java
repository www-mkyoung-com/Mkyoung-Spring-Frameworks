package com.mkyong.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mkyong.User;

@Component("itemProcessor")
@Scope(value = "step")
public class UserProcessor implements ItemProcessor<User, User> {

	@Value("#{stepExecutionContext[name]}")
	private String threadName;

	@Override
	public User process(User item) throws Exception {

		System.out.println(threadName + " processing : " + item.getId() + " : " + item.getUsername());

		return item;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}
