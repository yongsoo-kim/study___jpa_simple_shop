package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
        //이게 있어야 롤백이 됨.
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;


    @Test
        //@Rollback(value = false)
    void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); //이걸로 영속성컨텍스트에 있는 데이터를 DB로 날리기 때문에 SQL이 로그상에 나오게 된다.
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test()
    void 중복회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when & then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2); //중복예외 발생!
        });


        // 구형 테스트케이스.
//        memberService.join(member1);
//        try {
//            memberService.join(member2); //중복예외 발생!
//        } catch (IllegalStateException e) {
//            return;
//        }
        //then
//        fail("예외가 발생해야 합니다");// 여기까지 오면 안됨!

    }

}