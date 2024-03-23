package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto.NewsCheckDto;

import java.time.LocalDateTime;

public class NewsCheckConverter {

    public static NewsCheckResponseDto.NewsCheckDto toNewsCheckDto(NewsCheck newsCheck) {
        return NewsCheckDto.builder()
                .newsId(newsCheck.getNews().getId())
                .checkStatus(newsCheck.getCheckStatus())
                .checkTime(LocalDateTime.now())
                .username(newsCheck.getMember().getUsername())
                .build();
    }
}
