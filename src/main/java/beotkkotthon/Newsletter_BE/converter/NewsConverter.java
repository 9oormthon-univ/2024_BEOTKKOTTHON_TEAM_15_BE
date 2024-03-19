package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;

public class NewsConverter {

    public static NewsResponseDto.ShowNewsDto toShowNewsDto(News news) {
        return ShowNewsDto.builder()
                .title(news.getTitle())
                .content(news.getContent())
                .imageUrl1(news.getImageUrl1())
                .imageUrl2(news.getImageUrl2())
                .limitTime(news.getLimitTime())
                .build();
    }
}
