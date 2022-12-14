package org.catcom.classreserver.security;

import com.nimbusds.jose.crypto.impl.HMAC;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.catcom.classreserver.model.user.UserDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) throws IOException, ServletException
    {
        var userDetail = (UserDetail) auth.getPrincipal();

        var accessToken = Jwt
                .withTokenValue("")
                .subject(userDetail.getUsername())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                .issuer(request.getRequestURI())
                .claim("roles", userDetail.getAuthorities().stream().toList())
                .build();

        var refreshToken = Jwt
                .withTokenValue("")
                .subject(userDetail.getUsername())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(30)))
                .issuer(request.getRequestURI())
                .build();
    }
}
