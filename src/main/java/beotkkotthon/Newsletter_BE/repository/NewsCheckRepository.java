package beotkkotthon.Newsletter_BE.repository;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsCheckRepository extends JpaRepository<NewsCheck, Long> {

    Optional<NewsCheck> findByMemberAndNews(Member member, News news);
    List<NewsCheck> findByMember(Member member);

    @EntityGraph(attributePaths = "news")
    List<NewsCheck> findByNews(News news);
}
