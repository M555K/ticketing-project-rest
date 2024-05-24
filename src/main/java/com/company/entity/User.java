package com.company.entity;

import com.company.entity.Role;
import com.company.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="users")
@NoArgsConstructor
//@Where(clause = "is_deleted=false")//--> trow an error , when the employee complete all the task, i delete the employee, and then and then when I click on complete the project, In DB isDeleted = true, and this entity has @Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;
    @ManyToOne
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;


}
