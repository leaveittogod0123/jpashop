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
    @Column(name = "member_id") // DB 컬럼
    private Long id;

    private String name;

    @Embedded // Embeddable 예약어로된 내장타입 사용
    private Address address;

    @OneToMany(mappedBy = "member") // order 객체의 member필드에 의해 매핑되었고, 연관관계 주인은 member고, 그 연관관계의 매핑의 거울일 뿐
    private List<Order> orders = new ArrayList<>();

}
