package com.bcauction.application.impl;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.domain.*;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.DomainException;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.AuctionContract;
import com.bcauction.domain.wrapper.AuctionFactoryContract;
import com.bcauction.infrastructure.repository.AuctionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * AuctionContractService 작성, 배포한 AuctionFactory.sol Auction.sol 스마트 컨트랙트를 이용한다.
 */
@Service
public class AuctionContractService implements IAuctionContractService {
	private static final Logger log = LoggerFactory.getLogger(AuctionContractService.class);

	@Value("${eth.auction.factory.contract}")
	private String AUCTION_FACTORY_CONTRACT;

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;

	@Value("${eth.admin.wallet.filename}")
	private String WALLET_RESOURCE;

	@Value("${eth.encrypted.password}")
	private String PASSWORD;

	private AuctionFactoryContract auctionFactoryContract;
	private AuctionContract auctionContract;
	private ContractGasProvider contractGasProvider = new DefaultGasProvider();
	private Credentials credentials;

	@Autowired
	private Web3j web3j;

	private IWalletRepository walletRepository;

	@Autowired
	public AuctionContractService(IWalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 경매 정보를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return AuctionInfo 
	 * 1. web3j API를 이용하여 해당 컨트랙트주소의 스마트 컨트랙트를 로드(load)한다. 
	 * 2. info의 highestBidder의 정보를 가지고 최고입찰자 회원의 id를 찾아 
	 * 3. AuctionInfo의 인스턴스를  생성하여 반환한다.
	 */
	@Override
	public AuctionInfo 경매정보조회(final String 컨트랙트주소) {
		// TODO
		AuctionInfo auctionInfo = new AuctionInfo();
		web3j = Web3j.build(new HttpService("http://13.124.65.11:8545"));
		try {
			log.info("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
			credentials = CommonUtil.getCredential(WALLET_RESOURCE, PASSWORD);
			log.info("Credentials loaded");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info("parameter to address " + 컨트랙트주소);
		
		// smart contract factory load
		auctionFactoryContract = auctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials, contractGasProvider);
		log.info("Auction contract factory loaded to address " + auctionFactoryContract.getContractAddress());
		// smart contract load
		auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);
		log.info("Smart contract loaded to address " + auctionContract.getContractAddress());

		BigInteger auctionMinValue = null;
		TransactionReceipt receipt = null;
		try {
			auctionMinValue = auctionContract.minValue().send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("getTransactionReceipt "+auctionContract.getTransactionReceipt());
		
		
		auctionInfo.setAucInfo_min(auctionMinValue);
		log.info("minValue "+auctionMinValue);
		//auctionInfo.setAucInfo_close(aucInfo_close); // 종료
		//auctionInfo.setAucInfo_highest(aucInfo_highest); // 최고 입찰액
		//auctionInfo.setAucInfo_highestBider(aucInfo_highestBider); // 최고입찰자id
		
		return auctionInfo;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰가를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return BigInteger 현재최고가
	 */
	@Override
	public BigInteger 현재최고가(final String 컨트랙트주소) {
		// TODO
		return BigInteger.ZERO;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰 주소를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return String 최고 입찰한 이더리움 주소(EOA)
	 */
	@Override
	public String 현재최고입찰자주소(final String 컨트랙트주소) {
		// TODO
		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 생성된 모든 경매 컨트랙트의 주소 목록을 조회한다.
	 * 
	 * @return List<String> 경매 컨트랙트의 주소 리스트
	 */
	@Override
	public List<String> 경매컨트랙트주소리스트() {
		// TODO
		return null;
	}
}
