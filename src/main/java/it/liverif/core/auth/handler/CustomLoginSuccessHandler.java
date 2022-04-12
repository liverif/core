package it.liverif.core.auth.handler;

import it.liverif.core.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String URL = request.getContextPath() + "/app";

        User user=(User) authentication.getPrincipal();
        HttpSession session = request.getSession(true);

        log.info("LOGIN SUCCESS - USER: " + user.getUsername() +
                " * IP:" + LogUtils.getClientIP(request) +
                " * USER-AGENT:" + LogUtils.getUserAgent(request));

        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);
    }

}