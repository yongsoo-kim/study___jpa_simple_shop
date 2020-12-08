package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    /**
    @Autowired
    MemberRepository memberRepository;

    @Test
    // If Springboottest has @Transactional, this will rollback the DB after tests.
    //But you can cancel DB roll back by using @Rollback(false) annotation like this.
    //@Rollback(value = false)
    @Transactional
    void testMember() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        // then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        //같은 영속성컨텍스트 안에서는 ID같이 같으면 같은 엔터티로 식별한다.
        System.out.println("findMember == member:" + (findMember == member));

    }
**/

}