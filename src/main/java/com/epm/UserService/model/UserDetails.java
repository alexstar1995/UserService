package com.epm.UserService.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    @Id
    @Column("detail_id")
    private Long detailId;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("age")
    private Integer age;

    @Column("gender")
    private String gender;

    @Column("user_id")
    private Long userId;
}