package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Users;
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

        public UserEditDto(Users user) {
            this.userId = user.getUserId();
            this.userPw = user.getUserPw();
            this.name = user.getName();
            this.pCode = user.getPCode();
            this.phone = user.getPhone();

        }

}
