var explorerService = {
    
    // timestamp 포맷을 사람이 읽을 수 있는 형태로 변환한다.(아직 안됨 ->추후 확인)
    timeSince : function(date){
        var seconds = Math.floor((new Date() - (date * 1000)) / 1000);
        var interval = Math.floor(seconds / 31536000);

        if (interval > 1) {
            return interval + " years ago";
        }
        interval = Math.floor(seconds / 2592000);
        if (interval > 1) {
            return interval + " months ago";
        }
        interval = Math.floor(seconds / 86400);
        if (interval > 1) {
            return interval + " days ago";
        }
        interval = Math.floor(seconds / 3600);
        if (interval > 1) {
            return interval + " hours ago";
        }
        interval = Math.floor(seconds / 60);
        if (interval > 1) {
            return interval + " minutes ago";
        }
        return Math.floor(seconds) + " seconds ago";
    },
    // 최신 블록 10개 블록 가져오기
    call_Blocklist : function(callback){
        $.get(API_BASE_URL + "/api/eth/blocks", function(data){
            callback(data);
        });
    },
    // 특정블록의 상세정보 가져오기
    call_detailBlock : function(id, callback){
        $.get(API_BASE_URL + "/api/eth/block/" + id, function(data){
            callback(data)
        });
    },
    // 최신 트랜잭션 10개 가져오기
    call_txList : function(callback){
        $.get(API_BASE_URL + "/api/eth/trans",function(data){
            callback(data)
        })
    },
    // 특정트랜잭션의 상세정보 가져오기
    call_detailTx : function(hash, callback){
        $.get(API_BASE_URL + "/api/eth/trans/" + hash, function(data){
            callback(data)
        });
    },
    // 지갑 주소로 검색하여 주소와 관련된 트랜잭션 리스트 가져오기
    call_tx_byAddress : function(address, callback){
        $.get({
            url :API_BASE_URL + "/api/eth/address/"+address, 
            success : callback,
            error : function(error){
                alert("데이터를 조회할 수 없습니다.")
            }
        })
    },
    // 전체 경매 목록 불러오기
    call_auction_list : function(callback){
        $.get({
            url : API_BASE_URL + "/api/eth/auctions/",
            success : callback,
            error : function(error) {
                console.log("error", error)
            }
        });
    },
    // 경매 컨트랙트로부터 경매 정보를 가져오기
    call_auction_byContract : function(contract, callback) {
        $.get({
            url : API_BASE_URL + "/api/eth/auctions/"+contract,
            success : callback,
            error : function(error) {
                console.log("error", error)
            }
        }); 
    }
}

