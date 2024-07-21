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
    private List<User> getUsers () {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Elizabeth");
        return List.of(user,user2);
    }
    private List<UserDTO> getUsersDTO () {
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Elizabeth");
        return List.of(userDTO,userDTO2);
    }
    @Test
    public void should_list_all_users(){

        // given - Preparation
        List<User> users = getUsers();
        List<UserDTO> userDTOs = getUsersDTO();

        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(users);
        when(userMapper.convertToDTO(eq(users.get(0)))).thenReturn(userDTOs.get(0));
        when(userMapper.convertToDTO(eq(users.get(1)))).thenReturn(userDTOs.get(1));

        List<UserDTO> expectedList = userDTOs;
        System.out.println(expectedList);

        // when - Action
        List<UserDTO> actualList = userService.listAllUsers();
        System.out.println(actualList);

        // then - Assertion/Verification
        // AssertJ
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);

        verify(userRepository, times(1)).findAllByIsDeletedOrderByFirstNameDesc(false);
        verify(userRepository, never()).findAllByIsDeletedOrderByFirstNameDesc(true);
        //  verify(userRepository).findAllByIsDeletedOrderByFirstNameDesc(true);//fail --> because is not invoke
        //   verify(userRepository,atLeastOnce()).findAllByIsDeletedOrderByFirstNameDesc(false);
        //  verify(userRepository,atMost(1)).findAllByIsDeletedOrderByFirstNameDesc(false);
        //verify(userRepository,never()).findAllByIsDeletedOrderByFirstNameDesc(false);// fail--> I called this method
        //  verify(userRepository,never()).findAllByIsDeletedOrderByFirstNameDesc(true);
    }

    @Test
    public void should_trow_no_such_element_exception_when_user_not_found(){
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(null);
        Throwable actualException = assertThrows(NoSuchElementException.class, () -> userService.findByUserName("username"));
        //   Throwable actualExceptionExactly = assertThrowsExactly(NoSuchElementException.class, () -> userService.findByUserName("username"));//fail--> not exactly the same exception
        assertEquals("User Not Found.",actualException.getMessage());
    }
    @AfterEach
    public void tearDown() throws Exception {
        // Some impl
    }

    // TestNG
    // Test Engine



}
