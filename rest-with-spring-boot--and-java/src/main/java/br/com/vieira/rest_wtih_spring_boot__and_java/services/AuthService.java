package br.com.vieira.rest_wtih_spring_boot__and_java.services;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.security.AccountCredentialsDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.security.TokenDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.exception.RequiredObjectIsNullException;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.User;
import br.com.vieira.rest_wtih_spring_boot__and_java.repositoy.UserRepository;
import br.com.vieira.rest_wtih_spring_boot__and_java.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static br.com.vieira.rest_wtih_spring_boot__and_java.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        var user = repository.findByUserName(credentials.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");


        }

        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String userName, String refreshtoken) {


        var user = repository.findByUserName(userName);

        if (user == null) {

            var token = tokenProvider.createAccessToken(user.getUserName(), user.getRoles());
            return ResponseEntity.ok(token);

        } else {
            throw new UsernameNotFoundException("Username " + userName + " not found!");
        }


    }

    public String generateHashedPassWord(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pdbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pdbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }


    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if(user == null){
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating one User");

        var entity = new User();
        entity.setFullName(user.getFullname());
        entity.setUserName(user.getUsername());
        entity.setPassword(generateHashedPassWord(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialNonExpired(true);
        entity.setEnabled(true);
        return parseObject(repository.save(entity), AccountCredentialsDTO.class);

    }

}
