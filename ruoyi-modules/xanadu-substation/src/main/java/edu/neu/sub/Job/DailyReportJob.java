package edu.neu.sub.Job;

import edu.neu.sub.service.SubstationService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class DailyReportJob extends QuartzJobBean {
    private final SubstationService substationService;

    @Autowired
    public DailyReportJob(SubstationService substationService) {
        this.substationService = substationService;
    }

    @Override
    @SuppressWarnings("all")
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        substationService.generateSubstationStatistics();
    }
}