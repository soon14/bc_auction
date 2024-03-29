package com.bcauction.application.impl;

import com.bcauction.application.IEthereumService;
import com.bcauction.domain.*;
import com.bcauction.domain.Transaction;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;
import com.bcauction.infrastructure.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EthereumService implements IEthereumService {

	private static final Logger log = LoggerFactory.getLogger(EthereumService.class);

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(1L);
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21_000L);

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;
	@Value("${eth.encrypted.password}")
	private String PASSWORD;
	@Value("${eth.admin.wallet.filename}")
	private String ADMIN_WALLET_FILE;
	@Value("${spring.web3j.client-address}")
	private String ethUrl;
	
	private ITransactionRepository transactionRepository;
	private BigInteger latestBlockHeight = BigInteger.valueOf(0);
	
	@Autowired
	private Web3j web3j;

	@Autowired
	public EthereumService(ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	private EthBlock.Block latestBlock(final boolean fullFetched) {
		try {
			//
			web3j = Web3j.build(new HttpService(ethUrl));
			EthBlock latestBlockResponse;
			latestBlockResponse = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).sendAsync()
					.get();
			return latestBlockResponse.getBlock();
		} catch (ExecutionException | InterruptedException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * 최근 블록 조회 예) 최근 20개의 블록 조회
	 * 
	 * @return List<Block>
	 */
	@Override
	public List<Block> 최근블록조회() {
		
		List<Block> res=new ArrayList<Block>();
		//latestBlockHeight,latestBlockHeight-10 변수선언 
		latestBlockHeight=latestBlock(false).getNumber();
		BigInteger end=latestBlockHeight.subtract(BigInteger.valueOf(10));
		
		for (BigInteger bi = latestBlockHeight; bi.compareTo(end)>0; bi=bi.subtract(BigInteger.ONE)){
			try {
				//최신블록넘버부터 역순으로 idx(bi)탐색
				EthBlock recievedEthBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(bi), true).sendAsync().get();
				//Ethblcok.block을 wrapper클래스 Block으로 커스터마이징 한 후 List에 넣기
				Block tmp=new Block();
				tmp=tmp.fromOriginalBlock(recievedEthBlock.getBlock());
				res.add(tmp);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 최근 생성된 블록에 포함된 트랜잭션 조회 이더리움 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * 
	 * @return List<EthereumTransaction>
	 */
	@Override
	public List<EthereumTransaction> 최근트랜잭션조회() {
		// DB pk에러(trancation_id) 추후 insert, select 한번에 수정
		List<Transaction>tmp=transactionRepository.목록조회();
		List<EthereumTransaction>res=new ArrayList<EthereumTransaction>();
		for(Transaction q : tmp) {
			EthereumTransaction mid=null;
			long time=Timestamp.valueOf(q.getTrancation_savedate()).getTime();
			BigInteger timestamp=BigInteger.valueOf((time/1000)-60*60*9);
			mid=mid.convertTransaction(q,timestamp,true);
			res.add(mid);
		}
		return res;
	}

	/**
	 * 특정 블록 검색 조회한 블록을 Block으로 변환해야 한다.
	 * 
	 * @param 블록No
	 * @return Block
	 */
	@Override
	public Block 블록검색(String 블록No) {
		// TODO
		BigInteger q=BigInteger.valueOf(Long.parseLong(블록No));
		try {
			EthBlock tmp=web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(q), true).sendAsync().get();
			Block res=new Block();
			res=res.fromOriginalBlock(tmp.getBlock());
			return res;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 특정 hash 값을 갖는 트랜잭션 검색 조회한 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * 
	 * @param 트랜잭션Hash
	 * @return EthereumTransaction
	 */
	@Override
	public EthereumTransaction 트랜잭션검색(String 트랜잭션Hash) {
		// TODO
		Transaction tmp=transactionRepository.조회(트랜잭션Hash);
		
		long time=Timestamp.valueOf(tmp.getTrancation_savedate()).getTime();
		BigInteger timestamp=BigInteger.valueOf((time/1000)-60*60*9);
		
		EthereumTransaction res=null;
		res=res.convertTransaction(tmp, timestamp, true);
		return res;
	}

	/**
	 * 이더리움으로부터 해당 주소의 잔액을 조회하고 동기화한 트랜잭션 테이블로부터 Address 정보의 trans 필드를 완성하여 정보를
	 * 반환한다.
	 * 
	 * @param 주소
	 * @return Address
	 */
	@Override
	public Address 주소검색(String 주소) {
		// TODO
		Address res=new Address();
		EthGetBalance ethGetBalance;
		
		try {
			//Address객체 id, balance setting
			res.setId(주소);
			
			ethGetBalance = web3j.ethGetBalance(주소, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger wei = ethGetBalance.getBalance();
			res.setBalance(wei);
			
			//txCount setting
			List<Transaction> tmp=transactionRepository.조회By주소(주소);
			res.setTxCount(BigInteger.valueOf(tmp.size()));
			
			//trans setting
			List<EthereumTransaction> tx=new ArrayList<EthereumTransaction>();
			for(Transaction q : tmp) {
				EthereumTransaction mid=null;
				long time=Timestamp.valueOf(q.getTrancation_savedate()).getTime();
				BigInteger timestamp=BigInteger.valueOf((time/1000)-60*60*9);
				mid=mid.convertTransaction(q,timestamp,true);
				tx.add(mid);
			}
			res.setTrans(tx);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * [주소]로 시스템에서 정한 양 만큼 이더를 송금한다. 이더를 송금하는 트랜잭션을 생성, 전송한 후 결과인 String형의 트랜잭션 hash
	 * 값을 반환한다.
	 * 
	 * @param 주소
	 * @return String 생성된 트랜잭션의 hash 반환 (참고, TransactionReceipt)
	 */
	@Override
	public String 충전(final String 주소) // 특정 주소로 테스트 특정 양(5Eth) 만큼 충전해준다.
	{
		// TODO
		// web3j build
		web3j = Web3j.build(new HttpService("http://13.124.65.11:8545"));
		// admin build
		Admin admin = Admin.build(new HttpService("http://13.124.65.11:8545"));
		// geth account(계정) unlock(잠금해제)
		PersonalUnlockAccount personalUnlockAccount;
		// wallet file(지갑파일) 불러오기(admin.wallet file을 만들 것)
		Credentials credentials;
		String transactionHash = null;
		try {
			System.out.println("충전 함수 진입 중");
			personalUnlockAccount = admin.personalUnlockAccount("0xabe4d3eaa6e1b78106035c608562a222e5741b9d", "eth02")
					.send();
			if (personalUnlockAccount.accountUnlocked()) {
				System.out.println("잠금해제");
				credentials = CommonUtil.getCredential(ADMIN_WALLET_FILE, PASSWORD);
				System.out.println("송금준비");
				
				// eth_getTransactionCount 메소드 를 통해 사용 가능한 다음 nonce를 얻을 수 있습니다 .
				EthGetTransactionCount ethGetTransactionCount = web3j
						.ethGetTransactionCount("0xabe4d3eaa6e1b78106035c608562a222e5741b9d", DefaultBlockParameterName.LATEST).sendAsync().get();
				BigInteger nonce = ethGetTransactionCount.getTransactionCount();
				
				// nonce를 사용하여 트랜잭션 오브젝트를 작성할 수 있습니다.
				BigInteger value = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();
				RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, GAS_PRICE, GAS_LIMIT, 주소, value);
				
				// 트랜잭션에 서명하고 인코딩 할 수 있습니다.
				byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
				String hexValue = Numeric.toHexString(signedMessage);
				
				// eth_sendRawTransaction을 사용하여 트랜잭션을 보냅니다.
				EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
				transactionHash = ethSendTransaction.getTransactionHash();
				
				while(true) {
					EthGetTransactionReceipt tmp=web3j.ethGetTransactionReceipt(transactionHash).send();
					if(tmp.getResult()!=null) {
						break;
					}
					Thread.sleep(4000);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return transactionHash;
		}
	}

}
