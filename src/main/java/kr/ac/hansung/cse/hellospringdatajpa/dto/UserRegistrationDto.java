package kr.ac.hansung.cse.hellospringdatajpa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    
    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요")
    private String name;
    
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String email;
    
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 6, max = 100, message = "비밀번호는 6자 이상 100자 이하로 입력해주세요")
    private String password;
    
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String confirmPassword;
    
    // 비밀번호 일치 검증
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}