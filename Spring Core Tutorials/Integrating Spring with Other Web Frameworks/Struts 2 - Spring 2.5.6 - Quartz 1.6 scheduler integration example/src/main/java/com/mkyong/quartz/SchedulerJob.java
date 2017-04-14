package com.mkyong.quartz;
 
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
 
public class SchedulerJob extends QuartzJobBean
{
	private SchedulerTask schedulerTask;
 
	public void setSchedulerTask(SchedulerTask schedulerTask) {
		this.schedulerTask = schedulerTask;
	}
 
	protected void executeInternal(JobExecutionContext context)
	throws JobExecutionException {
 
		schedulerTask.printSchedulerMessage();
 
	}
}