package com.jsbs.casemall.entity;


import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@Table(name ="users")
@NoArgsConstructor
public class Users {


    @Id
    @Column(name = "userId")
    private String userId; // 유저 아이디

    @Column(name = "name", nullable = false) // 필수 입력 사항
    private String name; // 이름

    @Column(name = "userPw", nullable = false)
    private String userPw; // 비밀번호

    @Column(name = "email", nullable = false)
    private String email; // 이메일

    @Column(name = "phone", nullable = false)
    private String phone; // 전화번호

    @Column(name = "p_code", nullable = false)
    private String pCode; // 우편 주소

    @Column(name = "loadAddr", nullable = false)
    private String loadAddr; // 도로명 주소

    @Column(name = "lotAddr", nullable = false)
    private String lotAddr; // 지번 주소

    @Column(name = "detailAddr", nullable = false)
    private String detailAddr; // 우편 주소

//    권한추가
    @Enumerated(EnumType.STRING)
    private Role role;


    public static Users createMember(UserDto userDto,
                                     PasswordEncoder passwordEncoder){
        Users users = new Users();
        users.setUserId(userDto.getUserId());
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPhone(userDto.getPhone());
        users.setPCode(userDto.getP_code());
        users.setLoadAddr(userDto.getLoadAddr());
        users.setLotAddr(userDto.getLotAddr());
        users.setDetailAddr(userDto.getDetailAddr());
        String password = passwordEncoder.encode(userDto.getUserPw());
        users.setUserPw(password);
        users.setRole(Role.ADMIN);
        return users;
    }

}
