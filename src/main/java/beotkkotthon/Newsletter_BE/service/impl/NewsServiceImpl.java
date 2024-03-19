package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;


    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, NewsSaveRequestDto newsSaveRequestDto) {
        System.out.println(teamId);
        Team team = teamService.findById(teamId);
        News news = newsSaveRequestDto.toEntity(team);
        newsRepository.save(news);

        return new NewsResponseDto(news);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsResponseDto> findNewssByTeam(Long teamId) {

        Team team = teamService.findById(teamId);
        List<NewsResponseDto> newsResponseDtos = team.getNewsList().stream().map(NewsResponseDto::new)
                .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))  // 수정시각 기준 내림차순 정렬
                .collect(Collectors.toList());

        return newsResponseDtos;
    }
}
