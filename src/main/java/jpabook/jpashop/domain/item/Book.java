package jpabook.jpashop.domain.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Book") // single 테이블 구분자
@Data
@EqualsAndHashCode(callSuper=false)
public class Book extends Item{

    private String writer;
    private String ispn;
}
