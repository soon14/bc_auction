package com.bcauction.application.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionHash;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.bcauction.domain.EthInfo;
import com.bcauction.domain.repository.IEthInfoRepository;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.domain.Transaction;
/**
 * EthBlockListeningService
 * 이더리움 네트워크의 새로 생성된 블록 정보로부터
 * 트랜잭션을 동기화하는 기능 포함
 */
@Service
public class EthBlockListeningService{
	
	private static final Logger log = LoggerFactory.getLogger(EthBlockListeningService.class);

	private BigInteger latestBlockHeight = BigInteger.valueOf(0);
	
	private Web3j web3j;
	private IEthInfoRepository ethInfoRepository;
	private ITransactionRepository transactionRepository;

	@Value("${spring.web3j.client-address}")
	private String ethUrl;

	@Autowired
	public EthBlockListeningService(Web3j web3j,
									IEthInfoRepository ethInfoRepository,
									ITransactionRepository transactionRepository)
	{
		this.web3j = web3j;
		this.ethInfoRepository = ethInfoRepository;
		this.transactionRepository = transactionRepository;
	}

	/**
	 * 구축한 이더리움 네트워크로부터 신규 생성된 블록을 동기화한다.
	 * Scheduled 어노테이션을 이용하여 주기적으로 listen()함수를 실행시킬 수 있다.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@PostConstruct
	@Scheduled(fixedRateString = "5000", initialDelay = 10000)
	public void listen() throws InterruptedException, ExecutionException 
	{
		EthBlockListeningService ethBlockListeningService=new EthBlockListeningService(Web3j.build(new HttpService(ethUrl)), ethInfoRepository, transactionRepository);
	
		//이더리움 네트워크의 최신 블록(ethereumLatestBlock)과 DB에 기록되어있는 블록넘버(beforeBlock) 불러오기
		// ethereumLatestBlock과 beforeBlock값이 다르면, 즉 이더리움 네트워크와 데이터베이스의 블럭정보가 동기화 되어 있지 않다면
		// 블럭정보를 이더리움 네트워크에서 받아와서 데이터베이스에 기록한다.
		EthBlock ethereumLatestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).sendAsync().get();
		EthInfo beforeBlock=ethInfoRepository.get(ethUrl);
		
		// 두 블록사이에 있는 블록들에서 트랜잭션조회처리
		latestBlockHeight=BigInteger.valueOf(Long.parseLong(beforeBlock.getLatestBlockNumber()));
		BigInteger databaseBlockNumber = ethereumLatestBlock.getBlock().getNumber();
		
		//데이터베이스가 기록하지 못한 최신 블럭 정보를 불러와서 데이터베이스에 저장.
		for (BigInteger bi = latestBlockHeight.add(BigInteger.ONE); bi.compareTo(databaseBlockNumber)<=0; bi=bi.add(BigInteger.ONE)){
			EthBlock recievedEthBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(bi), true).sendAsync().get();
			List<TransactionResult> txs = recievedEthBlock.getBlock().getTransactions();
			
			for (int i = 0; i < txs.size(); i++) {
				TransactionObject txObject = (TransactionObject) txs.get(i).get();
				Transaction tx = new Transaction(txObject);
				this.transactionRepository.추가(tx);
			}
			
		}
				
				
		String str=""+ethereumLatestBlock.getBlock().getNumber()+"";
		ethInfoRepository.put(ethUrl, str);
		
		
		log.info("New Block Subscribed Here : "+ethereumLatestBlock.getBlock().getNumber());
	}
}
