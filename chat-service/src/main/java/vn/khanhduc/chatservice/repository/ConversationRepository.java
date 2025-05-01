package vn.khanhduc.chatservice.repository;

import vn.khanhduc.chatservice.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE c.conversationType = 'ONE_TO_ONE' AND " +
            "((c.senderId = :senderId AND c.receiverId = :receiverId) OR " +
            "(c.senderId = :receiverId AND c.receiverId = :senderId))")
    Optional<Conversation> findBySenderIdAndReceiverId(@Param("senderId") Long senderId,
                                                       @Param("receiverId") Long receiverId);

}
