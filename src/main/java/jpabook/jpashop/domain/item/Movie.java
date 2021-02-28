package jpabook.jpashop.domain.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Movie")
@Data
@EqualsAndHashCode(callSuper=false)
public class Movie extends Item {

    private String director;
    private String actor;

}
