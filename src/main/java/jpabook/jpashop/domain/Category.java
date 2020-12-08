package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //다대다는 1대다 다대1 로 풀어내는 중간테이블(JoinTable)을 설정해줄필요가 있다.
    //실무에서는 쓰지말것. 이유는 복잡한 세팅이 불가능해지니까.
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();


    //Category에대한 자기자신의 매핑도, 아예다른 클래스에 매핑한다고 생각하고 정하면 편하다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 메서드 ==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
