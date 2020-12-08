package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    //Enum 타입은 반드시@Enumerated 를 쓰되,
    // default 값이 숫자형인 ORDINAL이므로 반드시 STRING 타입으로 바꿔주어야 변화가 일어났을때 대처가 가능하다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}
