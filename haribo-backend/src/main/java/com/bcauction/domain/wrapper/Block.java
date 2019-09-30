package com.bcauction.domain.wrapper;

import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

public class Block {
    private BigInteger blockNo;
    private List<EthereumTransaction> trans;
    private BigInteger timestamp;
    private String nonce;
    private String difficulty;
    private BigInteger size;
    private BigInteger gasUsed;
    private BigInteger gasLimit;
    private String hash;
    private String parentHash;
    private String miner;
    //wrapper클래스 Block에 timestamp를 Localtime대신 BigInteger로 바꿈 -> 프론트에서 00전으로 표현하기 위해
    //nonce 변수 추가
    public static Block fromOriginalBlock(final EthBlock.Block currentBlock) {
        Block block = new Block();
        
        block.blockNo = currentBlock.getNumber();
        block.timestamp = currentBlock.getTimestamp();
        block.nonce=currentBlock.getNonceRaw();
        block.difficulty = String.valueOf(currentBlock.getDifficulty());
        block.size = currentBlock.getSize();
        block.gasLimit = currentBlock.getGasLimit();
        block.gasUsed = currentBlock.getGasUsed();
        block.trans = EthereumTransaction.getEthereumTransactionList(currentBlock.getTransactions(), currentBlock.getTimestamp(), true);
        block.hash = currentBlock.getHash();
        block.parentHash = currentBlock.getParentHash();
        block.miner = currentBlock.getMiner();

        return block;
    }

    public BigInteger getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(BigInteger blockNo) {
        this.blockNo = blockNo;
    }

    public List<EthereumTransaction> getTrans() {
        return trans;
    }

    public void setTrans(List<EthereumTransaction> trans) {
        this.trans = trans;
    }

    public BigInteger getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigInteger timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(final String hash)
    {
        this.hash = hash;
    }

    public String getParentHash()
    {
        return parentHash;
    }

    public void setParentHash(final String parentHash)
    {
        this.parentHash = parentHash;
    }

    public String getMiner()
    {
        return miner;
    }

    public void setMiner(final String miner)
    {
        this.miner = miner;
    }
}
