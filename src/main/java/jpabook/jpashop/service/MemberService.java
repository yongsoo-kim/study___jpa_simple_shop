package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//Javax에도 똑같은 이름의 어노테이션이 있으나, 스프링측의 것이 기능이 더 많다.
//읽기전용일때는 "readOnly = true" 이것을 붙이면 성능향상을 기대할수 있다.
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;


    //생성자 인젝션을 통해 얻는 장점. 요즘은 롬복으로 자동생성해주지만 정리를 해보자.
    //1.setter를 통한 섣부른 주입및 변화를 미연에 방지함.
    //2.Mock객체들을 주입하기 용이함.
    //3.final을 넣으면 컴파일 시점에서 주입실패를 알려준다.
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     * @param member
     * @return
     */
    //이것은 읽기전용이 아니다!
    @Transactional
    public Long join(Member member){
        
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복확인. 멀티쓰레드환경에서 적은 확률로 같은 이름을 가진사람이 동시에 등록할수도있으므로, DB상의 이름에 제약조건을 거는것이 좋을지도?
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조희
     * @return
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 1명 조회
     * @return
     */
    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
