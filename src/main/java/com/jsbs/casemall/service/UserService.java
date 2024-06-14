package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.*;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final SendService sendService;

    private final PasswordEncoder passwordEncoder;// 저장할떄 passwordEncoder.encode(넘어온비밀번호)

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 권한부여
        Users user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        return User.builder().username(user.getUserId()).password(user.getUserPw()).roles(user.getRole().toString()).build();

    }

    public Long save(AddUserRequest dto) {

        return userRepository.save(Users.builder()
                .email(dto.getEmail())
                .userPw(passwordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }


    //세이브

    public void JoinUser(UserDto userDTO) {
        // Check for duplicate userId
        Optional<Users> existingUser = userRepository.findByUserId(userDTO.getUserId());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }

        Users user = Users.createMember(userDTO, passwordEncoder);
        userRepository.save(user);
    }
    // 아이디 중복
    public boolean checkUserIdExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }


    // 아이디 찾기
    public Users getUserByEmailAndPhoneNumber(String email, String phone) {
        return userRepository.findByEmailAndPhone(email,phone).orElseThrow(EntityNotFoundException::new); // 없으면 예외 발생시킴

    }


    // 비밀번호 찾기

    public void userCheck(UserPwRequestDto requestDto) {
        Users user = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(()->new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

            sendEmail(requestDto);

    }


    public void sendEmail(UserPwRequestDto requestDto) {
        MailDto dto =  sendService.createMailAndChargePassword(requestDto);
        sendService.mailSend(dto);
    }


    // 정보 수정

    public  UserEditDto getUserById(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new  UserEditDto(user);
    }


    @Transactional
    public boolean updateUser(UserEditDto userEditDto) {

        try {
            log.info("수정한값 : {} ",userEditDto);
            Users user = userRepository.findById(userEditDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            user.setUserPw(passwordEncoder.encode(userEditDto.getUserPw()));
            user.setPCode(userEditDto.getPCode());
            user.setLoadAddr(userEditDto.getLoadAddr());
            user.setLotAddr(userEditDto.getLotAddr());
            user.setDetailAddr(userEditDto.getDetailAddr());
            user.setPhone(userEditDto.getPhone());
            user.setEmail(userEditDto.getEmail());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    }

