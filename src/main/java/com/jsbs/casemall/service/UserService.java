package com.jsbs.casemall.service;

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

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public void test(){
        int s = 10;


        String ss;
    }

    private final PasswordEncoder passwordEncoder; // 저장할떄 passwordEncoder.encode(넘어온비밀번호)

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        return User.builder().username(user.getUserId()).password(user.getUserPw()).roles(user.getRole().toString()).build();

    }

}
