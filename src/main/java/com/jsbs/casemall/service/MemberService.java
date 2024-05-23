package com.jsbs.casemall.service;


import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService  implements UserDetailsService {
    private final UserRepository userRepository;

    public Users saveMember(Users users){
        validateDuplicateMember(users);
        return userRepository.save(users);
    }

    private void validateDuplicateMember(Users users) {
        Users findMember = userRepository.findByEmail(users.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users =  userRepository.findByEmail(email);
        if(users == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(users.getEmail())
                .password(users.getUserPw())
                .roles(users.getRole().toString())
                .build()
                ;
    }
}
