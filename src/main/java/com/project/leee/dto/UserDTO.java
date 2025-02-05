package com.project.leee.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer uId;
    private String userId;
    private String userName;
    private Integer age;
    private String password;
    private Integer fontSize = 16;
}
