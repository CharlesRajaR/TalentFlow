package com.rcr.core_engine.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
class CandidateRegistrationRequest{
   @NotBlank(message = "First Name is required")
   private String firstName;

   @NotBlank(message = "Last Name is required")
   private String lastName;

   @Email(message = "Invalid email address")
   @NotBlank(message = "Email is required")
   private String email;

   @NotBlank(message = "Mobile Number is required")
   @Pattern(regexp = "^[0-9]{10,15}$", message = "mobile number must be between 10 and 15 digits")
   private String mobile;

   @NotNull(message = "Date of birth is required")
   private LocalDate dob;

   @NotBlank(message = "Password is required")
   @Size(min = 6, message = "password must be at least 6 characters")
   private String password;

   @NotBlank(message = "captcha is required")
   private String captchaToken;
}