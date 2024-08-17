package io.github.qndev.springbasicauth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ICustomExceptionHandler {
    void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws IOException;
}
