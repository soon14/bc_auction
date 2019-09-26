var auctionService = {
    // 전체 경매 내역 조회
    findAll: function(callback){
        $.get(API_BASE_URL + '/api/auctions', function(data){
            callback(data);
        });
    },
    findAllByUser: function(userId, callback){
      $.get(API_BASE_URL + '/api/auctions/owner/' + userId, function(data){
        callback(data);
      });
    },
    register: function(data, callback){
        $.ajax({
            type: "POST",
            url: API_BASE_URL + "/api/auctions",
            data: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json' },
            success: callback,
            error : function(error) {
                console.log("error", error)
                console.log("넘겨온 data인가? ", data)
            }
        });
    },
    findById: function(id, callback){
        $.get({
            url : API_BASE_URL + "/api/auctions/" + id,
            success : callback,
            error : function(error) {
                console.log("error", error)
                console.log("http get AuctionInfo ", callback)
            }
        });
    },
    // 경매 내역 저장
    saveBid: function(bidder, auctionId, bidPrice, callback){
        var data = {
            "bid_mem": bidder,
            "bid_auction": auctionId,
            "bid_price": bidPrice,
            "bid_date": new Date()
        }
        $.ajax({
            type: "PUT",
            url: API_BASE_URL + "/api/auctions/bid",
            data: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json' },
            success: callback
        })
    },
    // 경매 취소
    cancel: function(auctionId, bidderId, callback, whenError){
        $.ajax({
            type: "DELETE",
            url: API_BASE_URL + "/api/auctions/" + auctionId + "/by/" + bidderId,
            success: callback,
            error: whenError
        });
    },
    // 경매 종료
    close: function(auctionId, bidderId, callback, whenError){
        $.ajax({
            type: "PUT",
            url: API_BASE_URL + "/api/auctions/" + auctionId + "/by/" + bidderId,
            success: callback,
            error: whenError
        });
    }
}
