package br.com.vieira.rest_wtih_spring_boot__and_java.security.jwt;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.security.TokenDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.exception.InvalidJwtAuthenticationException;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.User;
import br.com.vieira.rest_wtih_spring_boot__and_java.repositoy.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-lenght:3600000}")
    private long validityInMilliseconds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserRepository repository;

    Algorithm algorithm = null;

    private User user;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String userName, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String acessToken = getAcessToken(userName, roles, now, validity);
        String refreshtoken = getRefreshToken(userName, roles, now);
        return new TokenDTO(userName, true, now, validity, acessToken, refreshtoken);
    }

    public TokenDTO refreshToken(String refreshToken) {

        if(StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ")){

            refreshToken.substring("Bearer ".length());
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String userName = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createAccessToken(userName,roles);
    }

    public String getAcessToken(String userName, List<String> roles, Date now, Date validity) {

        user = repository.findByUserName(userName);
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(issuerUrl)
                .sign(algorithm)
                .toString();
    }

    public String getRefreshToken(String userName, List<String> roles, Date now) {
        Date refreshTokenVailidity = new Date(now.getTime() + (validityInMilliseconds * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenVailidity)
                .withSubject(userName)
                .sign(algorithm)
                .toString();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedJWT(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedJWT(String token) {

        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return  decodedJWT;
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")){

            return bearerToken.substring("Bearer ".length());
        }
        return  null;
    }

    public boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedJWT(token);
        try {


            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
        }
    }

    public User getUser() {
        return user;
    }
}
