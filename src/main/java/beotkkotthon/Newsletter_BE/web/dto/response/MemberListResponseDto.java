package beotkkotthon.Newsletter_BE.web.dto.response;

import beotkkotthon.Newsletter_BE.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResponseDto {

    private List<MemberResponseDto> leaderList;  // 본인을 제외한 리스트
    private List<MemberResponseDto> memberList;  // 본인을 제외한 리스트
    private Integer leaderCount;  // 본인을 제외한 숫자 카운팅
    private Integer memberCount;  // 본인을 제외한 숫자 카운팅
    private Role myRole;  // 본인 Role
}
