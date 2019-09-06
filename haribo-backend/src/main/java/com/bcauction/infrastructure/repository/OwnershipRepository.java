package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IOwnershipRepository;
import com.bcauction.infrastructure.repository.factory.OwnershipFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OwnershipRepository implements IOwnershipRepository
{
	public static final Logger logger = LoggerFactory.getLogger(OwnershipRepository.class);

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Ownership> 목록조회()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM own");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{}, (rs, rowNum) -> OwnershipFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public List<Ownership> 소유자별목록조회(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM own WHERE own_id=?");
		return getOwnerships(id, sbSql);
	}

	@Override
	public List<Ownership> 작품별목록조회(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM own WHERE own_art=?");
		return getOwnerships(id, sbSql);
	}

	private List<Ownership> getOwnerships(final long id, final StringBuilder sbSql)
	{
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{id}, (rs, rowNum) -> OwnershipFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Ownership 조회(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM own WHERE own_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { (int)id }, (rs, rowNum) -> OwnershipFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Ownership 조회(final long 소유자id, final long 작품id)
	{
		logger.info("조회 (소유자id, 작품id) = (" + 소유자id + ", " + 작품id + ")");
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM own WHERE own_mem=? AND own_art=?");
		try {
			Ownership o = this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { 소유자id, 작품id }, (rs, rowNum) -> OwnershipFactory.생성(rs) );
			logger.info("" + (o!=null));
			return o;
		} catch (EmptyResultDataAccessException e) {
			logger.error("return null");
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 생성(final Ownership own) {
		//StringBuilder sbSql = new StringBuilder("INSERT INTO 작품소유(소유자id,작품id,소유시작일자,소유종료일자) VALUES(?,?,?,?)");
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("own_mem", own.getOwn_mem());
			paramMap.put("own_art", own.getOwn_art());
			paramMap.put("own_start", own.getOwn_start());
			paramMap.put("own_end", own.getOwn_end());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("own")
					.usingGeneratedKeyColumns("own_id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final Ownership own) {
		StringBuilder sbSql =  new StringBuilder("UPDATE own ");
		sbSql.append("SET own_end=? ");
		sbSql.append("where own_mem=? AND own_art=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									own.getOwn_end(),
									own.getOwn_mem(),
									own.getOwn_art()
							});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
