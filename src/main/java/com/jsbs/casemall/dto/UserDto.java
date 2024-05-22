package com.jsbs.casemall.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private String userId;
    private String name;
    private String userPw;
    private String email;
    private int phone;
    private String p_code;
    private String loadAddr;
    private String lotAddr;
    private String detailAddr;



}
