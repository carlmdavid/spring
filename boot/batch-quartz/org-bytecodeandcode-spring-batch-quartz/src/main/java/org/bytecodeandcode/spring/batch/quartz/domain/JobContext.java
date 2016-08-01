package org.bytecodeandcode.spring.batch.quartz.domain;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@JobScope
public class JobContext {

	private Date lastRunDateTime;
}
