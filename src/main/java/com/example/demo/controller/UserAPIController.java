package com.example.demo.controller;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Board;
import com.example.demo.model.QUser;
import com.example.demo.model.User;
import com.example.demo.repository.CustomizedUserRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
class UserAPIController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method, @RequestParam(required = false) String text) {
        Iterable<User> users = null;
        if("query".equals(method)){
            users = userRepository.findByUsernameQuery(text);
        } else if ("nativeQuery".equals(method)) {
            users = userRepository.findByUsernameNativeQuery(text);
        } else if ("querydsl".equals(method)) {
            QUser user = QUser.user;
            BooleanExpression predicate = user.username.contains(text); // if문을 추가하여 추가사항을 입력가능
            users = userRepository.findAll((com.querydsl.core.types.Predicate) predicate);
        } else if ("querydslCustom".equals(method)) {
            users = userRepository.findByUsernameCustom(text);
        } else if ("jdbc".equals(method)) {
            users = userRepository.findByUsernameJdbc(text);
        } else if ("mybatis".equals(method)) {
            users = userMapper.getUsers(text);
        }else {
            users = userRepository.findAll();
        }
        return users;
    }

    @PostMapping("/users")
    User newuser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
//                user1.setTitle(user.getTitle());
//                user1.setContent(user.getContent());
//                user.setBoards(newUser.getBoards());
            user.getBoards().clear();
            user.getBoards().addAll(newUser.getBoards());
                for (Board board : user.getBoards()) {
                    board.setUser(user);
                }
                return userRepository.save(user);
            })
            .orElseGet(() -> {
                newUser.setId(id);
                return userRepository.save(newUser);
            });
    }

    @DeleteMapping("/users/{id}")
    void deleteuser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }


}
