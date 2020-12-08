package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded //값타입을 선언.
    private Address address;

    //컬렉션은 필드에서 초기화한다. 이로서 null문제에서 안전하다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
