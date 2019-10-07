package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Auction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.infrastructure.repository.factory.AuctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuctionRepository implements IAuctionRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Auction> 목록조회()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE auction_status =?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{ "V" }, (rs, rowNum) -> AuctionFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction 조회(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE auction_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { id }, (rs, rowNum) -> AuctionFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction 조회(final String auction_contract)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE auction_contract=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { auction_contract }, (rs, rowNum) -> AuctionFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 생성(final Auction auction) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("auction_makerid", auction.getAuction_makerid());
			paramMap.put("auction_goodsid", auction.getAuction_goodsid());
			paramMap.put("auction_makedate", auction.getAuction_makedate());
			paramMap.put("auction_status", auction.getAuction_status());
			paramMap.put("auction_start", auction.getAuction_start());
			paramMap.put("auction_end", auction.getAuction_end());
			paramMap.put("auction_min", auction.getAuction_min());
			paramMap.put("auction_contract", auction.getAuction_contract());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("auction")
					.usingGeneratedKeyColumns("auction_id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final Auction auction)
	{
		StringBuilder sbSql =  new StringBuilder("UPDATE auction ");
		sbSql.append("SET auction_status=?, auction_end=? ");
		sbSql.append("where auction_id=? AND auction_makerid=? AND auction_goodsid=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] {
			                                		auction.getAuction_status(),
					                           auction.getAuction_end(),
					                           auction.getAuction_id(),
					                           auction.getAuction_makerid(),
					                           auction.getAuction_goodsid()
			                                });
		} catch (Exception e) {
			System.out.println("err : " + e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("DELETE FROM auction WHERE auction_id =?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
