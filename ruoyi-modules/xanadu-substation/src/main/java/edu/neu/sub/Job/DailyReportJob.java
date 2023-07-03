package edu.neu.sub.Job;

import edu.neu.sub.service.SubstationService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class DailyReportJob extends QuartzJobBean {
    private final SubstationService studentService;

    @Autowired
    public DailyReportJob(SubstationService studentService) {
        this.studentService = studentService;
    }

    @Override
    @SuppressWarnings("all")
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
       studentService.generateSubstationStatistics();
    }
}