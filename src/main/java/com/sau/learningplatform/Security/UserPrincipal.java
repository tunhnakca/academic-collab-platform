package com.sau.learningplatform.Security;

import com.sau.learningplatform.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Slf4j
public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole().toLowerCase();
        if (role.equals("student")) {
            return Collections.singleton(new SimpleGrantedAuthority("STUDENT"));
        }
        if (role.equals("instructor") || role.equals("ınstructor")) {
            return Collections.singleton(new SimpleGrantedAuthority("INSTRUCTOR"));
        }
        if (role.equals("admin") || role.equals("admın")) {
            return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
        }

        log.error("invalid role!");
        return Collections.emptyList();

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}