package com.balamaci.flux.webclientdemo.user.controller;


import com.balamaci.flux.webclientdemo.user.User;
import com.balamaci.flux.webclientdemo.user.exception.BannedUserException;
import com.balamaci.flux.webclientdemo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<User> retrieveAll() {
        return userRepository.getAllUsers();
    }

    @GetMapping
    public Flux<String> retrieveAllUserIds() {
        return userRepository.getAllUsers().map(User::getUsername);
    }

    @GetMapping("/ids")
    public Flux<String> findById(String username) {
        if(username.equals(User.BANNED_USER.getUsername())) {
            throw new BannedUserException();
        }

        return userRepository.getAllUsers().map(User::getUsername);
    }


}
