package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;

import java.util.List;
import java.util.stream.Collectors;

public class NewsConverter {

    public static NewsResponseDto.ShowNewsDto toShowNewsDto(News news) {
        int readMemberCount = countReadMember(news);
        int notReadMemberCount = news.getTeam().getTeamSize();

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
                .readMemberCount(readMemberCount)
                .notReadMemberCount(notReadMemberCount)
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

    public static int countReadMember(News news) {
        List<NewsCheck> newsChecks = news.getNewsCheckList();
        return (int) newsChecks.stream()
                .filter(newsCheck -> newsCheck.getCheckStatus() == CheckStatus.READ)
                .count();
    }
}
