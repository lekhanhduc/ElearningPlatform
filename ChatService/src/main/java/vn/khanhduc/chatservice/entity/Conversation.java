package vn.khanhduc.chatservice.entity;

import vn.khanhduc.chatservice.enums.ConversationType;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// Quản lý thông tin về một cuộc trò chuyện (chat 1-1 hoặc chat nhóm).
// Nhận biết được tin nhắn thuộc cuộc trò chuyện nào

@Entity
@Table(name = "conversation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Conversation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "conversation_type", nullable = false)
    private ConversationType conversationType;

    @Column(nullable = false)
    private Long senderId; // Người khởi tạo cuộc trò chuyện

    @Column(nullable = false)
    private Long receiverId;

    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Long lastMessageId;
}
