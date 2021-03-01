package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service // Component scan에 대상이 되서 Spring bean으로 등록됩니다.
@Transactional(readOnly = true)
// JPA 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행되야하거든요.
public class MemberService {

    private final MemberRepository memberRepository; // final -> 컴파일 시점에서 바인딩 체크를 해줄 수 있기 때문

    /**
     * spring boot에서는 생성자가 하나만 있는 경우에는
     * Autowired 하지않아도 됩니다.
     * @param memberRepository
     */
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    @Transactional // class 레벨보다 우선순위 갖음
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
     // JPA가 조회하는 로직에서는 성능 최적화합니다.
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
