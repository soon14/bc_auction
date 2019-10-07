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
        console.log("contract address ", contractAddress)
    })
    var tx = {
        // nonce : txCount,
        from: walletAddress,
        to: AUCTION_CONTRACT_ADDRESS,
        gas: 2000000,
        // gasPrice: "20000000000",
        data: encodedABI
    }
    

    // 트랜잭션 보내기 전 두 번째 contract
    createAuctionCall.call().then(res=> {
        contractAddress = res;
        console.log("트랜잭션 보내기 전 두 번째 contract address ", contractAddress)
    })
    
    // 트랜잭션 보내는 함수
    const transaction = web3.eth.accounts.signTransaction(tx, privateKey).then(res =>{
        console.log('res', res);
        
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .on('receipt', receipt=>{
            console.log("send tranaction result ",receipt);
            
            // 트랜잭션 후 만들어진 컨트랙트 주소로 컨트랙트 객체 생성
            var newAuctionContract = createAuctionContract(web3, contractAddress);
            console.log("생성된 컨트랙트 객체", newAuctionContract);

            // 이건 진짜 모르겠따
            var log = { newAuction:contractAddress};
            onConfirm(log)

            // 옥션 주소 변동 유무 확인
            createAuctionCall.call().then(res=> {
                contractAddress = res;
                console.log("트랜잭션 보낸 후 contract address ", contractAddress)
                
            })

            contract.events.AuctionCreated()
                .on('data', event2=>{ console.log('event2', event2)});
        });        
    });
}

function auctionEndTime(options, onConfirm){
    var web3 = createWeb3();
    var contract = createAuctionContract(web3, options.contractAddress);
    
    var createBidCall = contract.methods.bid();
    var encodedABI = createBidCall.encodeABI();
}





/**
 * TODO [입찰] 
 * 해당 컨트랙트 주소의 bid함수를 호출하여 입찰합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_bid(options, onConfirm){
    console.log('[auctionFactory.js : auction_bid] options', options);
    var web3 = createWeb3();
    var contract = createAuctionContract(web3, options.contractAddress);
    
    var createBidCall = contract.methods.bid();
    var encodedABI = createBidCall.encodeABI();

    var tx = {
        from: options.walletAddress,
        to: options.contractAddress,
        gas: 2000000,
        value : web3.utils.toWei(options.amount, 'ether'),
        data: encodedABI
    }

    // console.log('[auctionFactory.js : tx', tx);
    
    
    // 트랜잭션 보내는 함수
    const transaction = web3.eth.accounts.signTransaction(tx, options.privateKey).then(res =>{
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .then(receipt=>{
            console.log('[auctionFactory.js : auction_bid] receipt', receipt);
            onConfirm(receipt);
            // var highestBidCall = contract.methods.highestBid().call().then(ress=>{
            //     highestBidCall = ress;
            //     console.log('[auctionFactory.js : auction_bid] highestBidCall', highestBidCall);
            // });

        });        
    });

}

/**
 * TODO [경매 종료] 
 * 해당 컨트랙트 주소의 endAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_close(options, onConfirm){
    var web3 = createWeb3();
    var contract = createAuctionContract(web3, options.contractAddress);
    console.log("[auctionFactory.js : auction_close] contract", contract );
    

    var createBidCall = contract.methods.endAuction();
    var encodedABI = createBidCall.encodeABI();

    var tx = {
        from: options.walletAddress,
        to: options.contractAddress,
        gas: 2000000,
        data: encodedABI,
    }
    contract.methods.auctionEndTime().call().then(auctionEndTime=>{
        console.log('before_auctionEndTime', auctionEndTime);
    });
    contract.methods.owner().call().then(res=>{
        console.log('owner', res);
        
    })
    // 트랜잭션 보내는 함수
    const transaction = web3.eth.accounts.signTransaction(tx, options.privateKey).then(res =>{
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .then(receipt=>{
            console.log('[auctionFactory.js : auction_close] tx receipt', receipt);
            contract.methods.highestBidder().call().then(bidder=>{
                receipt.bidder = bidder;
                console.log('hbidder',bidder );
                
                onConfirm(receipt);
            });

            contract.methods.highestBid().call().then(bidder=>{
                console.log('hbid', bidder);
            });

            contract.methods.ended().call().then(ended=>{
                console.log('ended', ended);
            });

            contract.methods.auctionEndTime().call().then(auctionEndTime=>{
                console.log('after_auctionEndTime', auctionEndTime);
            });

            contract.methods.nowValue().call().then(nowValue=>{
                console.log('nowValue', nowValue);
            });
            
        });        
    });
}

/**
 * TODO [경매 취소] 
 * 해당 컨트랙트 주소의 cancelAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */ 
function auction_cancel(options, onConfirm){
    var web3 = createWeb3();
    var contract = createAuctionContract(web3, options.contractAddress);
    console.log("[auctionFactory.js : auction_close] contract", contract );
    
    var createBidCall = contract.methods.cancelAuction();
    var encodedABI = createBidCall.encodeABI();

    var tx = {
        from: options.walletAddress,
        to: options.contractAddress,
        gas: 2000000,
        data: encodedABI
    }
    
    // 트랜잭션 보내는 함수
    const transaction = web3.eth.accounts.signTransaction(tx, options.privateKey).then(res =>{
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .then(receipt=>{
            contract.methods.ended().call().then(ended=>{
                console.log('ended', ended);
            });
                onConfirm(receipt);
        });
    });
}

function auction_withdraw(options, onConfirm){
    var web3 = createWeb3();
    var contract = createAuctionContract(web3, options.contractAddress);
    var createWithdrawCall = contract.methods.withdraw();
    var encodedABI = createWithdrawCall.encodeABI();

    // console.log('contract', contract);
    
    var tx = {
        from: options.walletAddress,
        to: options.contractAddress,
        gas: 2000000,
        data: encodedABI
    }

    // createWithdrawCall.call().then(res=>{
    //     console.log("[auctionFactory.js : auction_withdraw ]", res);
        
    // })
    var checkWithdrawCall = contract.methods.getPendingReturnsBy(options.walletAddress);
    checkWithdrawCall.call().then(res =>{
        console.log('checkWithdraw', res);
    })


    // 트랜잭션 보내는 함수
    const transaction = web3.eth.accounts.signTransaction(tx, options.privateKey).then(res =>{
        web3.eth.sendSignedTransaction(res.rawTransaction)
        .then(receipt=>{
                onConfirm(receipt);
        });
    });
}