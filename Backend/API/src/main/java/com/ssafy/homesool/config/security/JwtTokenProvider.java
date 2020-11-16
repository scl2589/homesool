package com.ssafy.homesool.config.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

	private final UserDetailsService userDetailsService;
	private final long tokenValidMilisecond = 1000L * 60 * 60 * 6; // 6시간 토큰 유효
	@Value("spring.jwt.secret")
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// Jwt 토큰 생성
	public String createToken(long userPk) {
		Claims claims = Jwts.claims().setSubject(Long.toString(userPk));
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims) // 데이터
			.setIssuedAt(now) // 토큰 발행일자
			.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
			.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
			.compact();
	}

	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// Jwt 토큰에서 회원 구별 정보 추출
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			// TODO Refresh Token 도입?
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
