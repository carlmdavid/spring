package org.bytecodeandcode.spring.batch.quartz;

import java.io.IOException;

import org.bytecodeandcode.spring.batch.quartz.domain.Record;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

 	@Autowired
 	private JobBuilderFactory jobs;
 	
 	@Autowired
 	private StepBuilderFactory steps;

 	@Bean
 	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
 		JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
 		processor.setJobRegistry(jobRegistry);
 		
 		return processor;
 	}
 	
	
	@Bean
	public ItemReader<Record> reader(DefaultLineMapper<Record> recordLineMapper) throws IOException{
		FlatFileItemReader<Record> flatFileItemReader = new FlatFileItemReader<Record>();
		flatFileItemReader.setLineMapper(recordLineMapper);
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setResource(new ClassPathResource("csv/input/record.csv"));
		return flatFileItemReader;
	}
	
	@Bean
	public ItemWriter<Record> writer() throws IOException{
		FlatFileItemWriter<Record> flatFileItemWriter = new FlatFileItemWriter<Record>();
		flatFileItemWriter.setAppendAllowed(true);
		flatFileItemWriter.setLineAggregator(new PassThroughLineAggregator<Record>());
		flatFileItemWriter.setResource(new FileSystemResource(new ClassPathResource(".").getFile().getAbsolutePath() + "/csv/output/output.txt"));
		
		return flatFileItemWriter;
	}
	
	@Bean
	public BeanWrapperFieldSetMapper<Record> beanWrapperFieldSetMapper(){
		BeanWrapperFieldSetMapper<Record> fieldSetMapper = new BeanWrapperFieldSetMapper<Record>();
		fieldSetMapper.setPrototypeBeanName("record");
		return fieldSetMapper;
	}
	
	@Bean
	public DelimitedLineTokenizer defaultLineTokenizer() {
		DelimitedLineTokenizer defaultLineTokenizer = new DelimitedLineTokenizer(",");
		defaultLineTokenizer.setNames(new String[] {"id", "name"});		
		
		return defaultLineTokenizer;
	}

	@Bean
	public static DefaultLineMapper<Record> recordLineMapper(BeanWrapperFieldSetMapper<Record> beanWrapperFieldSetMapper
			, DelimitedLineTokenizer defaultLineTokenizer) {
		DefaultLineMapper<Record> defaultLineMapper = new DefaultLineMapper<Record>();
		defaultLineMapper.setLineTokenizer(defaultLineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		return defaultLineMapper;
	}
	

 	@Bean
 	public Job job(Step step1) {
 		return jobs.get("myJob").start(step1).build();
 	}

 	@Bean
    protected Step step1(ItemReader<Record> reader, ItemWriter<Record> writer) {
 		return steps.get("step1").<Record, Record>chunk(10).faultTolerant().reader(reader).writer(writer).build();
    }

}
