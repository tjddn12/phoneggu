package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table (name="member")
@Entity
@ToString
public class Member {


    @Id
    @Column(name = "user_id")
    private  String userId;  // 유저아이디

    @Column(name ="name",nullable = false)
    private String name; //이름


    @Column(name ="user_pw",nullable = false)
    private String userPw; //비밀번호

    @Column(name ="email",nullable = false)
    private String email; //이메일


    @Enumerated(EnumType.STRING)
    private Role role;

//    @Column(name ="phone",nullable = false)
//    private int phone; //전화번호
//
//    @Column(name ="p_code",nullable = false)
//    private String pCode; // 우편주소
//
//    @Column(name ="loadAddr",nullable = false)
//    private String loadAddr; // 도로명 주소
//
//    @Column(name ="lotAddr",nullable = false)
//    private String lotAddr; // 지번주소
//
//    @Column(name ="detailAddr",nullable = false)
//    private String detailAddr; // 우편주소



}
