package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.dto.UserDto;
import jakarta.persistence.*;
import lombok.Builder;
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

    private String username;

    @Id
    @Column(name = "userId")
    private String userId; // 유저 아이디

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "userPw", nullable = false)
    private String userPw;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "p_code", nullable = false)
    private String pCode;

    @Column(name = "loadAddr", nullable = false)
    private String loadAddr;

    @Column(name = "lotAddr", nullable = false)
    private String lotAddr;

    @Column(name = "detailAddr", nullable = false)
    private String detailAddr;

    @Column(name = "extraAddr", nullable = true)
    private String extraAddr; // 추가된 필드

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "social_id")
    private String socialId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Users(String userId, String name, String userPw, String email, String phone, String pCode,
                 String loadAddr, String lotAddr, String detailAddr, String extraAddr, String provider, String providerId, String socialId, Role role) {
        this.userId = userId;
        this.name = name;
        this.userPw = userPw;
        this.email = email;
        this.phone = phone;
        this.pCode = pCode;
        this.loadAddr = loadAddr;
        this.lotAddr = lotAddr;
        this.detailAddr = detailAddr;
        this.extraAddr = extraAddr;
        this.provider = provider;
        this.providerId = providerId;
        this.socialId = socialId;
        this.role = role;
    }

    public static Users createMember(UserDto userDto, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(userDto.getUserPw());
        return Users.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .pCode(userDto.getP_code())
                .loadAddr(userDto.getLoadAddr())
                .lotAddr(userDto.getLotAddr())
                .detailAddr(userDto.getDetailAddr())
                .extraAddr(userDto.getExtraAddr()) // 추가된 필드
                .userPw(encodedPassword)
                .role(Role.USER)
                .build();
    }
}
