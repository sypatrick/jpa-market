package com.jpamarket.service;

import com.jpamarket.domain.Member;
import com.jpamarket.repository.MemberRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    // 쿼리 확인하고 싶을 때 엔티티매니저를 통해서 flush까지 진행
    @Autowired
    EntityManager em;

    @Test
    public void join() throws Exception{
        //given
        Member member = new Member();
        member.setName("amy");
        //when
        Long savedId = memberService.join(member);
        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test
    public void joinException() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("amy1");
        Member member2 = new Member();
        member2.setName("amy1");

        //when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

    }
}