package com.inje.forseni.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String membershipId;
    private String userName;
    private Integer age;
    private String password;
}
