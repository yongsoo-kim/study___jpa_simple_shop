package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    
    // Item 속성
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    // Book만의 속성
    private String author;
    private String isbn;

}
