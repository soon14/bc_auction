package com.bcauction.application.impl;

import com.bcauction.application.IMemberService;
import com.bcauction.domain.Member;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService implements IMemberService {

    private IMemberRepository memberRepository;

    @Autowired
    public MemberService(IMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public List<Member> 목록조회() {
        return this.memberRepository.selectMemberList();
    }

    @Override
    public Member 조회(long id) {
        return this.memberRepository.selectMemberById(id);
    }

    @Override
    public Member 조회(String 이메일) { return this.memberRepository.selectMemberByMail(이메일); }

    @Override
    public Member 추가(Member 회원) {
        long id = this.memberRepository.insertMember(회원);
        return this.memberRepository.selectMemberById(id);
    }

    @Override
    public Member 수정(Member member) {

        Member found = this.memberRepository.selectMemberByMail(member.getMem_mail());
        if(found == null)
            throw new ApplicationException("회원 정보를 찾을 수 없습니다.");

        if(member.getMem_id() == 0)
            member.setMem_id(found.getMem_id());
        if(member.getMem_name() == null)
            member.setMem_name(found.getMem_name());
        if(member.getMem_pass() == null)
            member.setMem_pass(found.getMem_pass());

        int affected = this.memberRepository.updateMember(member);
        if(affected == 0)
            throw new ApplicationException("작품정보수정 처리가 반영되지 않았습니다.");

        return this.memberRepository.selectMemberById(member.getMem_id());
    }

    @Override
    public void 삭제(long id) {
        this.memberRepository.deleteMember(id);
    }
}
