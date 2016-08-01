package org.bytecodeandcode.spring.batch.quartz.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bytecodeandcode.spring.batch.quartz.domain.Record;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Service
@JobScope
@Slf4j
public class BusinessService   {
	
	
	private boolean fetched = false;
	
	public List<Record> serviceMethod(String mode, Date lastRunDateTime) {
		
		if(fetched)
			return null;
		
		fetched = true;
				
		Record record = new Record();
		record.setId(1);
		
		if (StringUtils.equalsIgnoreCase(mode, "daily")) {
			record.setName("daily");
			log.info("Getting the changes since {}", DateFormatUtils.format(lastRunDateTime, "yyyy-MM-dd hh:mm:ss.SSS a"));
		}
		
		else if (StringUtils.equalsIgnoreCase(mode, "monthly")) {
			record.setName("monthly");
		}
		
		record.setName(record.getName() + "_" + DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()));

		return Lists.newArrayList(record); 
	}
	
}
