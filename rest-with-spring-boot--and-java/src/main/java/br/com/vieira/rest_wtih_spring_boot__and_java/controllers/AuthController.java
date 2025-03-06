package br.com.vieira.rest_wtih_spring_boot__and_java.controllers;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.security.AccountCredentialsDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.security.TokenDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = service.signIn(credentials);
        if (token == null) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(("Invalid client request!"));
        }
        return ResponseEntity.ok(service.signIn(credentials).getBody());
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null && StringUtils.isBlank(credentials.getPassword()) || StringUtils.isBlank(credentials.getUsername());
    }

    @Operation(summary = "Refresh Token for authenticated user and returns a token")
    @PostMapping("/refresh;{username}")
    public ResponseEntity<?> refresktoken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalids(username,refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = service.refreshToken(username,refreshToken);
        if (token == null) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(("Invalid client request!"));
        }
        return ResponseEntity.ok(token).getBody();
    }

    private boolean parametersAreInvalids(String username, String refreskToken){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(refreskToken)){
            return true;
        }
       return false;
    }


}
