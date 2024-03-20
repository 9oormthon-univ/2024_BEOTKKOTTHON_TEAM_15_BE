package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    Optional<MemberTeam> findByMemberAndTeam(Member member, Team team);
    boolean existByMemberAndTeam(Member member, Team team);
}
