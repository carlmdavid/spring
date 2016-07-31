package org.bytecodeandcode.spring.batch.quartz.batch;

import java.util.List;

import org.bytecodeandcode.spring.batch.quartz.domain.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class LogWriter implements ItemWriter<List<Record>> {
	
	private static final Logger logger = LoggerFactory.getLogger(LogWriter.class);

	@Override
	public void write(List<? extends List<Record>> items) throws Exception {
		for (List<Record> list : items) {
			for (Record record : list) {
				logger.info(record.toString());				
			}
		}
	}


}
