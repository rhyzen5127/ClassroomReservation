package org.catcom.classreserver.controller;

import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.user.User;
import org.catcom.classreserver.model.user.UserRepos;
import org.catcom.classreserver.service.JwtTokenService;
import org.catcom.classreserver.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController
{

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtTokenService tokenService;

    @GetMapping("/users/current")
    @ResponseBody User getCurrentUser(Authentication auth)
    {
        var userDetail = userDetailService.loadByAuthentication(auth);
        return userDetail.getUser();
    }

    @PostMapping("/token")
    @ResponseBody
    Map<String, Object> getAccessToken(Authentication auth)
    {

        var accessToken = tokenService.generateAccessToken(auth);
        //var userDetail = userDetailService.loadByAuthentication(auth);

        var responseData = new HashMap<String, Object>();
        responseData.put("token", accessToken);
        return responseData;

    }

}
