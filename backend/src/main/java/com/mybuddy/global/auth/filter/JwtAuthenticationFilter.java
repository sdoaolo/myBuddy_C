package com.mybuddy.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybuddy.global.auth.dto.LoginDto;
import com.mybuddy.global.auth.jwt.JwtTokenizer;
import com.mybuddy.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenizer jwtTokenizer;

    private final RedisTemplate<String, String> redisTemplate;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();

        String accessToken = jwtTokenizer.delegateAccessToken(member);
        String refreshToken = jwtTokenizer.delegateRefreshToken(member);

        redisTemplate.opsForValue().set(
                "RefreshToken:" + member.getEmail(),
                refreshToken,
                jwtTokenizer.getRefreshTokenExpirationMinutes(),
                TimeUnit.MINUTES);

        response.setHeader("Authorization", "Bearer " + accessToken);
        // 헤더 적용 시에 사용
        // response.setHeader("Refresh", refreshToken);

        int maxAge = 60 * jwtTokenizer.getRefreshTokenExpirationMinutes();

        //Secure설정 이전 버전 저장용 주석 (2023.03.25 강지은)
//        response.addHeader("Set-Cookie", "Refresh=" + refreshToken + "; SameSite=None; " +
//                "Max-Age=" + maxAge + "; HttpOnly; Path=/");
        
        response.addHeader("Set-Cookie", "Refresh=" + refreshToken + "; SameSite=None; " +
                "Max-Age=" + maxAge + "; HttpOnly; Path=/; Secure");

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
