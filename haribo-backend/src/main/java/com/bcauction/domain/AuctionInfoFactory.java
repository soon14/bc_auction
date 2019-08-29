package com.bcauction.domain;

import org.web3j.tuples.generated.Tuple7;

import java.math.BigInteger;

public class AuctionInfoFactory {

    public static AuctionInfo 생성(String 컨트랙트주소, long 지갑소유자Id,
            Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, Boolean> info) {
        AuctionInfo auctionInfo = new AuctionInfo();
        auctionInfo.setAucInfo_contract(컨트랙트주소);
        auctionInfo.setAucInfo_start(CommonUtil.ETH타임스탬프변환(info.getValue1().longValue()));
        auctionInfo.setAucInfo_end(CommonUtil.ETH타임스탬프변환(info.getValue2().longValue()));
        auctionInfo.setAucInfo_min(info.getValue3());
        auctionInfo.setAucInfo_artId(info.getValue4().longValue());

        auctionInfo.setAucInfo_highestBider(지갑소유자Id);
        auctionInfo.setAucInfo_highest(info.getValue6());
        auctionInfo.setAucInfo_close(info.getValue7());

        return auctionInfo;
    }
}
