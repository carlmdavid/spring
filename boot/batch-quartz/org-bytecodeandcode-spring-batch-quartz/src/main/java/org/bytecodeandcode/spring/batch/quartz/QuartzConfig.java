package org.bytecodeandcode.spring.batch.quartz;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.bytecodeandcode.spring.batch.quartz.domain.JobLauncherDetails;
import org.bytecodeandcode.spring.batch.quartz.spring.AutowiringSpringBeanJobFactory;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SchedulerListener;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory
//			, Trigger jobTrigger
			,
			List<JobListener> jobListeners) throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		// this allows to update triggers in DB when updating settings in config
		// file:
		factory.setOverwriteExistingJobs(true);
		factory.setDataSource(dataSource);
		factory.setJobFactory(jobFactory);

		factory.setQuartzProperties(quartzProperties());
//		for (JobListener jobListener : jobListeners) {
//			factory.setGlobalJobListeners(jobListener);
//		}
//		factory.setTriggers(jobTrigger);
//		factory.setSchedulerName("report-csv-txt");

		return factory;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	// @Bean
	public JobDetailFactoryBean jobDetail(JobRegistry jobRegistry, JobLauncher jobLauncher,
			ApplicationContext applicationContext) {
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
		jobDetailFactoryBean.setGroup("quartz-batch");
		jobDetailFactoryBean.setJobClass(JobLauncherDetails.class);
		// job has to be durable to be stored in DB:
		jobDetailFactoryBean.setDurability(true);

		Map<String, Object> jobDataAsMap = new HashMap<>();
		jobDataAsMap.put(JobLauncherDetails.JOB_NAME, "myJob");
		jobDataAsMap.put("mode", "daily");
		/*
		 * jobDataAsMap.put("jobLocator", jobRegistry);
		 * jobDataAsMap.put("jobLauncher", jobLauncher);
		 */

		jobDetailFactoryBean.setJobDataAsMap(jobDataAsMap);

		return jobDetailFactoryBean;
	}

	// @Bean
	public CronTriggerFactoryBean jobTrigger(JobDetail jobDetail) {
		return createCronTrigger(jobDetail, "*/30 * * * * ?");
	}

	// Use this method for creating cron triggers instead of simple triggers:
	private static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
		CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
		factoryBean.setJobDetail(jobDetail);
		factoryBean.setCronExpression(cronExpression);
		factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		return factoryBean;
	}

}
