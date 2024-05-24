package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.dto.RoleDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        //give all users which is deleted fields is false in DB
        List<User> usersList =userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return usersList.stream().map(userMapper::convertToDTO).toList();
    }

    @Override
    public UserDTO findByUserName(String username) {
       return userMapper.convertToDTO(userRepository.findByUserNameAndIsDeleted(username,false));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
       userRepository.deleteById(userRepository.findByUserNameAndIsDeleted(username,false).getId());//deleteById method is internally managed by Spring Data JPA to operate within a transaction context
      //  userRepository.deleteByUserName(username); we need to use @Transactional with deleteByUserName() in UserRepository

    }

    @Override
    public UserDTO update(UserDTO user) {
        //find user
        User findUser = userRepository.findByUserNameAndIsDeleted(user.getUserName(),false);
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
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(manager,false);
        return users.stream().map(userMapper::convertToDTO).toList();


    }

    // will not delete from DB
    @Override
    public void delete(String username) {
        // find in DB and find the user
        User user =  userRepository.findByUserNameAndIsDeleted(username,false);
        if(checkIfUserCanBeDeleted(user)){
            //change the status is deleted
            user.setIsDeleted(true);
            // if the admin will create the user and use the same email(username)
            user.setUserName(user.getUserName()+"-"+user.getId());
            // save the objest
            userRepository.save(user);
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
