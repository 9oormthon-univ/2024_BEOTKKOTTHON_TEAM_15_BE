package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
}
