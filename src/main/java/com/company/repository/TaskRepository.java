package com.company.repository;

import com.company.dto.UserDTO;
import com.company.entity.Project;
import com.company.entity.Task;
import com.company.entity.User;
import com.company.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query ("select count(t) from Task t where t.project.projectCode = ?1 and t.taskStatus = 'IN_PROGRESS' or t.project.projectCode = ?1 and t.taskStatus = 'OPEN'")
   // @Query ("select count(t) from Task t where t.project.projectCode = ?1 and t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);
    @Query (value = "select count(*) from tasks t join projects p on t.project_id = p.id where p.project_code = ?1 and t.task_status = 'COMPLETE'",nativeQuery = true)
    int totalCompletedTasks(String projectCode);
    List<Task> findAllByProject(Project project);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);
    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);
}
