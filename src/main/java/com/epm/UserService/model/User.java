package com.epm.UserService.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Table("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column("user_id")
    private Long userId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("email")
    private String email;

    @Column("created_date")
    private Date createdDate;

    @Column("updated_date")
    private Date updatedDate;

    private UserDetails userDetails;
}