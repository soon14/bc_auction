var auctionView = Vue.component('AuctionView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 참여하기" description="경매 중인 작품을 보여줍니다."></v-breadcrumb>
            <div id="auction-list" class="container">
                <div class="row">
                    <div class="col-md-12 text-right">
                        <router-link :to="{ name: 'auction.regsiter' }" class="btn btn-outline-secondary">경매 생성하기</router-link>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 auction" v-for="item in auctions">
                        <div class="card">
                            <div class="card-body">
                                <img src="./assets/images/artworks/artwork1.jpg">
                                <h4>{{ item['auction_goodsid']['art_name'] }}</h4>
                                <p>{{ calculateDate(item['auction_start'], item['auction_end'] ) }}</p>
                                <router-link :to="{ name: 'auction.detail', params: { id: item['auction_id'] }}" class="btn btn-block btn-secondary">자세히보기</router-link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            auctions: []
        }
    },
    methods: {
        calcTime(time){
            var difference = time / 1000;
                var secs = difference % 60
                difference = parseInt(difference / 60)
                var minutes = difference % 60
                difference = parseInt(difference / 60)
                var hours = difference % 24
                difference = parseInt(difference / 24)
                var days = difference;

                var timeInfo = {'day' : days, 'hour' : hours, 'minute' : minutes, 'sec': secs};
                return timeInfo;
        },
        calculateDate(sdate, edate) {
            var now = new Date().getTime();
            var startDate = new Date(sdate);
            var endDate = new Date(edate);

            endDate=endDate.setHours(endDate.getHours()+9);
            var plus9StartDate = startDate.setHours(startDate.getHours() + 9);
           
            var endDiff = endDate-now;

            // 만약 종료일자가 지났다면 "경매 마감"을 표시한다.
            if( startDate > now) {
                var startDiff = startDate - now;
                var timeInfo = this.calcTime(startDiff);
                return "경매 예정 ( " + timeInfo.hour + "h" + timeInfo.minute + "m 후 )";
            }
            if(endDiff < 0) {
                return "경매 마감";
            } else {
                // UNIX Timestamp를 자바스크립트 Date객체로 변환한다.
                var timeInfo = this.calcTime(endDiff);
                return "남은시간: " + timeInfo.day + "일 " + timeInfo.hour + "시간 ";
            }
        }
    },
    mounted: function(){
        var scope = this;

        auctionService.findAll(function(data){
            var result = data;
            console.log("auctionService.findAll를 호출한 모든 옥션 ", result)

            // 각 경매별 작품 정보를 불러온다.
            function fetchData(start, end){
                if(start == end) {
                    scope.auctions = result;
                } else {
                    // 경매작품 id
                    var id = result[start]['auction_goodsid'];
                    workService.findById(id, function(work){
                        // 작품정보
                        // console.log("findById(id) work", work)
                        result[start]['auction_goodsid'] = work;
                        fetchData(start+1, end);
                    });
                }
            }
            fetchData(0, result.length);
        });
    }
});