package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.UserDTO;
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

    private final PasswordEncoder passwordEncoder; // 저장할떄 passwordEncoder.encode(넘어온비밀번호)

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 권한부여
        Users user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        return User.builder().username(user.getUserId()).password(user.getUserPw()).roles(user.getRole().toString()).build();

    }

    // 세이브

    public void JoinUser(UserDTO userDTO){
        Users user = Users.createMember(userDTO,passwordEncoder);
        userRepository.save(user);
    }

}
