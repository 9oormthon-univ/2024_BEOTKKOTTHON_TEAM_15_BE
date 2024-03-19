package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.ImageUploadService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.TeamSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ImageUploadService imageUploadService;


    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
    }

    @Transactional
    @Override
    public TeamResponseDto createTeam(MultipartFile image, TeamSaveRequestDto teamSaveRequestDto) throws IOException {
        String imageUrl = imageUploadService.uploadImage(image);

        String uuid = UUID.randomUUID().toString();
        // 20자리의 UUID 생성.
        long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
        String link = "/teams?link=" + Long.toString(l, 9);
//        // 10자리의 UUID 생성.
//        int l = ByteBuffer.wrap(uuid.getBytes()).getInt();
//        String link = "/teams?link=" + Integer.toString(l, 9);

        Team team = teamSaveRequestDto.toEntity(imageUrl, link);
        teamRepository.save(team);

        return new TeamResponseDto(team);
    }

    @Transactional
    @Override
    public List<TeamResponseDto> searchTeam(String name) {
        List<Team> teams = teamRepository.findByNameContaining(name);
        return teams.stream().map(TeamResponseDto::new)
                .sorted(Comparator.comparing(TeamResponseDto::getId, Comparator.reverseOrder()))  // id 내림차순 정렬 후 (최신 생성순)
                .sorted(Comparator.comparing(TeamResponseDto::getName))  // 이름 오름차순 정렬 (이름순)
                .collect(Collectors.toList());  // 정렬 완료한 리스트 반환 (연속sorted 정렬은 마지막 순서의 기준이 가장 주요된 기준임.)
//        return teams.stream().map(TeamResponseDto::new)
//                .sorted(Comparator.comparing(TeamResponseDto::getName)
//                        .thenComparing(TeamResponseDto::getId, Comparator.reverseOrder()))
//                .collect(Collectors.toList());
    }
}
