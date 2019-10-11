package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Transaction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.infrastructure.repository.factory.TransactionFactory;
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
public class TransactionRepository implements ITransactionRepository {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transaction> 목록조회() {
    	StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction order by trancation_id desc limit 10 ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> TransactionFactory.생성(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Transaction 조회(String hash) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction  WHERE hash=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { hash }, (rs, rowNum) -> TransactionFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public List<Transaction> 조회By주소(final String 주소)
    {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction  WHERE from_hash=? OR to_hash=?");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                                           new Object[] { 주소, 주소 }, (rs, rowNum) -> TransactionFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public long 추가(Transaction transaction) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hash", transaction.getHash());
            paramMap.put("nonce", transaction.getNonce());
            paramMap.put("block_hash", transaction.getBlockHash());
            paramMap.put("block_number", transaction.getBlockNumber());
            paramMap.put("transaction_Index", transaction.getTransactionIndex());
            paramMap.put("from_hash", transaction.getFrom());
            paramMap.put("to_hash", transaction.getTo());
            paramMap.put("value", transaction.getValue());
            paramMap.put("gas_price", transaction.getGasPrice());
            paramMap.put("gas", transaction.getGas());
            paramMap.put("input", transaction.getInput());
            paramMap.put("creates", transaction.getCreates());
            paramMap.put("public_key", transaction.getPublicKey());
            paramMap.put("raw", transaction.getRaw());
            paramMap.put("r", transaction.getR());
            paramMap.put("s", transaction.getS());
            paramMap.put("v", transaction.getV());
            paramMap.put("transaction_savedate", LocalDateTime.now().plusHours(9));

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("transaction")
                    .usingGeneratedKeyColumns("trancation_id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
