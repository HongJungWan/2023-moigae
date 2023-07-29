package batch.spring.metanet.component.repository;

import batch.spring.metanet.component.domain.meeting_payment.MeetingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MeetingPaymentRepository extends JpaRepository<MeetingPayment, Long> {
    @Query("SELECT m.paidAmount * COUNT(m.user.id) FROM MeetingPayment m WHERE m.meeting.id = :meetingId")
    Long calculateAmount(@Param("meetingId") String meetingId);

    @Transactional
    @Modifying
    @Query("UPDATE MeetingPayment m SET m.calculateAmount = :calculateAmount WHERE m.meeting.id = :id")
    void updateCalculateAmount(@Param("id") String id, @Param("calculateAmount") Long calculateAmount);
}