package com.bcauction.domain.repository;

import com.bcauction.domain.Member;

import java.util.List;

public interface IMemberRepository {
    List<Member> selectMemberList();
    Member selectMemberById(long id);
    Member selectMemberByMail(String 이메일);
    long insertMember(Member 회원);
    int updateMember(Member 회원);
    int deleteMember(long id);
}
