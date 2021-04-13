package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    /**
     * 주문
     */

    //하나의 트랜잭션안에서 member, item, order를 다 처리하는게 안전/깔끔하다.
    //식별자만 넘기는것도 좋은 방법이다.
    @Transactional
    public Long order(Long memberId, Long itmeId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itmeId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //객체를 new로 생성해서 세터로 값을 대입하는걸 좋아하는 사람이 있다.
//        OrderItem orderItem1 = new OrderItem(); -> 이렇게 하면 나중에 스타일이 혼재가 되어서 헷깔리므로, 강제적으로 new를 못하게 OrderItem에 protect생성자를 만들어 준다.
        //아니면 롬복을 써서 깔끔하게 처리도 가능하다. -> @NoArgsConstructor(access = AccessLevel.PROTECTED)


        //주문 생성
        // Cascade덕분에 OrderItem, Delivery가 한꺼번에 persist가 된다.
        // 다만 남발해서는 안된다. 이번에는 Order가 OrderItem, Delivery랑 라이프사이클이 같기에 해준것이고, 라이프 사이클이 다를 경우에는 persist를 따로따로 해줘야 안전하다.
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */

    @Transactional //더티체킹을 가능하게 한다. 이걸로 인해, em.commit(), em.save()를 안해줘도 된다.
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }


    /**
     * 검색
     */

    //JPA에서 동적 쿼리를 어떻게 해야 날릴수 있는지에 대한 고민을 할수있는 메서드.
    public List<Order> findOrders(OrderSearch orderSearch) {
        //return orderRepository.findAll(orderSearch);
        return orderRepository.findAllByCriteria(orderSearch);
    }


}
