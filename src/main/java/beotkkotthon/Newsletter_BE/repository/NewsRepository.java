package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
