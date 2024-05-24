package com.company.repository;

import com.company.entity.Project;
import com.company.entity.User;
import com.company.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByProjectCode(String code);
    List<Project> findAllByAssignedManager(User manager);

    List<Project> findAllByProjectStatusIsNotAndAssignedManager(Status status, User assignedManager);
}
