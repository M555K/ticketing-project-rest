package com.company.unit;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.company.dto.ProjectDTO;
import com.company.dto.RoleDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.entity.Role;
import com.company.entity.User;
import com.company.exception.TicketingProjectException;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void should_encode_user_password_on_save_operation() {
        //given
        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convertToDTO(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        String expectedPassword="password";
        //when
        UserDTO savedUser =  userService.save(userDTO);
        //then
        assertEquals(expectedPassword,savedUser.getPassWord());
        verify(passwordEncoder).encode(anyString());
    }
    @Test
    public void should_encode_user_password_on_update_operation() {
        //given
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(user);
        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convertToDTO(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        String expectedPassword="password";
        //when
        UserDTO updateUser =  userService.update(userDTO);
        //then
        assertEquals(expectedPassword,updateUser.getPassWord());
        verify(passwordEncoder).encode(anyString());
    }
    @Test
    void should_delete_manager_if_all_projects_is_completed() throws TicketingProjectException {
        //given
        User manager = getUserWithRole("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(manager);
        when(userRepository.save(any())).thenReturn(manager);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());
        //action
        userService.delete(manager.getUserName());

        //then
        assertTrue(manager.getIsDeleted());
        assertNotEquals("user3",manager.getUserName());

    }

    @Test
    void should_delete_employee_if_all_task_is_completed() throws TicketingProjectException {
        //given
        User employee = getUserWithRole("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(employee);
        when(userRepository.save(any())).thenReturn(employee);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
        //action
        userService.delete(employee.getUserName());

        //then
        assertTrue(employee.getIsDeleted());
        assertNotEquals("user3",employee.getUserName());

    }

    @ParameterizedTest
    @ValueSource(strings = {"Employee","Manager"})
    public void should_delete_user(String role) throws TicketingProjectException {
        User testUser = getUserWithRole(role);
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(testUser);
        when(userRepository.save(any())).thenReturn(testUser);
        lenient().when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(testUser);
        lenient().when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(testUser);
        userService.delete(testUser.getUserName());
    }

    @Test
    void should_throw_exception_when_deleting_manager_with_project(){
        User manager = getUserWithRole("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(manager);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>(List.of(new ProjectDTO())));

        Throwable actualException = assertThrows(TicketingProjectException.class, () -> userService.delete(manager.getUserName()));

        assertEquals("User can't be deleted", actualException.getMessage());

    }
    @Test
    void should_throw_exception_when_deleting_employee_with_tasks(){
        User employee = getUserWithRole("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(employee);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>(List.of(new TaskDTO())));
        Throwable actualExceptionExactly = assertThrows(TicketingProjectException.class, () -> userService.delete(employee.getUserName()));
        assertEquals("User can't be deleted",actualExceptionExactly.getMessage());
    }

    private User getUserWithRole(String role) {
        User user3 = new User();
        user3.setFirstName("Adam");
        user3.setLastName("Ronan");
        user3.setUserName("user33");
        user3.setPassWord("abc2");
        user3.setEnabled(true);
        user3.setIsDeleted(false);
        user3.setRole(new Role(role));
        return user3;
    }
}
