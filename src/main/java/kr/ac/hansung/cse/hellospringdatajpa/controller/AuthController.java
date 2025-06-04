package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.dto.UserRegistrationDto;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 홈 페이지
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "성공적으로 로그아웃되었습니다.");
        }
        
        return "auth/login";
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegistrationDto userDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        // 유효성 검사 실패
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        
        // 비밀번호 일치 검사
        if (!userDto.isPasswordMatching()) {
            model.addAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
            return "auth/register";
        }
        
        // 이메일 중복 검사
        if (userService.isEmailExists(userDto.getEmail())) {
            model.addAttribute("emailError", "이미 사용중인 이메일입니다.");
            return "auth/register";
        }
        
        try {
            // 사용자 등록
            userService.registerUser(userDto.getEmail(), userDto.getPassword(), userDto.getName());
            redirectAttributes.addFlashAttribute("successMessage", 
                "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
            return "auth/register";
        }
    }
}