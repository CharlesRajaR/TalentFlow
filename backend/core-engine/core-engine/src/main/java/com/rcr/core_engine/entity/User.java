package com.rcr.core_engine.entity;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rcr.core_engine.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
@Builder
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="first_name", nullable=false, length = 50)
    private String firstName;

    @Column(name="last_name", nullable=false, length = 50)
    private String lastName;

    @Column(nullable=false, length=20, unique=true)
    private String mobile;

    @Column(unique = true, length=100, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable=false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Role role;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt; 

    @PrePersist
    protected  void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}