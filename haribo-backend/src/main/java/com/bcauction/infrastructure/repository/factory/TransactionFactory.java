package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionFactory {

    public static Transaction 생성(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        Transaction transaction = new Transaction();

        transaction.setTrancation_id(rs.getLong("trancation_id"));
        transaction.setHash(rs.getString("hash"));
        transaction.setNonce(rs.getString("nonce"));
        transaction.setBlockHash(rs.getString("block_hash"));
        transaction.setBlockNumber(rs.getString("block_number"));
        transaction.setTransactionIndex(rs.getString("transaction_index"));
        transaction.setFrom(rs.getString("from_hash"));
        transaction.setTo(rs.getString("to_hash"));
        transaction.setValue(rs.getString("value"));
        transaction.setGasPrice(rs.getString("gas_price"));
        transaction.setGas(rs.getString("gas"));
        transaction.setInput(rs.getString("input"));
        transaction.setCreates(rs.getString("creates"));
        transaction.setPublicKey(rs.getString("public_key"));
        transaction.setRaw(rs.getString("raw"));
        transaction.setR(rs.getString("r"));
        transaction.setS(rs.getString("s"));
        transaction.setV(rs.getInt("v"));
        transaction.setTrancation_savedate(rs.getTimestamp("trancation_savedate").toLocalDateTime());

        return transaction;
    }
}
