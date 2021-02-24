package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Book") // single 테이블 구분자
@Data
public class Book extends Item{

    private String writer;
    private String ispn;
}
