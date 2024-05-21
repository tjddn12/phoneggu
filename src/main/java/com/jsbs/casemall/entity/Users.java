package com.jsbs.casemall.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name ="users")
@NoArgsConstructor
public class Users {


    @Id
    @Column(name = "user_id")
    private String userId; // 유저 아이디

    @Column(name = "name", nullable = false) // 필수 입력 사항
    private String name; // 이름

    @Column(name = "user_pw", nullable = false)
    private String user_pw; // 비밀번호

    @Column(name = "email", nullable = false)
    private String email; // 이메일

    @Column(name = "phone", nullable = false)
    private int phone; // 전화번호

    @Column(name = "p_code", nullable = false)
    private String pCode; // 우편 주소

    @Column(name = "loadAddr", nullable = false)
    private String loadAddr; // 도로명 주소

    @Column(name = "lotAddr", nullable = false)
    private String lotAddr; // 지번 주소

    @Column(name = "detailAddr", nullable = false)
    private String detailAddr; // 우편 주소
}
