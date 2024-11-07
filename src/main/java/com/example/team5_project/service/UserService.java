package com.example.team5_project.service;

import com.example.team5_project.entity.User;
import com.example.team5_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public User findUserByName(String name){
        User user = userRepository.findByName(name).orElse(null);
        return user;
    }

    public User findUserByUserId(Long id){
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public User findUserByCommentId(Long id){
        User user = userRepository.findbyCommentId(id).orElse(null);
        return user;
    }

    public User findUserByPostId(Long id){
        User user = userRepository.findbyPostId(id).orElse(null);
        return user;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(User user){
        User findUser=userRepository.findById(user.getUserId())
                .orElseThrow(()->new RuntimeException());

        Optional.ofNullable(user.getName())
                .ifPresent(name->findUser.setName(name));
        Optional.ofNullable(user.getPassword())
                .ifPresent(password->findUser.setPassword(password));
        Optional.ofNullable(user.getEmail())
                .ifPresent(email->findUser.setEmail(email));
        Optional.ofNullable(user.getAttendance())
                .ifPresent(attendance->findUser.setAttendance(attendance));

        return userRepository.save(findUser);
    }

    public void deleteUser(Long userId){
        User findUser=userRepository.findById(userId)
                        .orElseThrow(()->new RuntimeException());
        userRepository.delete(findUser);
    }
}

