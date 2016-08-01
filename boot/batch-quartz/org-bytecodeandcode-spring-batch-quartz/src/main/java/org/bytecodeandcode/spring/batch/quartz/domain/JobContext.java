package org.bytecodeandcode.spring.batch.quartz.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Component
public class JobContext {

	private Map<JobKey, Date> lastRunDateTimeMap = new HashMap<>();

	public void setLastRunDateTime(JobKey key, Date previousFireTime) {
		lastRunDateTimeMap.put(key, previousFireTime);
	}
	
	public Date getLastRunDateTime(JobKey key) {
		return lastRunDateTimeMap.get(key);
	}
	
	
}
