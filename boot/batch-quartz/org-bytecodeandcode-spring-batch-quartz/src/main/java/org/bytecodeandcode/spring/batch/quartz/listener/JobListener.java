package org.bytecodeandcode.spring.batch.quartz.listener;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bytecodeandcode.spring.batch.quartz.domain.JobContext;
import org.quartz.JobExecutionContext;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobListenerSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(JobListener.class);

	@Autowired
	private JobContext jobContext;
	
	@Override
	public String getName() {
		return "JobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		if (StringUtils.startsWith(context.getJobDetail().getKey().getName(), "JobSchedulingDataLoaderPlugin"))
				return;
		
		Date previousFireTime = context.getPreviousFireTime();
		if (previousFireTime == null)
			previousFireTime = context.getTrigger().getPreviousFireTime();
		
		logger.info(DateFormatUtils.format(previousFireTime, "yyyy-MM-dd hh:mm:ss.SSS a"));
		jobContext.setLastRunDateTime(context.getJobDetail().getKey(), previousFireTime);
	}
}
