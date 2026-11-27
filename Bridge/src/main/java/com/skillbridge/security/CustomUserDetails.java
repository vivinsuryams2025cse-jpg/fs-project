package com.skillbridge.security;

import com.skillbridge.entity.Student;
import com.skillbridge.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final UserRole role;
    private final boolean enabled;

    public CustomUserDetails(Student student) {
        this.id = student.getId();
        this.fullName = student.getFullName();
        this.email = student.getEmail();
        this.password = student.getPassword();
        this.role = student.getRole();
        this.enabled = student.isEnabled();
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public UserRole getRole() { return role; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}
