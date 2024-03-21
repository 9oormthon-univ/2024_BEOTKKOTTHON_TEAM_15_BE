package beotkkotthon.Newsletter_BE.payload.status;

import beotkkotthon.Newsletter_BE.payload.BaseErrorCode;
import beotkkotthon.Newsletter_BE.payload.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // Common_Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "Bad request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON4001",  "Validation error"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON4002", "Requested resource not found"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000",  "Internal error"),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5001",  "Data access error"),

    // Member_Error
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4000", "MEMBER not found"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4001", "Email already exists"),

    // Team Error
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM4000", "TEAM not found"),

    // News Error
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "NEWS4000", "News not found"),

    // MemberTeam Error
    MEMBERTEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBERTEAM4000", "MEMBERTEAM not found"),

    // Participation Error
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTICIPATION4000", "PARTICIPATION not found"),

    // Notification Error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION4000", "NOTIFICATION not found"),

    // Token Error
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"TOKEN4000", "Invalid token"),

    // Security Error
    NOT_FOUND_CONTEXT(HttpStatus.NOT_FOUND,"Security4000", "SecurityContext not found");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}
