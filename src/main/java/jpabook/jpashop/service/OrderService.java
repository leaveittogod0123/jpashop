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
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 예제를 간단히 하기 위해서 이렇게 씀 실제로는 입력한 주소를 받아야겠죠.

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // OrderItem, Delivery가 Cascade 옵션을 통해 자동으로 persist 됌
        // cascade의 범위는 ? order가 orderItem, delivery 를 관리함. 이런 구조에서만 쓰는게 좋음. order에 따라 orderItem, delivery값이 변경이 되고 persist 되는 라이프 사이클이 똑같은 도메인이기에 사용함
        // Delivery Order말고는 안쓰거든요.

        // 그에 반에 Delivery가 중요해서 여러 엔티티에서 참조해서 사용한다면 Cascade 막 쓰면 안됌.
        // order 지울때 delivery도 다 지워지고 또 cascade된 테이블데이터 또 다 지워질거기 때문에
        // 별도의 repository를 이용해서 각 repository에서 persist해야합니다!!
        // 잘 모를땐 쓰지말고 -> 다른 여러 엔티티에서 참조하지 않고, 같이 persist 하는 구조일때 리팩토링시 사용하는게 좋음
        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();

        /**
         * 일반적으로 Database SQL를 직접 다루는 mybatis, jdbctemplate, sql를 직접 날리면
         * 비즈니스 로직에서 data를 변경하면 -> update 쿼리를 직접 짜서 날려야죠.
         * order.cancel()시 주문 상태를 바꾸는 로직이 수행되는데 그렇게 해도 결국에는 update 쿼리에 업데이트 된 값을 최종적으로 넘겨줘야하죠.
         * transactional 스크립트라고 하는데 service 레벨에서 비즈니스 로직을 다 쓸 수 밖에 없어요.
         *
         * JPA는 데이터만 바꾸면 영속성 컨텍스트에 존재하는 엔티티들의 변경을 감지해서 데이터베이스에 업데이트 쿼리가 날라갈겁니다.
         */
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderSearch.findAll(orderSearch);
//    }

}
