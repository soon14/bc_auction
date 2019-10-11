var walletService = {
    findWalletByaddr: function(addr, callback){
        $.get(API_BASE_URL + "/api/wallets/" + addr, function(data){
            callback(data);
        });
    },
    findAddressById: function(id, callback){
        $.get(API_BASE_URL + "/api/wallets/of/" + id, function(data){
            callback(data['wallet_addr']);
        });
    },
    findById: function(id, callback){
        try{
            $.get(API_BASE_URL + "/api/wallets/of/" + id, function(data){
                callback(data);
            });
        }
        catch(error){
            callback(error)
        }
    },
    isValidPrivateKey: function(id, privateKey, callback){
        var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
        var account = web3.eth.accounts.privateKeyToAccount(privateKey);

        this.findById(id, function(data){
            var address = data['wallet_addr'];
            callback(account && account.address == address);
        });
    },
    registerWallet: function(userId, walletAddress, callback){
        // TODO 지갑 등록 API를 호출합니다.
        var body={
            "wallet_addr" : walletAddress,
            "wallet_mem" : userId
        }
        $.ajax({
            type : "POST",
            url : API_BASE_URL+"/api/wallets",
            data : JSON.stringify(body),
            headers: { 'Content-Type': 'application/json' },
            success: function(response){
                callback(response);
            }
        });
        
    },
    chargeEther: function(walletAddress, callback){
        console.log("충전요청");
        console.log(walletAddress);
        // TODO 코인 충전 API를 호출합니다.
        var body={
            "wallet_addr" : walletAddress
        }
        $.ajax({
            type : "PUT",
            url : API_BASE_URL+"/api/wallets/"+walletAddress,
            data : JSON.stringify(body),
            headers: { 'Content-Type': 'application/json' },
            success : function(response) {
                console.log("충전 중");
                console.log(response);
                callback(response);
            }
        })
    }
}