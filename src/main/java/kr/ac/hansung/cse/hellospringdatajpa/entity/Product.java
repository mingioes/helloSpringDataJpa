package kr.ac.hansung.cse.hellospringdatajpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "상품명을 입력해주세요")
    @Size(min = 1, max = 100, message = "상품명은 1자 이상 100자 이하로 입력해주세요")
    private String name;
    
    @NotBlank(message = "브랜드를 입력해주세요")
    @Size(min = 1, max = 50, message = "브랜드는 1자 이상 50자 이하로 입력해주세요")
    private String brand;
    
    @NotBlank(message = "제조국가를 입력해주세요")
    @Size(min = 1, max = 50, message = "제조국가는 1자 이상 50자 이하로 입력해주세요")
    private String madeIn;
    
    @NotNull(message = "가격을 입력해주세요")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다")
    private double price;

    // 추가 생성자
    public Product(String name, String brand, String madeIn, double price) {
        this.name = name;
        this.brand = brand;
        this.madeIn = madeIn;
        this.price = price;
    }
}