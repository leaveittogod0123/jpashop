package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // TEST 후 롤백
public class MemberServiceTest {

    @Autowired // SpringBootTest 없으면 Autowired도 안됩니다.
    private  MemberService memberService;

    @Autowired
    private  MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given

        Member member = new Member();
        member.setName("kim");
        // when

        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId)); // JPA에서 같은 트랙잭션 안에서 PK가 같은 엔티티 참조하면 같은 영속성 컨텍스트 참조
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        // when

        memberService.join(member);
        memberService.join(member2); // 예외가 발생해야한다.
        // then

        fail("에외가 발생해야 한다.");
    }

}
