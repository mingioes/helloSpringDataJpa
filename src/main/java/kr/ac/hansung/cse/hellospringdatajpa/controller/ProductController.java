package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Product;
import kr.ac.hansung.cse.hellospringdatajpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // 상품 목록 조회 - USER, ADMIN 모두 접근 가능
    @GetMapping({"", "/"})
public String viewHomePage(Model model, Principal principal,
                          @RequestParam(value = "login", required = false) String login) {
    List<Product> listProducts = service.listAll();
    model.addAttribute("listProducts", listProducts);
    
    // 현재 로그인한 사용자 정보 추가
    if (principal != null) {
        model.addAttribute("currentUser", principal.getName());
    }
    
    // 로그인 성공 메시지 처리
    if ("success".equals(login)) {
        model.addAttribute("successMessage", "성공적으로 로그인되었습니다!");
    }
    
    return "index";
}

    // 상품 등록 폼 - ADMIN만 접근 가능
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new_product";
    }

    // 상품 수정 폼 - ADMIN만 접근 가능
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditProductPage(@PathVariable(name = "id") Long id, Model model) {
        Product product = service.get(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    // 상품 등록/수정 처리 - ADMIN만 접근 가능
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            if (product.getId() != null) {
                return "edit_product";
            } else {
                return "new_product";
            }
        }
        
        // 가격 유효성 검사
        if (product.getPrice() < 0) {
            model.addAttribute("priceError", "가격은 0 이상이어야 합니다.");
            if (product.getId() != null) {
                return "edit_product";
            } else {
                return "new_product";
            }
        }
        
        try {
            service.save(product);
            if (product.getId() != null) {
                redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 등록되었습니다.");
            }
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 저장 중 오류가 발생했습니다: " + e.getMessage());
            if (product.getId() != null) {
                return "edit_product";
            } else {
                return "new_product";
            }
        }
    }

    // 상품 삭제 - ADMIN만 접근 가능
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "redirect:/products";
    }
}