package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    // 관리자 대시보드
    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    // 전체 사용자 목록
    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    // 사용자에게 관리자 권한 부여
    @PostMapping("/users/{userId}/grant-admin")
    public String grantAdminRole(@PathVariable Long userId, 
                                RedirectAttributes redirectAttributes) {
        try {
            userService.grantAdminRole(userId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "관리자 권한이 성공적으로 부여되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "권한 부여 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}