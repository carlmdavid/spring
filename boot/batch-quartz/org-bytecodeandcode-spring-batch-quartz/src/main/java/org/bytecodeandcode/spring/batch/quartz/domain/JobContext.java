package org.bytecodeandcode.spring.batch.quartz.domain;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class JobContext {

	private Date lastRunDateTime;
}
