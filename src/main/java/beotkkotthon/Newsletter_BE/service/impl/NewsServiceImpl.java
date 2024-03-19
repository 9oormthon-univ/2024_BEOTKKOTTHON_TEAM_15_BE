package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;


    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, NewsSaveRequestDto newsSaveRequestDto) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
        News news = newsSaveRequestDto.toEntity(team);
        newsRepository.save(news);

        return new NewsResponseDto(news);
    }
}
