package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByMember(Member member);
    boolean existsByMember(Member member);
}
