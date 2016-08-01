package org.bytecodeandcode.spring.batch.quartz.listener;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class TriggerListener extends TriggerListenerSupport {

	@Override
	public String getName() {
		return "TriggerListener";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
//		getLog().info(DateFormatUtils.format(trigger.getPreviousFireTime(), "yyyy-MM-dd hh:mm:ss.SSS a"));
	}

}
