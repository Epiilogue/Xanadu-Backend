package edu.neu.sub.config;

import edu.neu.sub.Job.DailyReportJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(DailyReportJob.class)
                .withIdentity("daily_report", "daily_report")
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger trigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("daily_report", "daily_report")
                .startNow()
                // 每天八点自动生成当日的结算报表
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 20 * * ? *"))
                .build();
        return trigger;
    }
}