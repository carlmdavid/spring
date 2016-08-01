package org.bytecodeandcode.spring.batch.quartz.listener;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.Trigger;
import org.quartz.listeners.SchedulerListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class SchedulerListener extends SchedulerListenerSupport {

	@Override
	public void jobScheduled(Trigger trigger) {
		// TODO Auto-generated method stub
		super.jobScheduled(trigger);
//		getLog().info(DateFormatUtils.format(trigger.getPreviousFireTime(), "yyyy-MM-dd hh:mm:ss.SSS a"));
	}

	
}
