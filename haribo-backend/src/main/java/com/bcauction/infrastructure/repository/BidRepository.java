package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Bid;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IBidRepository;
import com.bcauction.infrastructure.repository.factory.BidFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BidRepository implements IBidRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Bid> 목록조회() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bid");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> BidFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	// 사용자 입찰 내역
	@Override
	public List<Bid> userBid(final long mem_id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bid WHERE bid_mem=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
								new Object[] { mem_id }, (rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
	
	@Override
	public Bid 조회(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bid WHERE bid_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
					new Object[] { id }, (rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid 조회(final Bid bid)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bid WHERE bid_mem=? AND bid_id=? AND bid_date=? AND bid_price=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {
										bid.getBid_mem(),
										bid.getBid_id(),
										bid.getBid_date(),
										bid.getBid_price() },
								(rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid 조회(final long 경매id, final long 낙찰자id, final BigInteger 최고가) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bid WHERE bid_mem=? AND bid_auction=? AND bid_price=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {낙찰자id, 경매id, 최고가 }, (rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 생성(final Bid bid) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("bid_mem", bid.getBid_mem());
			paramMap.put("bid_auction",  bid.getBid_auction());
			paramMap.put("bid_date", bid.getBid_date());
			paramMap.put("bid_price", bid.getBid_price());
			paramMap.put("bid_issuccess", bid.getBid_issuccess());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("bid")
					.usingGeneratedKeyColumns("bid_id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final Bid bid){
		StringBuilder sbSql =  new StringBuilder("UPDATE bid ");
		sbSql.append("SET bid_issuccess=? ");
		sbSql.append("WHERE bid_memid=? AND bid_auction=? AND bid_price=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									bid.getBid_issuccess(),
									bid.getBid_mem(),
									bid.getBid_id(),
									bid.getBid_price()
							});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final long 경매id, final long 낙찰자id, final BigInteger 입찰최고가) {
		System.out.println(경매id + " " + 낙찰자id + " " + 입찰최고가);
		StringBuilder sbSql =  new StringBuilder("UPDATE bid ");
		sbSql.append("SET bid_issuccess=? ");
		sbSql.append("WHERE bid_auction=? AND bid_mem=? AND bid_price=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "Y", 경매id, 낙찰자id, 입찰최고가 });
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id) {
		StringBuilder sbSql =  new StringBuilder("DELETE FROM bid WHERE bid_id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(), new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
