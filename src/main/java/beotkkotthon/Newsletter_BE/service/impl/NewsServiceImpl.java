package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.ImageUploadService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ImageUploadService imageUploadService;
    private final TeamService teamService;


    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, MultipartFile image1, MultipartFile image2, NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        Team team = teamService.findById(teamId);
        String imageUrl1 = imageUploadService.uploadImage(image1);
        String imageUrl2 = imageUploadService.uploadImage(image2);
        News news = newsSaveRequestDto.toEntity(team, imageUrl1, imageUrl2);
        newsRepository.save(news);

        return new NewsResponseDto(news);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsResponseDto> findNewssByTeam(Long teamId) {

        Team team = teamService.findById(teamId);
        List<NewsResponseDto> newsResponseDtos = team.getNewsList().stream().map(NewsResponseDto::new)
                .sorted(Comparator.comparing(NewsResponseDto::getId, Comparator.reverseOrder()))  // id 내림차순 정렬 후 (최신 생성순)
                .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))  // 수정날짜 내림차순 정렬 (최신 수정순)
                .collect(Collectors.toList());  // 정렬 완료한 리스트 반환 (연속sorted 정렬은 마지막 순서의 기준이 가장 주요된 기준임.)

        return newsResponseDtos;
    }
}
