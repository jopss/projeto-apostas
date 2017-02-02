package com.jopss.apostas.servicos.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

        public CustomAuthenticationEntryPoint(String loginFormUrl) {
                super(loginFormUrl);
        }

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                String contentType = ((HttpServletRequest) request).getHeader("Content-Type");
                if (authException != null && contentType != null && contentType.contains("application/json")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                        super.commence(request, response, authException);
                }
        }
}
