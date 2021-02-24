package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // JPA에서 상속관계 전략 잡야아함.
@DiscriminatorColumn(name = "dtype")
@Data
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id") // 테이블의 경우 id로만 적어두면 가독성도 그렇고 명확히 어떤 엔티티의 식별자인지 알 수 있으니
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
