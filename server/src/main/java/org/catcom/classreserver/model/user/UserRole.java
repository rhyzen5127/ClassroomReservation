package org.catcom.classreserver.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum UserRole {

    USER {
        @Override
        public List<GrantedAuthority> getAuthorities() { return List.of( AUTH_USER ); }
    },

    STAFF {
        @Override
        public List<GrantedAuthority> getAuthorities() { return List.of( AUTH_USER, AUTH_STAFF ); }
    },

    ADMIN {
        @Override
        public List<GrantedAuthority> getAuthorities() { return List.of( AUTH_USER, AUTH_STAFF, AUTH_ADMIN ); }
    }
    ;

    private static final GrantedAuthority AUTH_USER = () -> "user";
    private static final GrantedAuthority AUTH_STAFF = () -> "staff";
    private static final GrantedAuthority AUTH_ADMIN = () -> "admin";

    public List<GrantedAuthority> getAuthorities() { return null; }
}
