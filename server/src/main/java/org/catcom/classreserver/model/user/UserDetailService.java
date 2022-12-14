package org.catcom.classreserver.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService
{

    @Autowired
    private UserRepos userRepos;

    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = userRepos.findByEmail(username);

        if (user == null)
            throw new UsernameNotFoundException("User not found: " + username);

        return new UserDetail(user);
    }

    public UserDetail loadByAuthentication(Authentication auth)
    {

        var principal = auth.getPrincipal();

        if (principal instanceof Jwt jwt)
        {
            return loadUserByUsername(jwt.getSubject());
        }


        if (principal instanceof UserDetail userDetailPrincipal)
        {
            return userDetailPrincipal;
        }

        return null;
    }
}
