package vn.khanhduc.chatservice.entity;

import vn.khanhduc.chatservice.enums.ParticipantRole;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// Lưu tất cả users tham gia cuộc trò chuyện
// Quản lý mối quan hệ giữa người dùng và cuộc trò chuyện, tức là ai tham gia cuộc trò chuyện nào.
// Xác định ai là ADMIN, ai là MEMBER, và tham và từ lúc nào

@Entity
@Table(name = "conversation_participant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConversationParticipant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long conversationId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    private ParticipantRole role;
}
