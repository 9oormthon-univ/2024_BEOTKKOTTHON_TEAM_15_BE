package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;

import java.util.List;

public interface TeamService {
    Team findById(Long id);
}
