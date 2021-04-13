package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new Book());
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String create(BookForm form) {

        //이렇게 세터를 열어두는것은 좋지않다. Book클래스에 static함수등을 이용해 한번에 처리하든지...세터를 못쓰게 하는게 좋은 방법이지만 예제이므로 그대로 둔다.
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }


    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        //예제를 간단하게 하기 위해 무조건 Book만 받아온다고 가정한다.
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }


    //ModelAttribute("form")을 쓰는이유. Thymeleaf에서 폼을보낼때 오브젝트명이 "form"이라서. 맞춰줘야한다.
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
//        Book book = new Book();
//        book.setId(form.getId()); //ID가 세팅이 된다? -> 오브젝트는 새것이지만, 데이터 자체는 DB를 거쳐온것임. 따라서  준영속 상태임.
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        //EntityManager의 Merge를 부르는 메서드. 신규등록은 Persist. 업데이트는 Merge?
//        itemService.saveItem(book);

        //Merge(준영속)은 안쓰는게 답이다. 왜냐하면, 이녀석은 전체속성 변경을 하기에, 원치하는 속성 변경까지 일어날수 있기 때문이다.
        // 따라서 변경감지(더티체킹)으로 대응하는게 좋다.
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }

}
