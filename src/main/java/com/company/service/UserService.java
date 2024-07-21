package com.company.service;

import com.company.dto.RoleDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.exception.TicketingProjectException;

import java.util.List;

public interface UserService {
    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    UserDTO save(UserDTO user);
    void deleteByUserName(String username);
    UserDTO update(UserDTO user);
    void delete(String username) throws TicketingProjectException;

    List<UserDTO> listAllByRole(String manager);
}
