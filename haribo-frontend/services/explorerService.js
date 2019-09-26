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
    // 최신블록기준 10개 블록 가져오기
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
    }
}