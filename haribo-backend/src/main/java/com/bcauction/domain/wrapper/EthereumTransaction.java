package com.bcauction.domain.wrapper;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EthereumTransaction {

    private String txHash;
    private String Status;
    private String blockId;
    private BigInteger timestamp;
    private String from;
    private String to;
    private BigInteger amount;
    private boolean accepted;

    public static EthereumTransaction getEthereumTransaction(final EthBlock.TransactionResult tx,
                                                              final BigInteger timestamp,
                                                              final boolean accepted){
        EthereumTransaction ethTx = new EthereumTransaction();
        if(tx instanceof EthBlock.TransactionHash){
            ethTx.txHash = ((EthBlock.TransactionHash) tx).get();
        } else if(tx instanceof EthBlock.TransactionObject){
            ethTx = EthereumTransaction.convertTransaction(((EthBlock.TransactionObject) tx).get());
        }

        ethTx.accepted = accepted;
        ethTx.timestamp = timestamp;

        return ethTx;
    }

    public static List<EthereumTransaction> getEthereumTransactionList(final List<EthBlock.TransactionResult> txs, final BigInteger timestamp, final boolean accepted){
        List<EthereumTransaction> txList = new ArrayList<>();

        for(EthBlock.TransactionResult t: txs) {
            EthereumTransaction etx = EthereumTransaction.getEthereumTransaction(t, timestamp, accepted);
            etx.setStatus("0x01");
            txList.add(etx);
        }

        return txList;
    }

    public static EthereumTransaction convertTransaction(Transaction transaction){
        EthereumTransaction tx = new EthereumTransaction();

        tx.txHash = transaction.getHash();
        tx.blockId = String.valueOf(transaction.getBlockNumber());
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        tx.amount = transaction.getValue();

        return tx;
    }
    //아래 메소드를 사용하여 convert함 ->timestamp, accepted 변수 추가
    //기존 localtime timestamp를 BigInteger로 변환후 프론트에서 처리
    public static EthereumTransaction convertTransaction(final com.bcauction.domain.Transaction transaction,final BigInteger timestamp, final boolean accepted)
    {
        EthereumTransaction tx = new EthereumTransaction();
        tx.txHash = transaction.getHash();
        tx.blockId = transaction.getBlockNumber();
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        if(transaction.getValue() != null)
            tx.amount = new BigInteger(transaction.getValue());

        tx.accepted = accepted;
        tx.timestamp = timestamp;
        return tx;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public BigInteger getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigInteger timestamp) {
		this.timestamp = timestamp;
	}

	public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
