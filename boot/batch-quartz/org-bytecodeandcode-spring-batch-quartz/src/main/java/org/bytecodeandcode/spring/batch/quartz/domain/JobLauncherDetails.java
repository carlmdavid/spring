package org.bytecodeandcode.spring.batch.quartz.domain;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobLauncherDetails extends QuartzJobBean {
	
	@Autowired
	private JobContext jobContext;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRegistry jobRegistry;

	public static final String JOB_NAME = "JOB_NAME";

	@Override
	protected void executeInternal(JobExecutionContext context) {

		Map<String, Object> jobDataMap = context.getMergedJobDataMap();

		String jobName = (String) jobDataMap.get(JOB_NAME);

		JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap, context);

		try {
			jobLauncher.run(jobRegistry.getJob(jobName), jobParameters);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}

	// get params from jobDataAsMap property, job-quartz.xml
	private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap, JobExecutionContext context) {

		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String && !key.equals(JOB_NAME)) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			} else {
				// JobDataMap contains values which are not job parameters
				// (ignoring)
			}
		}

		// need unique job parameter to rerun the same job
		builder.addDate("run date", new Date());
		
		// Add the last run time
		Date lastRunDateTime = jobContext.getLastRunDateTime(context.getJobDetail().getKey());
		builder.addDate("last_run_time", lastRunDateTime);
		jobDataMap.put("last_run_time", lastRunDateTime);

		return builder.toJobParameters();

	}

}
