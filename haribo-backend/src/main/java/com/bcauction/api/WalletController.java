package com.bcauction.api;

import com.bcauction.application.IWalletService;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class WalletController {
	public static final Logger logger = LoggerFactory.getLogger(WalletController.class);

	private IWalletService walletService;

	@Autowired
	public WalletController(IWalletService walletService) {
		Assert.notNull(walletService, "walletService 개체가 반드시 필요!");
		this.walletService = walletService;
	}

	@RequestMapping(value = "/wallets", method = RequestMethod.POST)
	public Wallet 등록(@Valid @RequestBody Wallet wallet) {
		logger.debug("지갑의 번지 : " + wallet.getWallet_addr());
		logger.debug("지갑의 소유자 : " + String.valueOf(wallet.getWallet_mem()));

		this.walletService.등록(wallet);
		Wallet 새지갑 = walletService.조회_ETH잔액동기화(wallet.getWallet_addr());

		if (새지갑 == null)
			throw new NotFoundException(wallet.getWallet_addr() + " 해당 주소 지갑을 찾을 수 없습니다.");

		return 새지갑;
	}

	@RequestMapping(value = "/wallets", method = RequestMethod.GET)
	public List<Wallet> 목록조회() {
		List<Wallet> 목록 = walletService.목록조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		return 목록;
	}

	@RequestMapping(value = "/wallets/{address}", method = RequestMethod.GET)
	public Wallet 조회(@PathVariable String address) {
		return walletService.조회_ETH잔액동기화(address);
	}

	@RequestMapping(value = "/wallets/of/{mid}", method = RequestMethod.GET)
	public Wallet 조회By소유자(@PathVariable long mid) {
		System.out.println("/wallets/of/{mid}");
		Wallet 지갑 = this.walletService.조회(mid);
		System.out.println(지갑);
		if (지갑 == null)
			throw new EmptyListException("[소유자id] " + mid + " 해당 지갑을 찾을 수 없습니다.");

		return walletService.조회_ETH잔액동기화(지갑.getWallet_addr());
	}

	@RequestMapping(value = "/wallets/{address}", method = RequestMethod.PUT)
	public Wallet 충전(@PathVariable String address) { // 테스트 가능하도록 일정 개수의 코인을 충전해준다.
		System.out.println(address);
		return this.walletService.충전(address);
	}
}
