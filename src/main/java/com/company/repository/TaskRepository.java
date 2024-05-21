package com.company.repository;

import com.company.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query ("select count(t) from Task t where t.project.projectCode = ?1 and t.taskStatus = 'IN_PROGRESS' or t.project.projectCode = ?1 and t.taskStatus = 'OPEN'")
   // @Query ("select count(t) from Task t where t.project.projectCode = ?1 and t.project.projectStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);
    @Query (value = "select count(*) from tasks t join projects p on t.project_id = p.id where p.project_code = ?1 and t.task_status = 'COMPLETE'",nativeQuery = true)
    int totalCompletedTasks(String projectCode);
}
