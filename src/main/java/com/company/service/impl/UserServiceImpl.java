package com.company.service.impl;

import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.mapper.RoleMapper;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;

        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> usersList =userRepository.findAll(Sort.by("firstName"));
        return usersList.stream().map(userMapper::convertToDTO).toList();

    }

    @Override
    public UserDTO findByUserName(String username) {
        return null;
    }

    @Override
    public void save(UserDTO user) {

    }

    @Override
    public void deleteByUserName(String username) {

    }
}
