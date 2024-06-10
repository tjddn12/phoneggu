package com.jsbs.casemall.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class UserPwRequestDto {

    private String userName;

    private String userEmail;

    private String userId;
}
