package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;

import java.util.List;
import java.util.stream.Collectors;

public class NewsConverter {

    public static NewsResponseDto.ShowNewsDto toShowNewsDto(News news) {
        return ShowNewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .writer(news.getMember().getUsername())
                .content(news.getContent())
                .teamId(news.getTeam().getId())
                .teamName(news.getTeam().getName())
                .imageUrl1(news.getImageUrl1())
                .imageUrl2(news.getImageUrl2())
                .limitTime(news.getLimitTime())
                .checkStatus(CheckStatus.NOT_READ)
                .readMemberCount(0)
                .notReadMemberCount(news.getTeam().getTeamSize())
                .modifiedTime(news.getModifiedTime())
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
