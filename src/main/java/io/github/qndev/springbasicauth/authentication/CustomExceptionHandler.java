package io.github.qndev.springbasicauth.authentication;

import io.github.qndev.springbasicauth.response.BaseResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomExceptionHandler implements ICustomExceptionHandler {
    @Override
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String detailMessage = exception.getMessage();
        if (exception instanceof AccessDeniedException) {
            detailMessage = "Access Denied Exception";
        } else if (exception instanceof AuthenticationException) {
            detailMessage = "Authentication Exception";
        }

        response.getWriter().write(BaseResponse.toJson(response.getStatus(), detailMessage, null));
    }
}
