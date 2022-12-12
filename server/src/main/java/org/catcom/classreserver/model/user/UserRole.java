package org.catcom.classreserver.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum UserRole implements GrantedAuthority {

    USER {
        @Override
        public String getAuthority()
        {
            return "user";
        }
    },

    STAFF {
        @Override
        public String getAuthority()
        {
            return "staff";
        }
    },

    ADMIN {
        @Override
        public String getAuthority()
        {
            return "admin";
        }
    }


}
