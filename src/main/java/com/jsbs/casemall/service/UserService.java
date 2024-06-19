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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다 : " + username));
        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPw())
                .roles(user.getRole().toString())
                .build();
    }

    public void save(AddUserRequest dto) {
        userRepository.save(Users.builder()
                .email(dto.getEmail())
                .userPw(passwordEncoder.encode(dto.getPassword()))
                .build());
    }

    public void JoinUser(UserDto userDTO) {
        Optional<Users> existingUser = userRepository.findById(userDTO.getUserId());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }

        Users user = Users.createMember(userDTO, passwordEncoder);
        userRepository.save(user);
    }

    public boolean checkUserIdExists(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public Users getUserByEmailAndPhoneNumber(String email, String phone) {
        return userRepository.findByEmailAndPhone(email, phone).orElseThrow(EntityNotFoundException::new);
    }

    public void userCheck(UserPwRequestDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("사용자의 이메일이 설정되지 않았습니다.");
        }

        requestDto.setUserEmail(user.getEmail());
        sendEmail(requestDto);
    }

    public void sendEmail(UserPwRequestDto requestDto) {
        if (requestDto.getUserEmail() == null || requestDto.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty.");
        }

        MailDto dto = sendService.createMailAndChargePassword(requestDto);
        sendService.mailSend(dto);
    }

    public UserEditDto getUserById(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserEditDto(user);
    }

    @Transactional
    public boolean updateUser(UserEditDto userEditDto) {
        try {
            log.info("수정한값 : {} ", userEditDto);
            Users user = userRepository.findById(userEditDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
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
