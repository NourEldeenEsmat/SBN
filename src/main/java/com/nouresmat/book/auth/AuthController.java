package com.nouresmat.book.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final AuthServices authServices;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest)
            throws MessagingException {
        authServices.register(registerRequest);
        return ResponseEntity.accepted().build();
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authentication(
            @RequestBody @Valid AuthRequest authRequest
    ){
        return ResponseEntity.ok(authServices.authenticate(authRequest));
    }
    @GetMapping("/activate-account")
    public void confirm(String token) throws MessagingException {
        authServices.activateAccount(token);
    }
}
