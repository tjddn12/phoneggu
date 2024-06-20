package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Users;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

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
    private String loadAddr;
    private String lotAddr;
    private String detailAddr;
    private String extraAddr; // 추가된 필드
    private String email;

    public UserEditDto(Users user) {
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.name = user.getName();
        this.pCode = user.getPCode();
        this.phone = user.getPhone();
        this.loadAddr = user.getLoadAddr();
        this.lotAddr = user.getLotAddr();
        this.detailAddr = user.getDetailAddr();
        this.extraAddr = user.getExtraAddr(); // 추가된 필드 초기화
        this.email = user.getEmail();
    }
}
