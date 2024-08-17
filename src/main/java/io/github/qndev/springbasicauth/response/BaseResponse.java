package io.github.qndev.springbasicauth.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private int status;

    private String message;

    private T data;

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(200, message, data);
    }

    public static <T> BaseResponse<T> error(String message, T data) {
        return new BaseResponse<>(500, message, data);
    }

    public static <T> String toJson(@NonNull T src) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(src);
    }

    public static <T> String toJson(int status, String message, T data) throws JsonProcessingException {
        BaseResponse<T> response = new BaseResponse<>(status, message, data);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(response);
    }

}
