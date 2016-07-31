package org.bytecodeandcode.spring.batch.quartz.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Scope("prototype")
public class Record {

	private int id;
	private String name;
}
