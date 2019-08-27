package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Member;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IMemberRepository;
import com.bcauction.infrastructure.repository.factory.MemberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository implements IMemberRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Member> selectMemberList() {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM member ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> MemberFactory.생성(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member selectMemberById(long id) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM member WHERE mem_id=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { id }, (rs, rowNum) -> MemberFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member selectMemberByMail(final String mail)
    {
        StringBuilder sbSql = new StringBuilder("SELECT * FROM member WHERE mem_mailc=?");
        try{
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                                                    new Object[] { mail }, (rs, rowNum) -> MemberFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }


    @Override
    public long insertMember(Member member) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("mem_name", member.getMem_name());
            paramMap.put("mem_mail", member.getMem_mail());
            paramMap.put("mem_registdate", LocalDateTime.now());
            paramMap.put("mem_pass", member.getMem_pass());

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("member")
                    .usingGeneratedKeyColumns("mem_id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int updateMember(Member member) {
        StringBuilder sbSql =  new StringBuilder("UPDATE member ");
        sbSql.append("SET mem_name =?, mem_mail=?, mem_pass=? ");
        sbSql.append("WHERE mem_id=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { member.getMem_name(), member.getMem_mail(), member.getMem_pass(), member.getMem_id() });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int deleteMember(long id) {
        StringBuilder sbSql =  new StringBuilder("DELETE FROM member ");
        sbSql.append("WHERE mem_idid=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { id });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
