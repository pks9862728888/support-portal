package com.demo.supportportal.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.demo.supportportal.constants.SecurityConstants;
import com.demo.supportportal.models.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JWTTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    private String generateJwtToken(UserPrincipal userPrincipal) {
        List<String> claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(SecurityConstants.GET_ARRAYS_LLC)
                .withAudience(SecurityConstants.GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_IN_MILLISECONDS))
                .withClaim(SecurityConstants.AUTHORITIES, claims)
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        return getClaimsFromToken(token)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                null,   // because we have already verified the token
                authorities
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJwtVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    public String getSubjectFromToken(String token) {
        JWTVerifier verifier = getJwtVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date tokenExpirationDate = verifier
                .verify(token)
                .getExpiresAt();
        return tokenExpirationDate.before(new Date());
    }

    private List<String> getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier
                .verify(token)
                .getClaim(SecurityConstants.AUTHORITIES)
                .asList(String.class);
    }

    private JWTVerifier getJwtVerifier() throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.require(algorithm)
                    .withIssuer(SecurityConstants.GET_ARRAYS_LLC)
                    .build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(SecurityConstants.TOKEN_CANNOT_BE_VERIFIED);
        }
    }

    private List<String> getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities()
                .stream()
                .map(g -> String.valueOf(g.getAuthority()))
                .collect(Collectors.toList());
    }
}
