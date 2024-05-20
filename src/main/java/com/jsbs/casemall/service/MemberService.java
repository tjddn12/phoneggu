package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.MemberDto;
import com.jsbs.casemall.entity.Member;
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
public class MemberService implements UserDetailsService {

    private  final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userRepository.findById(username).orElseThrow(EntityNotFoundException :: new);
        return User.builder().username(member.getUserId()).password(member.getUserPw()).roles(member.getRole().toString()).build();

    }

    public void saveMember(MemberDto dto, String password){




    }


}
