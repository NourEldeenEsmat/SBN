package com.nouresmat.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "first name is mandatory")
    @NotBlank(message = "first name is mandatory")
    private String firstName;
    @NotEmpty(message = "last name is mandatory")
    @NotBlank(message = "last name is mandatory")
    private String lastName;
    @NotEmpty(message = "email name is mandatory")
    @NotBlank(message = "email name is mandatory")
    @Email(message = "email is not formatted")
    private String email;
    @Size(min = 8,message = "password should be 8")
    private String password;
}
