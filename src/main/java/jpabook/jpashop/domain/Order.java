package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 1:1매핑의 경우 주로 ACCESS가 많은 곳에 연관관계 주인을 둬요.
     * Order -> Delivery는 참조를 많이하는데
     * Delivery -> Order로 참조하는 일은 거의 없으니
     * Order가 연관관계 주인 역할을 하게 됌
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
}
