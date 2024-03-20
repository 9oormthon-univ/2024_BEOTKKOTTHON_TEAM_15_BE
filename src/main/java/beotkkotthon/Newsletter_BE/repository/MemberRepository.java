package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
