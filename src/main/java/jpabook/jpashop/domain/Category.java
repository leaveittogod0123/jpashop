package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /**
     * 실습상 다대다 연관관계 매핑을 해봄. 연관테이블을 지정해줘야해요.
     * 실무에서 쓰이지 않는게 연관관계 테이블에 필드 추가가 어려워요.
     */
    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")) // RDB는 컬렉션 관계를 양쪽에 둘 수 없기 때문에 일대다 다대일로 풀어내야한다.
    private List<Item> items = new ArrayList<>();


    /**
     * 카테고리 구조 어떻게 하지?
     * 계층 구조로 쭈루룩 내려가잖아요.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;


    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    // Null 익셉션에도 안전
    // 하이버네이트가 엔티티를 영속화 할 때, 컬렉션을 감싸서 하이버네이트가  제공하는 내장 컬렉션으로 변경한다.
    // 이 컬렉션을 바꾸지 않아야 하이버네이트가 원하는 메커니즘대로 동작함.


    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
