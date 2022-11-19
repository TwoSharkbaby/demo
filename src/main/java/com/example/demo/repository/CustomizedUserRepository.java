package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;

public interface CustomizedUserRepository {

    List<User> findByUsernameCustom(String username);

    List<User> findByUsernameJdbc(String username);

}
