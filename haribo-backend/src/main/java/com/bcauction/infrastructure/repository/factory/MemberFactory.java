package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberFactory {

    public static Member 생성(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        Member member = new Member();
        member.setMem_id(rs.getLong("mem_id"));
        member.setMem_name(rs.getString("mem_name"));
        member.setMem_mail(rs.getString("mem_mail"));
        member.setMem_pass(rs.getString("mem_pass"));
        member.setMem_registdate(rs.getTimestamp("mem_registdate").toLocalDateTime());

        return member;
    }
}
