package it.liverif.core.auth.handler;

import it.liverif.core.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String URL = request.getContextPath() + "/login?error";

        HttpSession session = request.getSession(true);
        String user = request.getParameter("username");
        log.info("LOGIN FAILURE - USER:" + user +
                " * TYPE:" + exception.getMessage() +
                " * SESSION-ID:" + session.getId() +
                " * IP:" + LogUtils.getClientIP(request) +
                " * USER-AGENT:" + LogUtils.getUserAgent(request));

        session.invalidate();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect(URL);
    }
}