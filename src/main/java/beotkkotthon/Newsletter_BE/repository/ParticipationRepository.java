package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.mapping.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
