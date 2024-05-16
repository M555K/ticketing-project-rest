package com.company.repository;

import com.company.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByProjectCode(String code);
}
