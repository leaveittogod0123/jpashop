package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 객체 생성시 생성자 메서드를 사용하게끔 제약하는 방법으로 코딩하는게 유지보수에 편함
public class Order {

    @Id @GeneratedValue
    @Column( name = "order_id") // DB 컬럼
    private Long id;

    /**
     * FK는 하나, JPA입장에서는 예를 기준으로 member, order중 어떤놈이 FK를 업데이트 쳐야하는지에 대해 모르니까
     * 객체에서는 order, member 변경포인트가 2개인데, Table에는 FK 하나  그래서 둘 중 하나를 연관관계 주인이란 개념으로 잡는다.
     * member값이 변경되면 FK값을 업데이트
     */
    @ManyToOne(fetch = FetchType.LAZY)  // Member 1:N Order
    @JoinColumn(name ="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // <- CascadeType.ALL order를 persist하면 orderItem 컬렉션도 다 persist
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 1:1매핑의 경우 주로 ACCESS가 많은 곳에 연관관계 주인을 둬요.
     * Order -> Delivery는 참조를 많이하는데
     * Delivery -> Order로 참조하는 일은 거의 없으니
     * Order가 연관관계 주인 역할을 하게 됌
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // <- CascadeType.ALL order를 persist하면 orderItem 컬렉션도 다 persist
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간
    // SpringPhysicalNamingStrategy Java8부터는 Hibernate가 자동으로 뭘 해줌?

    @Enumerated(EnumType.STRING) // Default가 ORDINAL 가독성 최악.. 종갼에 값 추가되면;; 꼭 STRING으로 해야 문제없겠죠.
    private OrderStatus status; // 주문의 상태 [ORDER, CANCEL]


    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    //
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /**
     * 생성 메서드
     * 여러 테이블에 데이터를 수정해야하는 경우에는 별도의 생성 메서드를 만들어서 쓰는게 관리하기 좋아요.
     * 주문 생성 비즈니스 로직이 변경되면 여기만 보면 되게 때문에
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for ( OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 비즈니스 로직
     *
     * 주문 취소
     */

    public void cancel() {
        if( delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for( OrderItem orderItem: orderItems) {
            orderItem.cancel();
        }
    }

    /**
     * 조회 로직
     *
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
//        int totalPrice = 0;
//        for( OrderItem orderItem: orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;

        //stream + lambda이용
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }
}
