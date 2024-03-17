package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCheckRepository extends JpaRepository<NewsCheck, Long> {
}
