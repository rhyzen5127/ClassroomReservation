package org.catcom.classreserver.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService
{

    @Autowired
    private UserRepos repos;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = repos.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found: " + username);
        return new UserDetail(user);
    }
}
