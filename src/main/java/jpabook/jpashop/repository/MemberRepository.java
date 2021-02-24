package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        /**
         * 영속성 컨텍스트에 member 객체를 넣죠.
         * 나중에 transaction이 커밋되는 시점에 DB에 반영이 되겠죠. DB에 insert 쿼리가 날라갑니다.
         */
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        /**
         * 단건 조회
         * (객체타입, PK)
         */
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        /**
         * JPQL
         * from 의 대상이 entity
         */
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

    }
}
