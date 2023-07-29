package batch.spring.metanet.component.repository;

import batch.spring.metanet.component.domain.meeting_image.MeetingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingImageRepository extends JpaRepository<MeetingImage, Long> {
}