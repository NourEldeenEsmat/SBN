package com.nouresmat.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotEmpty(message = "email name is mandatory")
    @NotBlank(message = "email name is mandatory")
    @Email(message = "email is not formatted")
    private String email;
    @Size(min = 8,message = "password should be 8")
    private String password;
}
