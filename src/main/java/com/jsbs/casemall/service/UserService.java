package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.dto.*;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.entity.OrderDetail;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.OrderRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SendService sendService;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

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

    @Transactional
    public void save(AddUserRequest dto) {
        userRepository.save(Users.builder()
                .email(dto.getEmail())
                .userPw(passwordEncoder.encode(dto.getPassword()))
                .build());
    }
    @Transactional
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
            if (userEditDto.getUserPw() != null && !userEditDto.getUserPw().isEmpty()) {
                user.setUserPw(passwordEncoder.encode(userEditDto.getUserPw()));
            }
            user.setPCode(userEditDto.getPCode());
            user.setLoadAddr(userEditDto.getLoadAddr());
            user.setLotAddr(userEditDto.getLotAddr());
            user.setDetailAddr(userEditDto.getDetailAddr());
            user.setExtraAddr(userEditDto.getExtraAddr());
            user.setPhone(userEditDto.getPhone());
            user.setEmail(userEditDto.getEmail());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error updating user: ", e);
            return false;
        }
    }


    // 소셜 로그인 시도시 회원정보가 비어있는지 없는지 체크
    public boolean isProfileComplete(String userid) {
        Users user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 검사 항목들
        String pCode = user.getPCode();
        String loadAddr = user.getLoadAddr();
        String lotAddr = user.getLotAddr();
        String detailAddr = user.getDetailAddr();

        List<String> fieldCheckList = Arrays.asList(pCode, loadAddr, lotAddr, detailAddr);

        return fieldCheckList.stream().allMatch(this::isFieldValid);
    }
    // 각 필드 검사
    private boolean isFieldValid(String field) {
        return field != null && !field.isEmpty();
    }


    @Transactional(readOnly = true)
    public MypageDto myPageCompleteCount(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("찾는 유저가 없습니다"));
        List<Order> orders = orderRepository.findByUsersAndOrderStatus(user, OrderStatus.ORDER);
        MypageDto result = new MypageDto();
        int count = 0;
        int total = 0;
        // 주문 완료된 주문들을 순회 하면서
        // 각각 아이템들의 가격을 더해서 dto 로 반환
        for (Order order : orders) {
            for(OrderDetail orderDetail : order.getOrderItems()){
                total += orderDetail.getTotalPrice(); // 각각 아이템의 총 금액
            }
            count++; // order 순회 횟수  =  주문횟수
        }


//        주문횟수   count
//        토탈 금액  totalPay

        return result;
    }




}
