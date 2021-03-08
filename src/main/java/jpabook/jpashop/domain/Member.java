package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Member {

    @Id
    @GeneratedValue // 기본적으로 persist할때 insert문 안나감, JPA에는 DB 트랜잭션 커밋될때 flush되면서 DB에 insert쿼리가 날라감
    @Column(name = "member_id") // DB 컬럼
    private Long id;

    private String name;

    @Embedded // Embeddable 예약어로된 내장타입 사용
    private Address address;

    @OneToMany(mappedBy = "member") // order 객체의 member필드에 의해 매핑되었고, 연관관계 주인은 member고, 그 연관관계의 매핑의 거울일 뿐
    private List<Order> orders = new ArrayList<>();
}
