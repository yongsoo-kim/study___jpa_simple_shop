package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

//Loading type은 항상 LAZY 로 지정한다. EAGER로 할시 예측하기 힘든 문제가 발생할수 있다.
//특히 JQPL같은 기술과 함께 사용할때, N+1같은 문제가 발생하기쉽다.
//OneToOne과 ManyToOne은 EAGER가 기본이므로, 특히나 주의하고 LAZY로 바꾸도록 한다.

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생으로 new 로 생성금지한다. static메서드 써라는 의미. 코드는 항상 제약하는 스타일로 짜는게 좋다.(일관성.유지보수 용이)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //Cascade를 안했을시 -> OrderItem1,OrderItem2, Order를 persist해야함 -> 귀찮음
    //Cascade를 했을시 ->  Order안에 orderItems를 넣고 한번에 persist가능.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //1대1관계에서는 외래키가 어디에있든 상관없지만, 주로 자주 액서스하는 클래스에 두는것이 편하다.
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") //이로인해 연관관계의 주인이되고, 외래키가 이 클래스에서 생성 관리 됨
    private Delivery delivery;

    //SpringPhysicalNamingStrategy에 의해, 테이블 생성시의 테이블명이 필드와 다르다는것을 기억한다.
    // 1. 카멜케이스 -> 언더스코어
    // 2. 점 -> 언더스코어
    // 3. 대문자 -> 소문자
    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 [ORDER, CANCEL]

    //==연관 관계 편의 메서드==//

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비지니스 로직 ==//
    /**
    * 주문취소
    */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직 ==//

    /**
     *
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem: orderItems) {
            totalPrice+= orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}
