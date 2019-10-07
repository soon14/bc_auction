package com.bcauction.application.impl;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.Bid;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IBidRepository;
import com.bcauction.infrastructure.repository.factory.OwnershipFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService implements IAuctionService
{
	public static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

	private IAuctionContractService auctionContractService;
	private IFabricService fabricService;
	private IAuctionRepository auctionRepository;
	private IBidRepository bidRepository;
	private IDigitalWorkService digitalWorkService;
	

	@Autowired
	public AuctionService(IAuctionContractService auctionContractService,
						  IFabricService fabricService,
							IAuctionRepository auctionRepository, IBidRepository bidRepository,
							IDigitalWorkService digitalWorkService) {
		this.auctionContractService = auctionContractService;
		this.fabricService = fabricService;
		this.auctionRepository = auctionRepository;
		this.bidRepository = bidRepository;
		this.digitalWorkService = digitalWorkService;
	}
	
	public List<Bid> userAuctionBid(final long mem_id) {
		return this.bidRepository.userBid(mem_id);
	}

	@Override
	public List<Auction> 경매목록조회() {
		return this.auctionRepository.목록조회();
	}

	@Override
	public Auction 조회(final long 경매id) {
		return this.auctionRepository.조회(경매id);
	}

	@Override
	public Auction 조회(final String 컨트랙트주소) {
		return this.auctionRepository.조회(컨트랙트주소);
	}

	@Override
	public Auction 생성(final Auction auction) {
		if(auction.getAuction_start() == null) return null;
		if(auction.getAuction_end() == null) return null;
		if(auction.getAuction_makerid() == 0) return null;
		if(auction.getAuction_goodsid() == 0) return null;
		if(auction.getAuction_contract() == null) return null;
		if(auction.getAuction_min()  == null) return null;
		

		auction.setAuction_makedate(LocalDateTime.now().plusHours(9));
		long id = this.auctionRepository.생성(auction);

		return this.auctionRepository.조회(id);
	}

	@Override
	public Bid 입찰(Bid 입찰) {
		long id = this.bidRepository.생성(입찰);
		return this.bidRepository.조회(id);
	}

	@Override
	public Bid 낙찰(final long 경매id, final long 낙찰자id, final BigInteger 입찰최고가)
	{
		int affected = this.bidRepository.수정(경매id, 낙찰자id, 입찰최고가);
		if(affected == 0)
			return null;

		return this.bidRepository.조회(경매id, 낙찰자id, 입찰최고가);
	}

	/**
	 * 프론트엔드에서 스마트 컨트랙트의 경매종료(endAuction) 함수 직접 호출 후
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param 경매id
	 * @param 회원id
	 * @return Auction
	 * 1. 해당 경매의 상태가 E(ended)로 바뀌고,
	 * 2. 입찰 정보 중 최고 입찰 정보를 '낙찰'로 업데이트해야 한다.
	 * 3. 데이터베이스의 소유권정보를 업데이트 한다.
	 * 4. 패브릭 상에도 소유권 이전 정보가 추가되어야 한다.
	 * 5. 업데이트 된 경매 정보를 반환한다.
	 * */
	@Override
	public Auction 경매종료(final long 경매id, final long 회원id) {
		// TODO
		//1. 해당 경매의 상태가 E(ended)로 바뀌고,
		Auction targetAuction = this.auctionRepository.조회(경매id);
		targetAuction.setAuction_status("E");
		logger.info("종료된 경매 " + targetAuction);
		this.auctionRepository.수정(targetAuction);
		
		//2. 입찰 정보 중 최고 입 찰 정보를 '낙찰'로 업데이트해야 한다.
		// this.bidRepository.수정(경매id, 낙찰자id, 입찰최고가) 메서드 호출만하면 내부에서 알아서 issuccess 값을 변경해 줌.
		BigInteger highest = this.auctionContractService.현재최고가(targetAuction.getAuction_contract());
		int result = this.bidRepository.수정(경매id, 회원id, highest);
		logger.info("bidRepository.수정 " + result);
		
		//3. 데이터베이스의 소유권정보를 업데이트 한다.
		//4. 패브릭 상에도 소유권 이전 정보가 추가되어야 한다.
		// fabricService.소유권이전 메서드에서 이전 소유권 소멸과 새로운 소유권 수정이 같이 이루어짐
		DigitalWork targetWork = this.digitalWorkService.조회(targetAuction.getAuction_goodsid());
		long targetWork_art_id = targetWork.getArt_id();
		// 작품이 최근 소유권을 조회해서 소유권자 검색
		List<FabricAsset> targetWorkHistory = this.fabricService.작품이력조회(targetWork_art_id);
		int last = targetWorkHistory.size() - 1;
		String previousId = targetWorkHistory.get(last).getOwner();
		long previousOwner = Long.parseLong(previousId);
		// 최근 소유권자에서 최고 입찰자에게로 소유권 이전 (from : 최근 소유권자, to : 최고입찰자, 작품 : work(art))
		Ownership newOwner = this.fabricService.소유권이전(previousOwner, 회원id, targetWork_art_id);
		
		//5. 업데이트 된 경매 정보를 반환한다.
		return targetAuction;
	}

	/**
	 * 프론트엔드에서 스마트 컨트랙트의 경매취소(cancelAuction) 함수 직접 호출 후
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param 경매id
	 * @param 회원id
	 * @return Auction
	 * 1. 해당 경매의 상태와(C,canceled) 종료일시를 업데이트 한다.
	 * 2. 업데이트 된 경매 정보를 반환한다.
	 * */
	@Override
	public Auction 경매취소(final long 경매id, final long 회원id)
	{
		// TODO
		// 1. 해당 경매의 상태와(C,canceled) 종료일시를 업데이트 한다.
		Auction targetAuction = this.auctionRepository.조회(경매id);
		targetAuction.setAuction_status("C");
		targetAuction.setAuction_end(LocalDateTime.now());
		this.auctionRepository.수정(targetAuction);
		
		// 3. 업데이트 된 경매 정보를 반환한다.
		return targetAuction;
	}
}
