package com.company.service.impl;

import com.company.dto.RoleDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.UserService;
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
       return userMapper.convertToDTO(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
       userRepository.deleteById(userRepository.findByUserName(username).getId());//deleteById method is internally managed by Spring Data JPA to operate within a transaction context
      //  userRepository.deleteByUserName(username); we need to use @Transactional with deleteByUserName() in UserRepository

    }

    @Override
    public UserDTO update(UserDTO user) {
        //find user
        User findUser = userRepository.findByUserName(user.getUserName());
        // convert to the entity
        User convertedUser = userMapper.convertToEntity(user);
        //set id to the entity
        convertedUser.setId(findUser.getId());
        //save to the DB
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());
    }

    @Override
    public List<UserDTO> listAllByRole(String manager) {
        // go to Db and give the users with the role
        List<User> users = userRepository.findByRoleDescriptionIgnoreCase(manager);
        return users.stream().map(userMapper::convertToDTO).toList();


    }

    // will not delete from DB
    @Override
    public void delete(String username) {
        // find in DB and find the user
        User user =  userRepository.findByUserName(username);
        //change the status is deleted
        user.setIsDeleted(true);
        // save the objest
        userRepository.save(user)
        ;
    }
}
