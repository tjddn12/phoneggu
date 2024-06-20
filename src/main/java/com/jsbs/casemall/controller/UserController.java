package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.AddUserRequest;
import com.jsbs.casemall.dto.UserDto;
import com.jsbs.casemall.dto.UserEditDto;
import com.jsbs.casemall.dto.UserPwRequestDto;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/join")
    public String saveForm() {
        return "user/join";
    }

    @PostMapping("/join")
    public String userForm(@Valid @ModelAttribute UserDto userDTO,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        System.out.println("userDTO" + userDTO);
        userService.JoinUser(userDTO);
        return "redirect:/login";
    }

    // 아이디 찾기 페이지
    @GetMapping("/findUser")
    public String findForm() {
        return "user/findUser";
    }

    @GetMapping("/findId")
    public String showFindIdPage() {
        return "user/findId";
    }

    @PostMapping("/findUser")
    public String findUserByEmailAndPhone(@RequestParam String email, @RequestParam String phone, Model model) {
        try {
            Users user = userService.getUserByEmailAndPhoneNumber(email, phone);
            model.addAttribute("user", user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "User not found");
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        return "user/findId";
    }

    // 비밀번호 변경
    @GetMapping("/findPw")
    public String findPwForm() {
        return "user/findPw";
    }

    @PostMapping("/findPw")
    public String pwFind(UserPwRequestDto requestDto) {
        userService.userCheck(requestDto);
        return "user/EditPw";
    }

    // 아이디 중복
    @GetMapping("/checkUserId")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam String userId) {
        boolean exists = userService.checkUserIdExists(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage() {
        return "user/myPage";
    }

    // 정보 수정
    @GetMapping("/userEdit")
    public String showEditForm(Principal principal, Model model) {
        String id = principal.getName();
        log.info("아이디 값 : {}", id);
        UserEditDto dto = userService.getUserById(id);
        log.info(dto.toString());

        // 소셜 로그인 여부 확인
        boolean isSocialLogin = principal instanceof OAuth2AuthenticationToken;
        model.addAttribute("isSocialLogin", isSocialLogin);
        model.addAttribute("user", dto);

        return "user/userEdit";
    }

    @PostMapping("/userEdit")
    public String updateUser(@Valid @ModelAttribute("user") UserEditDto userEditDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/userEdit";
        }

        boolean isUpdated = userService.updateUser(userEditDto);
        if (isUpdated) {
            model.addAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        } else {
            model.addAttribute("message", "회원 정보 수정에 실패했습니다. 다시 시도해주세요.");
        }
        return "redirect:/myPage";
    }


    @PostMapping("/user")
    public String signup(@Valid AddUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup";
        }
        try {
            log.info("리퀘스트 {} ", request);
            userService.save(request);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "user/userLogin";
    }

    @GetMapping("/signup")
    public String signup(AddUserRequest addUserRequest) {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    // 소셜 로그인 성공시 정보수정한 사람과 아닌 사람 구분
    @GetMapping("/loginSuccess")
    public String loginSuccess(Principal principal) throws Exception {
        String userid = principal.getName();
        log.info("소셜 아이디 존재 여부 : {} ",userid);
        boolean edit = userService.isProfileComplete(userid);
        log.info("리턴값 : {} ",edit);
        if(edit){
            // true 반환시 메인 페이지로
            return "redirect:/";
        }else{
            // false 회원수정 페이지로
            return "redirect:userEdit";
        }

    }



}
