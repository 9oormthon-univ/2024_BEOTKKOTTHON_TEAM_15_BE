package beotkkotthon.Newsletter_BE.payload;

import beotkkotthon.Newsletter_BE.payload.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder
@Builder
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(Include.NON_NULL)  // result필드가 null값일때, JSON 직렬화에서 이 필드가 무시됨.
    private T result;


    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessStatus.OK.getCode(), SuccessStatus.OK.getMessage(), result);
    }
    public static <T> ApiResponse<T> onCreate(T result) {
        return new ApiResponse<>(true, SuccessStatus.CREATED.getCode(), SuccessStatus.CREATED.getMessage(), result);
    }
    public static <T> ApiResponse<T> onUpdateDelete(T result) {  // 반환값은 'ApiResponse'이자 'return ApiResponse.onUpdateDelete(null);'으로 작성하면 된다.
        return new ApiResponse<>(true, SuccessStatus.NO_CONTENT.getCode(), SuccessStatus.NO_CONTENT.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }


    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
