package com.company.repository;

import com.company.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
      User findByUserNameAndIsDeleted(String username, Boolean deleted);
      List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);

      void deleteByUserName(String username);
      List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String role,Boolean deleted);
}
