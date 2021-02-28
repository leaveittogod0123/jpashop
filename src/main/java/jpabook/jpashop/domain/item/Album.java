package jpabook.jpashop.domain.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Album")
@Data
@EqualsAndHashCode(callSuper=false)
public class Album extends Item{
    private String artist;
    private String etc;
}
