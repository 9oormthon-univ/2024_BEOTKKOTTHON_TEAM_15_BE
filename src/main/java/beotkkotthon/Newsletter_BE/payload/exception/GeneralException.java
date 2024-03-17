package beotkkotthon.Newsletter_BE.payload.exception;

import beotkkotthon.Newsletter_BE.payload.BaseErrorCode;
import beotkkotthon.Newsletter_BE.payload.dto.ErrorReasonDto;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public GeneralException(ErrorStatus errorStatus, String message) {
        super(errorStatus.getMessage(message));
        this.code = errorStatus;
    }

    public GeneralException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus.getMessage(message), cause);
        this.code = errorStatus;
    }


    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
