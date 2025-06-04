package kr.ac.hansung.cse.hellospringdatajpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용을 위해 필요
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // 정적 리소스와 공개 페이지는 모든 사용자에게 허용
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                
                // 관리자 페이지는 ADMIN 권한만 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 상품 관리 기능은 ADMIN 권한만 접근 가능
                .requestMatchers("/products/new", "/products/edit/**", "/products/save", "/products/delete/**").hasRole("ADMIN")
                
                // 상품 목록 조회는 인증된 사용자 모두 접근 가능
                .requestMatchers("/products", "/products/").authenticated()
                
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                    // 커스텀 로그인 페이지
                .defaultSuccessUrl("/products?login=success", true)   // 로그인 성공 후 이동할 페이지
                .failureUrl("/login?error=true")        // 로그인 실패 시 이동할 페이지
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")                   // 로그아웃 URL
                .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 이동할 페이지
                .invalidateHttpSession(true)           // 세션 무효화
                .deleteCookies("JSESSIONID")           // 쿠키 삭제
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)                     // 동시 세션 수 제한
                .maxSessionsPreventsLogin(false)        // 새 로그인 시 기존 세션 만료
            );

        return http.build();
    }
}