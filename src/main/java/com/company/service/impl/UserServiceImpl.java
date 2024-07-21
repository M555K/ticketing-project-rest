package com.company.service.impl;

import com.company.annotation.DefaultExceptionMessage;
import com.company.dto.ProjectDTO;
import com.company.dto.RoleDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.exception.TicketingProjectException;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.KeycloakService;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService, KeycloakService keycloakService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        //give all users which is deleted fields is false in DB
        List<User> usersList =userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
      // return usersList.stream().map(userMapper::convertToDTO).toList();
        return usersList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
     //  return userMapper.convertToDTO(userRepository.findByUserNameAndIsDeleted(username,false));
       User user = userRepository.findByUserNameAndIsDeleted(username,false);
       if(user == null) throw new NoSuchElementException("User Not Found.");
       return userMapper.convertToDTO(user);
    }

    @Override
    public UserDTO save(UserDTO user) {
        user.setEnabled(true);
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        User userEntity = userMapper.convertToEntity(user);

        User sevedUser = userRepository.save(userEntity);
        keycloakService.userCreate(user);
        return userMapper.convertToDTO(sevedUser);
    }

    @Override
    public void deleteByUserName(String username) {
       userRepository.deleteById(userRepository.findByUserNameAndIsDeleted(username,false).getId());//deleteById method is internally managed by Spring Data JPA to operate within a transaction context
      //  userRepository.deleteByUserName(username); we need to use @Transactional with deleteByUserName() in UserRepository

    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        userDTO.setPassWord(passwordEncoder.encode(userDTO.getPassWord()));
        //find user
        User findUser = userRepository.findByUserNameAndIsDeleted(userDTO.getUserName(),false);
        // convert to the entity

        User convertedUser = userMapper.convertToEntity(userDTO);
        //set id to the entity
        convertedUser.setId(findUser.getId());
        //save to the DB
        User updatedUser = userRepository.save(convertedUser);
        return userMapper.convertToDTO(updatedUser);
    }

    @Override
    public List<UserDTO> listAllByRole(String manager) {
        // go to Db and give the users with the role
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(manager,false);
        return users.stream().map(userMapper::convertToDTO).toList();


    }

    // will not delete from DB
    @Override
    @DefaultExceptionMessage(defaultMessage = "Failed to delete a user")
    public void delete(String username) throws TicketingProjectException {
        // find in DB and find the user
        User user =  userRepository.findByUserNameAndIsDeleted(username,false);
        if(checkIfUserCanBeDeleted(user)){
            //change the status is deleted
            user.setIsDeleted(true);
            // if the admin will create the user and use the same email(username)
            user.setUserName(user.getUserName()+"-"+user.getId());
            //delete from keycloak
            keycloakService.delete(username);
            // save the objest
            userRepository.save(user);
        }else {
                throw new TicketingProjectException("User can't be deleted");
        }

    }
    private boolean checkIfUserCanBeDeleted(User user ){
        switch (user.getRole().getDescription()){
            case "Manager":
              List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user)
              );
              return projectDTOList.isEmpty();
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDTO(user)
                );
                return taskDTOList.isEmpty();
            default:
                return true;
        }
    }
}
