package com.xupt.demo.service;

import com.xupt.demo.model.User;
import com.xupt.demo.repository.UserRepository;
import com.xupt.demo.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(Md5Util.getMD5String(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNickname(userDetails.getNickname());
            user.setEmail(userDetails.getEmail());
            user.setSex(userDetails.getSex());
            user.setBirthday(userDetails.getBirthday());
            user.setUpdatedAt(java.time.LocalDateTime.now());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("用户未找到"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
