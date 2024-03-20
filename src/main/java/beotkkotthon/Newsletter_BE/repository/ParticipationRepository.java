package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.domain.mapping.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByMemberAndTeam(Member member, Team team);
    List<Participation> findAllByTeam(Team team);
    boolean existsByMemberAndTeam(Member member, Team team);
}
