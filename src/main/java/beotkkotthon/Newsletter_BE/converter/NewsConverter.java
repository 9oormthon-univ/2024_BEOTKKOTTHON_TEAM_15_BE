package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;

import java.util.List;
import java.util.stream.Collectors;

public class NewsConverter {

    public static NewsResponseDto.ShowNewsDto toShowNewsDto(News news) {
        return ShowNewsDto.builder()
                .newsId(news.getId())
                .title(news.getTitle())
                .writer(news.getMember().getUsername())
                .content(news.getContent())
                .imageUrl1(news.getImageUrl1())
                .imageUrl2(news.getImageUrl2())
                .limitTime(news.getLimitTime())
                .build();
    }

    public static NewsResponseDto.ShowNewsListDto toShowNewsDtoList(List<News> newsList) {
        List<NewsResponseDto.ShowNewsDto> showNewsDtoList = newsList.stream()
                .map(NewsConverter::toShowNewsDto)
                .collect(Collectors.toList());

        return NewsResponseDto.ShowNewsListDto.builder()
                .showNewsDtoList(showNewsDtoList)
                .build();
    }
}
