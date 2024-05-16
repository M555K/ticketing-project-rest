package com.company.entity;

import com.company.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name ="projects")
@Where(clause = "is_deleted=false")
public class Project extends BaseEntity{
    private String projectCode;
    @Column(columnDefinition = "DATE")
    private String projectName;
    @Column(columnDefinition = "DATE")
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status projectStatus;
    @Column(columnDefinition="text")
    private String projectDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;
}
