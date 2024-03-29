package com.bcauction.application.impl;

import com.bcauction.application.IEthereumService;
import com.bcauction.application.IWalletService;
import com.bcauction.domain.Address;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class WalletService implements IWalletService
{
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	private IWalletRepository walletRepository;
	private IEthereumService ethereumService;

	@Autowired
	public WalletService(IWalletRepository walletRepository,
						 IEthereumService ethereumService) {
		this.walletRepository = walletRepository;
		this.ethereumService = ethereumService;
	}
	
	@Autowired
	private Web3j web3j;

	@Override
	public List<Wallet> 목록조회()
	{
		return this.walletRepository.목록조회();
	}

	/**
	 * DB에 저장된 지갑주소의 정보와 이더리움의 잔액 정보를 동기화한다.
	 * @param 지갑주소
	 * @return Wallet
	 */
	@Override
	public Wallet 조회_ETH잔액동기화(final String 지갑주소)
	{
		Wallet wallet = walletRepository.조회(지갑주소);
		if(wallet == null)
			throw new NotFoundException(지갑주소 + " 해당 주소 지갑을 찾을 수 없습니다.");

		web3j=Web3j.build(new HttpService("http://13.124.65.11:8545"));
//		web3j=Web3j.build(new HttpService("http://127.0.0.1:7545"));
		
	    EthGetBalance ethGetBalance = null;
	    
	    try {
	    	//이더리움 노드에게 지정한 Address 잔액을 조회. DB에 저장된 BigDecimal형태로 변환하여 사용(wei_dec)
	    	ethGetBalance = web3j.ethGetBalance(지갑주소, DefaultBlockParameterName.LATEST).sendAsync().get();
	    	BigInteger wei = ethGetBalance.getBalance();
	    	BigDecimal wei_dec=new BigDecimal(ethGetBalance.getBalance());
	    	
	    	//DB에 있는 잔액 != Ethereum network에서 조회한 잔액
	    	if(wallet.getWallet_money()!=wei_dec) {
	    		walletRepository.잔액갱신(지갑주소, wei_dec);
	    		wei_dec=Convert.fromWei(wei.toString() , Convert.Unit.ETHER);
	    		wallet.setWallet_money(wei_dec);
	    	}
	    }catch (InterruptedException e) {
	    	e.printStackTrace();
	    }catch (ExecutionException e) {
	    	e.printStackTrace();
	    }
	    
		return wallet;
	}

	@Override
	public Wallet 조회(final long id)
	{
		Wallet wallet = this.walletRepository.조회(id);
		if(wallet == null)
			throw new NotFoundException(id + " 해당 회원의 주소 지갑을 찾을 수 없습니다.");
		System.out.println(wallet);
		return 조회_ETH잔액동기화(wallet.getWallet_addr());
	}

	@Override
	public Wallet 등록(final Wallet 지갑)
	{
		long id = this.walletRepository.추가(지갑);
		return this.walletRepository.조회(id);
	}

	@Override
	public Wallet 잔액갱신(final String 지갑주소, final BigDecimal 잔액)
	{
		int affected = this.walletRepository.잔액갱신(지갑주소, 잔액);
		if(affected == 0)
			throw new ApplicationException("잔액갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.조회(지갑주소);
	}

	@Override
	public Wallet 충전회수갱신(final String 지갑주소)
	{
		System.out.println("충전회수 갱신 진입");
		int affected = this.walletRepository.충전회수갱신(지갑주소);
		if(affected == 0)
			throw new ApplicationException("충전회수갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.조회(지갑주소);
	}

	/**
	 * [지갑주소]로 이더를 송금하는 충전 기능을 구현한다.
	 * 무한정 충전을 요청할 수 없도록 조건을 두어도 좋다.
	 * @param 지갑주소
	 * @return Wallet
	 */
	@Override
	public Wallet 충전(String 지갑주소) {
		Wallet wallet = this.조회_ETH잔액동기화(지갑주소);
		if (wallet == null || !wallet.isCharge()) {
			throw new ApplicationException("[1] 충전할 수 없습니다!");
		}

		try {
			String txHash = this.ethereumService.충전(지갑주소);
			if(txHash == null || txHash.equals(""))
				throw new ApplicationException("충전회수갱신 트랜잭션을 보낼 수 없습니다!");
			log.info("received txhash: " + txHash);

			this.충전회수갱신(지갑주소);

			return this.조회_ETH잔액동기화(지갑주소);
		}
		catch (Exception e) {
			throw new ApplicationException("[2] 충전할 수 없습니다!");
		}
	}
}
