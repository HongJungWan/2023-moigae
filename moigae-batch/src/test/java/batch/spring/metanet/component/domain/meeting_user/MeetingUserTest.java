package batch.spring.metanet.component.domain.meeting_user;

import batch.spring.metanet.component.domain.meeting.Meeting;
import batch.spring.metanet.component.domain.meeting.MeetingAddress;
import batch.spring.metanet.component.domain.meeting.MeetingContact;
import batch.spring.metanet.component.domain.meeting.ParticipantRange;
import batch.spring.metanet.component.domain.meeting.enumeraion.*;
import batch.spring.metanet.component.domain.meeting_image.MeetingImage;
import batch.spring.metanet.component.domain.user.User;
import batch.spring.metanet.component.domain.user.enumeration.Gender;
import batch.spring.metanet.component.domain.user.enumeration.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeetingUserTest {
    private static final String PHONE = "010";
    private static final String EMAIL = "email";
    private ParticipantRange participantRange;
    private MeetingAddress meetingAddress;
    private MeetingContact meetingContact;
    private MeetingImage meetingImage;
    private MeetingType meetingType = MeetingType.ONLINE;
    private MeetingCategory meetingCategory = MeetingCategory.PARTY;
    private MeetingPrice meetingPrice = MeetingPrice.PAY;
    private PetAllowedStatus petAllowedStatus = PetAllowedStatus.WITH_OUT;
    private MeetingStatus meetingStatus = MeetingStatus.AVAILABLE;
    private User user;
    private Meeting meeting;

    @BeforeEach
    void setUp() {
        user = createUser();
        meeting = createMeeting();
    }

    @Test
    @DisplayName("모임 유저 객체 생성 테스트")
    void 모임_유저_객체_생성_테스트() {
        //given & when
        MeetingUser meetingUser = MeetingUser.of(1L, user, meeting, "hostId");
        //then
        모임_유저_객체_검증(meetingUser);
    }

    private User createUser() {
        return User.builder()
                .userName("홍정완")
                .password("패스워드")
                .gender(Gender.MAN)
                .phone(PHONE)
                .account("계좌")
                .hostIntroduction("호스트 자기소개")
                .email(EMAIL)
                .userRole(UserRole.USER)
                .flag(true)
                .deactivateAt(null)
                .build();
    }

    private Meeting createMeeting() {
        return Meeting.builder()
                .meetingTitle("타이틀")
                .meetingType(meetingType)
                .meetingCategory(meetingCategory)
                .nickName("닉네임")
                .meetingImage(meetingImage)
                .meetingDescription("모임 소개")
                .participantRange(participantRange)
                .meetingAddress(meetingAddress)
                .meetingPrice(meetingPrice)
                .petAllowedStatus(petAllowedStatus)
                .meetingContact(meetingContact)
                .meetingFreeFormDetails("모임 정보 자유 작성")
                .meetingStatus(meetingStatus)
                .build();
    }

    private static void 모임_유저_객체_검증(MeetingUser meetingUser) {
        assertThat(meetingUser.getId()).isEqualTo(1L);
        assertThat(meetingUser.getMeeting().getMeetingTitle()).isEqualTo("타이틀");
        assertThat(meetingUser.getUser().getUserName()).isEqualTo("홍정완");
        assertThat(meetingUser.getHostId()).isEqualTo("hostId");
    }
}