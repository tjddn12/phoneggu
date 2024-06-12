package com.jsbs.casemall.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserEditDto {

        private String userId;
        private String userPw;
        private String name;
        private String pCode;
        private String phone;
        private String email;


    public  UserEditDto(String userId, String userPw, String name, String pCode, String phone, String email) {
    }
}
