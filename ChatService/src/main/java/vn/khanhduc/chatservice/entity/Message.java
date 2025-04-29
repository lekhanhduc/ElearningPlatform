package vn.khanhduc.chatservice.entity;

import vn.khanhduc.chatservice.enums.MessageStatus;
import vn.khanhduc.chatservice.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// Lưu trữ thông tin về từng tin nhắn trong hệ thống.
// Xem tin nhắn đã gửi

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long conversationId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status")
    private MessageStatus messageStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;
}
