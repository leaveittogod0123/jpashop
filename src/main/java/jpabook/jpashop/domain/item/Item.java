package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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

    /**
     * 비즈니스 로직
     * 재고를 늘리고 줄이는 로직
     *
     * 도메인 주도 설계
     * 엔티티 자체에서 해결할 수 있는 로직들은 엔티티안에 비즈니스 로직을 넣는게 좋아요.
     * 데이터를 갖고있는 객체에 비즈니스 로직이 위치하는게 제일 응집도가 높겠죠.
     *
     */

    /**
     * 재고 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if( restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
