package batch.spring.metanet.component.repository;

import batch.spring.metanet.component.domain.meeting.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {
    @Query("SELECT m FROM Meeting m WHERE m.meetingStatus = 'END' AND m.meetingEndDateTime < :threeMonthsAgo")
    Page<Meeting> findEndDateTimeMeetings(@Param("threeMonthsAgo") LocalDateTime threeMonthsAgo, Pageable pageable);

    @Query("SELECT m FROM Meeting m WHERE m.meetingStatus != 'END' AND m.meetingEndDateTime < :oneDaysAgo")
    Page<Meeting> findMeetingStatusUpdateMeetings(@Param("oneDaysAgo") LocalDateTime oneDaysAgo, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Meeting m SET m.meetingStatus = 'END' WHERE m.meetingStatus != 'END' AND m.meetingEndDateTime < :currentTime")
    void updateMeetingStatusToEND(@Param("currentTime") LocalDateTime currentTime);
}