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
public class UserDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message="비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String userPw;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String p_code;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String loadAddr;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String lotAddr;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String detailAddr;

    private String extraAddr; // 추가된 필드

    public UserDto(Users user) {
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.name = user.getName();
        this.p_code = user.getPCode();
        this.phone = user.getPhone();
        this.loadAddr = user.getLoadAddr();
        this.lotAddr = user.getLotAddr();
        this.detailAddr = user.getDetailAddr();
        this.extraAddr = user.getExtraAddr(); // 추가된 필드 초기화
        this.email = user.getEmail();
    }
}
