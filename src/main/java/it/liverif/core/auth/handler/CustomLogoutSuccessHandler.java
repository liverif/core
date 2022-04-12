package it.liverif.core.auth.handler;

import it.liverif.core.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String URL = request.getContextPath() + "/login";

        HttpSession session = request.getSession(false);
        log.info("LOGOUT - USER: " + ((User) authentication.getPrincipal()).getUsername() +
                " * IP:" + LogUtils.getClientIP(request) +
                " * USER-AGENT:" + LogUtils.getUserAgent(request));

        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);
    }

}