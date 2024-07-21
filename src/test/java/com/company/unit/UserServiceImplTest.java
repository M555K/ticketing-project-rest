package com.company.unit;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.company.dto.RoleDTO;
import com.company.dto.UserDTO;
import com.company.entity.Role;
import com.company.entity.User;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.KeycloakService;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    User user;
    UserDTO userDTO;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User();
        user.setId(1L);
        user.setFirstName("Adam");
        user.setLastName("Ronan");
        user.setUserName("adamronan");
        user.setPassWord("abc1");
        user.setEnabled(true);
        user.setRole(new Role("Employee"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Adam");
        userDTO.setLastName("Ronan");
        userDTO.setUserName("adamronan");
        userDTO.setPassWord("abc1");
        userDTO.setEnabled(true);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Employee");
        userDTO.setRole(roleDTO);
    }


}
