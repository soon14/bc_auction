// Web3 Object 생성
function createWeb3(){
    var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
    return web3;
}

// AuctionFactory 컨트랙트 Object 생성
function createFactoryContract(web3){
    var auctionContract = new web3.eth.Contract(AUCTION_FACTORY_CONTRACT_ABI, AUCTION_CONTRACT_ADDRESS);
    return auctionContract;
}

// Auction Object 컨트랙트 생성
function createAuctionContract(web3, contractAddress){
    var auctionContract = new web3.eth.Contract(AUCTION_CONTRACT_ABI, contractAddress);
    return auctionContract;
}

/**
 * TODO [경매 생성] 
 * AuctionFactory의 createAuction 함수를 호출하여 경매를 생성합니다.
 * 경매 생성 시, (작품id, 최소입찰가, 경매시작시간, 경매종료시간)을 반드시 지정해야합니다. 
 *  */ 
function createAuction(options, walletAddress, privateKey, onConfirm){
    var web3 = createWeb3();
    var contract = createFactoryContract(web3);
    var createAuctionCall = contract.methods.createAuction(options.workId, options.minValue, options.startTime, options.endTime); // 함수 호출 Object 초기화
    var encodedABI = createAuctionCall.encodeABI();

    var contractAddress = createAuctionCall.call().then(res=> {
        contractAddress = res;
    })
    var tx = {
        // nonce : txCount,
        from: walletAddress,
        to: AUCTION_CONTRACT_ADDRESS,
        gas: 2000000,
        // gasPrice: "20000000000",
        data: encodedABI
    }

    const transaction = web3.eth.accounts.signTransaction(tx, privateKey).then(res =>{
        console.log('res', res);
        
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .on('receipt', receipt=>{
            console.log(receipt);
            
            // var newAuctionContract = createAuctionContract(web3, contractAddress);
            // console.log(newAuctionContract);
            
        });        
    });

}

/**
 * TODO [입찰] 
 * 해당 컨트랙트 주소의 bid함수를 호출하여 입찰합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_bid(options, onConfirm){
    
}

/**
 * TODO [경매 종료] 
 * 해당 컨트랙트 주소의 endAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_close(options, onConfirm){
    
}

/**
 * TODO [경매 취소] 
 * 해당 컨트랙트 주소의 cancelAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_cancel(options, onConfirm){
    
}