package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.converter.TeamConverter;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.repository.MemberTeamRepository;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.ImageUploadService;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.service.MemberTeamService;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final ImageUploadService imageUploadService;
    private final MemberService memberService;
    private final MemberTeamService memberTeamService;


    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
    }

//    @Transactional
//    @Override
//    public TeamResponseDto createTeam(TeamSaveRequestDto teamSaveRequestDto) throws IOException {
//        String imageUrl = imageUploadService.uploadImage(teamSaveRequestDto.getImage());
//
//        String uuid = UUID.randomUUID().toString();
//        // 20자리의 UUID 생성.
//        long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
//        String link = "/teams?link=" + Long.toString(l, 9);
////        // 10자리의 UUID 생성.
////        int l = ByteBuffer.wrap(uuid.getBytes()).getInt();
////        String link = "/teams?link=" + Integer.toString(l, 9);
//
//        Team team = teamSaveRequestDto.toEntity(imageUrl, link);
//        teamRepository.save(team);
//
//        Long loginMemberId = SecurityUtil.getCurrentMemberId();
//        Member loginMember = memberService.findById(loginMemberId);
//
//        // 그룹에 CREATOR로 save 시킴
//        Role role = Role.CREATOR;
//        MemberTeam memberTeam = MemberTeam.MemberTeamSaveBuilder()
//                .role(role)
//                .member(loginMember)
//                .team(team)
//                .build();
//        memberTeamRepository.save(memberTeam);
//
//        return new TeamResponseDto(team);
//    }


    @Override
    public Team createTeam(Long memberId, TeamSaveRequestDto teamSaveRequestDto) throws IOException {
        String imageUrl = imageUploadService.uploadImage(teamSaveRequestDto.getImage());

        String uuid = UUID.randomUUID().toString();
        // 20자리의 UUID 생성.
        long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
        String link = "/teams?link=" + Long.toString(l, 9);
//        // 10자리의 UUID 생성.
//        int l = ByteBuffer.wrap(uuid.getBytes()).getInt();
//        String link = "/teams?link=" + Integer.toString(l, 9);

        Team team = teamSaveRequestDto.toEntity(imageUrl, link);
        teamRepository.save(team);

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member loginMember = memberService.findById(loginMemberId);

        // 그룹에 CREATOR로 save 시킴
        Role role = Role.CREATOR;
        MemberTeam memberTeam = MemberTeam.MemberTeamSaveBuilder()
                .role(role)
                .member(loginMember)
                .team(team)
                .build();
        memberTeamRepository.save(memberTeam);

        return team;
    }

    @Transactional
    @Override
    public List<TeamResponseDto> findTeamsByMember(String name, String link) {  // 그룹명 검색 or 초대링크 클릭 or 내가 가입한 팀 목록 조회

        if(name != null && link == null) {  // 그룹명을 검색한 경우
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
        else if(name == null && link != null) {  // 초대링크를 누른 경우
            String findLink = "/teams?link=" + link;
            Team team = teamRepository.findByLink(findLink).orElseThrow(
                    () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
            List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
            teamResponseDtoList.add(new TeamResponseDto(team));
            return teamResponseDtoList;  // 초대링크의 UUID link와 일치하는 그룹을 검색해서 알려줌.
        }
        else if(name == null && link == null) {  // 내가 가입한 팀 목록 조회
            Long memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberService.findById(memberId);
            List<MemberTeam> memberTeamList = member.getMemberTeamList();
            List<Team> teamList = memberTeamList.stream().map(MemberTeam::getTeam).collect(Collectors.toList());
            return teamList.stream().map(TeamResponseDto::new)
                    .sorted(Comparator.comparing(TeamResponseDto::getName)
                            .thenComparing(TeamResponseDto::getId, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        else {  // 잘못된 url
            throw new GeneralException(ErrorStatus.BAD_REQUEST);
        }
    }

    @Override
    public TeamResponseDto.ShowTeamDto showTeamById(Long memberId, Long teamId, List<News> newsList) {
        Member member = memberService.findById(memberId);
        Team team = findById(teamId);
        MemberTeam memberTeam = memberTeamService.findByMemberAndTeam(member, team);
        Role role = memberTeam.getRole();

        return TeamConverter.toShowTeamDto(team, role, newsList);
    }
}
