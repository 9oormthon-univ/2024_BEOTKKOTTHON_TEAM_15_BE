package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;

import java.util.List;

public interface TeamService {

    List<NewsResponseDto> findNewssByTeam(Long teamId);
}
