package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.UserDto;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;



    private final PasswordEncoder passwordEncoder; // 저장할떄 passwordEncoder.encode(넘어온비밀번호)

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 권한부여
        Users user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        return User.builder().username(user.getUserId()).password(user.getUserPw()).roles(user.getRole().toString()).build();

    }

    // 세이브

    public void JoinUser(UserDto userDTO){
        Users user = Users.createMember(userDTO,passwordEncoder);
        userRepository.save(user);
    }

    // 아이디 찾기



    public Users getUserByEmailAndPhoneNumber(String email, String phone) {
         return userRepository.findByEmailAndPhone(email,phone).orElseThrow(EntityNotFoundException::new); // 없으면 예외 발생시킴

    }

}
