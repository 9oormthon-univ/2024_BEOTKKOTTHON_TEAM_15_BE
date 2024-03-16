package beotkkotthon.Newsletter_BE.payload;

import beotkkotthon.Newsletter_BE.payload.dto.ErrorReasonDto;

public interface BaseErrorCode {

    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();
}
