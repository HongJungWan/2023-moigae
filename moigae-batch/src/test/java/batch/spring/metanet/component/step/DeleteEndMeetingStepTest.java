package batch.spring.metanet.component.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBatchTest
class DeleteEndMeetingStepTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    public void clearMetadata() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("모임 삭제 일괄 처리 테스트")
    public void 모임_삭제_일괄_처리_테스트() throws Exception {
        //given, 더미 JobParameters 생성
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2023-06-20")
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("updateEndMeetingStepOne", jobParameters);

        //then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}