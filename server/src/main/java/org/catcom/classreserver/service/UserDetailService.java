package org.catcom.classreserver.service;

import org.catcom.classreserver.model.user.UserDetail;
import org.catcom.classreserver.model.user.UserRepos;
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

    public UserDetail loadUserById(int id) throws UsernameNotFoundException
    {
        var user = userRepos.findById(id);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found: " + id);

        return new UserDetail(user.get());
    }

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

        if (auth == null) return null;

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
