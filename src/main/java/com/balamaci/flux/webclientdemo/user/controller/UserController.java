package com.balamaci.flux.webclientdemo.user.controller;


import com.balamaci.flux.webclientdemo.user.User;
import com.balamaci.flux.webclientdemo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping
    public Mono<ServerResponse> findById(String username) {
        if(username.equals(User.BANNED_USER.getUsername())) {
            return
        }
        return userRepository.getAllUsers();
    }


}
