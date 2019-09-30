var auctionDetailView = Vue.component('AuctionDetailView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 작품 상세 정보" description="선택하신 경매 작품의 상세 정보를 보여줍니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <table class="table table-bordered">
                                    <tr>
                                        <th width="20%">생성자</th>
                                        <td><router-link :to="{ name: 'work.by_user', params: { id: creator['mem_id'] } }">{{ creator['mem_name'] }}({{creator['mem_mail']}})</router-link></td>
                                    </tr>
                                    <tr>
                                        <th>작품명</th>
                                        <td>{{ work['art_name'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>작품 설명</th>
                                        <td>{{ work['art_detail'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>경매 시작일</th>
                                        <td>{{ auction['aucInfo_start'] }}</td>
                                    </tr>   
                                    <tr>
                                        <th>경매 종료일</th>
                                        <td>{{ auction['aucInfo_end'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>최저가</th>
                                        <td><strong>{{ auction['aucInfo_min'] }} ETH</strong></td>
                                    </tr>
                                    <tr>
                                        <th>컨트랙트 주소</th>
                                        <td><a href="#">{{ auction['aucInfo_contract'] }}</a></td>
                                    </tr>
                                    <tr>
                                        <th>상태</th>
                                        <td>
                                            <span class="badge badge-success" v-if="auction['aucInfo_close'] == false">경매 진행중</span>
                                            <span class="badge badge-danger" v-if="auction['aucInfo_close'] == true">경매 종료</span>
                                        </td>
                                    </tr>
                                </table>
                                <table class="table table-bordered mt-3" v-if="bidder.mem_id">
                                    <tr>
                                        <th width="20%">현재 최고 입찰자</th>
                                        <td>{{ bidder['mem_name'] }}({{ bidder['mem_mail'] }})</td>
                                    </tr>
                                    <tr>
                                        <th>현재 최고 입찰액</th>
                                        <td>{{ auction['aucInfo_highest'] }} ETH</td>
                                    </tr>
                                </table>
                                <div class="alert alert-warning mt-3" role="alert" v-if="!bidder.mem_id">
                                    입찰 내역이 없습니다.
                                </div>
                                <div class="alert alert-danger mt-3" role="alert" v-if="auction['aucInfo_close'] == true">
                                    경매가 종료되었습니다.
                                </div>
                                <div class="row mt-5">
                                    <div class="col-md-6">
                                        <router-link :to="{ name: 'auction' }" class="btn btn-sm btn-outline-secondary">경매 리스트로 돌아가기</router-link>
                                    </div>
                                    <div class="col-md-6 text-right" v-if="sharedStates.user.id == work['art_mem'] && auction['aucInfo_close'] != true">
                                        <button type="button" class="btn btn-sm btn-primary" v-on:click="closeAuction" v-bind:disabled="isCanceling || isClosing">{{ isClosing ? "낙찰중" : "낙찰하기" }}</button>
                                        <button type="button" class="btn btn-sm btn-danger" v-on:click="cancelAuction" v-bind:disabled="isCanceling || isClosing">{{ isCanceling ? "취소하는 중" : "경매취소하기" }}</button>
                                    </div>
                                    <div class="col-md-6 text-right" v-if="sharedStates.user.id != work['art_mem'] && auction['aucInfo_close'] != true">
                                        <router-link :to="{ name: 'auction.bid', params: { id: this.$route.params.id } }" class="btn btn-sm btn-primary">입찰하기</router-link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            work: {},
            creator: {id:0},
            auction: {},
            sharedStates: store.state,
            bidder: {},
            isCanceling: false,
            isClosing: false,
            wallet:{},
        }
    },
    methods: {
        closeAuction: function(){
            /**
             * 컨트랙트를 호출하여 경매를 종료하고
             * 경매 상태 업데이트를 위해 API를 호출합니다. 
             */
            var scope = this;
            var privateKey = window.prompt("경매를 종료하시려면 지갑 비밀키를 입력해주세요.","");
            
            // register.vue.js, bid.vue.js를 참조하여 완성해 봅니다. 
            walletService.findAddressById(this.sharedStates.user.id, function(walletAddress){
                var options = {
                    contractAddress: scope.auction['aucInfo_contract'],
                    walletAddress: walletAddress,
                    privateKey: privateKey,
                    auctionId : scope.$route.params.id,
                };

                
                auction_close(options, function(receipt){

                    /**
                     *  AuctionService.java : 경매종료(final long 경매id, final long 회원id)
                     */
                    auctionService.close(scope.$route.params.id, receipt.bidder, 
                        function(auction){

                        }, 
                        function(error){

                        });
                });
            });

            
        },
        cancelAuction: function(){
            /**
             * 컨트랙트를 호출하여 경매를 취소하고
             * 경매 상태 업데이트를 위해 API를 호출합니다. 
             */
            var scope = this;
            var privateKey = window.prompt("경매를 취소하시려면 지갑 비밀키를 입력해주세요.","");
            console.log('privateKey', privateKey);
            
            // register.vue.js, bid.vue.js를 참조하여 완성해 봅니다.
            walletService.findAddressById(this.sharedStates.user.id, function(walletAddress){
                var options = {
                    contractAddress: scope.auction['aucInfo_contract'],
                    walletAddress: walletAddress,
                    privateKey: privateKey,
                    auctionId : scope.$route.params.id,
                };

                auction_cancel(options, function(receipt){
                    auctionService.cancel(scope.$route.params.id, receipt.bidder, 
                        function(auction){
                            console.log(auction);
                        }, 
                        function(error){

                        });
                });
            });
        }
    },
    mounted: async function(){
        var auctionId = this.$route.params.id;
        var scope = this;
        var web3 = createWeb3();

        // 경매 정보 조회
        auctionService.findById(auctionId, function(auction){
            console.log("경매 정보 조회 AuctionInfo ", auction)
            var amount = Number(auction['aucInfo_min']).toLocaleString().split(",").join("")
            auction['aucInfo_min'] = web3.utils.fromWei(amount, 'ether');
            

            var workId = auction['aucInfo_artId'];

            // 작품 정보 조회
            workService.findById(workId, function(work){
                console.log("작품 정보 조회 DigitalWork", work);
                scope.work = work;
                var creatorId = work['art_mem'];

                // 생성자 정보 조회
                userService.findById(creatorId, function(user){
                    console.log("생성자 정보 조회 Member", user)
                    scope.creator = user;
                });
            });

            // 입찰자 조회
            if(auction['aucInfo_highest'] > 0) {
                var amount = Number(auction['aucInfo_highest']).toLocaleString().split(",").join("")
                auction['aucInfo_highest'] = web3.utils.fromWei(amount, 'ether');
                var bidderId = auction['aucInfo_highestBider'];

                userService.findById(bidderId, function(user){
                    console.log("입찰자 조회", user)
                    scope.bidder = user;
                });
            }

            scope.auction = auction;
        });
    }
});