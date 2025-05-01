package vn.khanhduc.chatservice.repository;

import vn.khanhduc.chatservice.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    List<ConversationParticipant> findByConversationId(Long id);
    boolean existsByConversationIdAndUserId(Long conversationId, String userId);
}
