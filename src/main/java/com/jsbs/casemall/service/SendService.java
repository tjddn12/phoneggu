package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.MailDto;
import com.jsbs.casemall.dto.UserPwRequestDto;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class    SendService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ksh020407789@gmail.com";


    public MailDto createMailAndChargePassword(UserPwRequestDto requestDto) {
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(requestDto.getUserEmail());
        dto.setTitle(requestDto.getUserName() + "님의 임시 비밀번호 안내 이메일 입니다.");
        dto.setContent("안녕하세요. 폰꾸입니다. " + requestDto.getUserName() + "님, 임시 비밀번호 안내 관련 메일 입니다." + "[" + requestDto.getUserName() + "]" + "님의 임시 비밀번호는 "
                + str + " 입니다. " +
                "임시 비밀번호로 로그인 후 마이페이지 [정보수정]에서 비밀번호를 변경해 주세요."); // 임시 비밀번호
        updatePassword(str, requestDto);
        return dto;
    }


    @Transactional
    public void updatePassword(String str, UserPwRequestDto requestDto) {
        String pw = passwordEncoder.encode(str);
       // log.info("암호화 비번 : {} ", pw);
        Users users = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        users.setUserPw(pw);
      //  log.info("업데이트된 내용 : {} ", users);
        userRepository.save(users);
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }


    public void mailSend(MailDto mailDto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());

        mailSender.send(message);
    }
}