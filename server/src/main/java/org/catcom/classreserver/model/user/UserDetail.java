package org.catcom.classreserver.model.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetail implements UserDetails
{

    @Getter
    private final User user;

    public UserDetail(User user)
    {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        var roleName = user.getRole();
        return switch (roleName) {
            case "admin" -> List.of( UserRole.ADMIN, UserRole.STAFF, UserRole.USER );
            case "staff" -> List.of( UserRole.STAFF, UserRole.USER );
            case "user" -> List.of( UserRole.USER );
            default -> null;
        };
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.getEnabled();
    }

    public boolean isStaff()
    {
        return getAuthorities().contains(UserRole.STAFF);
    }

    public int getId()
    {
        return user.getId();
    }

    public String getFullName()
    {
        return user.getFirstName() + " " + user.getLastName();
    }

}
