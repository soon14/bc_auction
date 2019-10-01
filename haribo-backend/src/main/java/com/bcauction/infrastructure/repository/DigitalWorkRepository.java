package com.bcauction.infrastructure.repository;

import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.infrastructure.repository.factory.DigitalWorkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DigitalWorkRepository implements IDigitalWorkRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<DigitalWork> 목록조회() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM art WHERE art_isopen=? AND art_status=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", "Y"}, (rs, rowNum) -> DigitalWorkFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}


	@Override
	public List<DigitalWork> 사용자작품목록조회(final long 회원id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM art WHERE art_status=? AND art_mem=? ");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", 회원id}, (rs, rowNum) -> DigitalWorkFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork 조회(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM art WHERE art_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> DigitalWorkFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork 조회(final long 회원id, final String 이름) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM art WHERE art_mem=? AND art_name=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { 회원id, 이름 }, (rs, rowNum) -> DigitalWorkFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 추가(final DigitalWork art) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("art_name", art.getArt_name());
			paramMap.put("art_detail", art.getArt_detail());
			paramMap.put("art_isopen", art.getArt_isopen());
			paramMap.put("art_status", art.getArt_status());
			paramMap.put("art_mem", art.getArt_mem());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("art")
					.usingGeneratedKeyColumns("art_id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final DigitalWork art) {
		StringBuilder sbSql =  new StringBuilder("UPDATE art ");
		sbSql.append("SET art_name=?, art_detail=?, art_isopen=?, art_status=?, art_mem=? ");
		sbSql.append("where art_id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] {
										art.getArt_name() ,
										art.getArt_detail(),
										art.getArt_isopen() ,
										art.getArt_status(),
										art.getArt_mem(),
										art.getArt_id()
			                                });
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id) { // 상태를 N으로 업데이트
		StringBuilder sbSql =  new StringBuilder("UPDATE art ");
		sbSql.append("SET art_status=?, art_isopen=? ");
		sbSql.append("where art_id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "N", "N", id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}

	}

}
