package org.bytecodeandcode.spring.batch.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.listeners.SchedulerListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class ReportJobListener extends JobListenerSupport {

	@Override
	public String getName() {
		return "ReportJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		context.getPreviousFireTime();
	}
	
	


}
