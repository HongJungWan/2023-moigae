package batch.spring.metanet.component.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzService {
    public static final String JOB_NAME = "jpaBatchItemWriterJob";
    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
            scheduler.getListenerManager().addJobListener(new QuartzJobListener());
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

            addJob(QuartzBatchJob.class, "jpaBatchItemWriterJob", "jpaBatchItemWriterJob 실행", null, "0 0 2 * * ?");
        } catch (Exception e) {
            log.error("addJob error  : {}", e);
        }
    }

    //Job 추가
    public <T extends Job> void addJob(Class<? extends Job> job, String name, String docs, Map paramsMap, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name, docs, paramsMap);
        Trigger trigger = buildCronTrigger(cron);
        if (scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //JobDetail 생성
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String name, String desc, Map paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_NAME, name);
        jobDataMap.put("executeCount", 1);

        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .usingJobData(jobDataMap)
                .build();
    }

    //Trigger 생성
    private Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }
}