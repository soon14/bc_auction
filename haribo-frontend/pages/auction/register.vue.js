/**
 * 화면: 경매 등록하기
 */

var auctionRegisterView = Vue.component('AuctionRegisterView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 등록하기" description="새로운 경매를 등록합니다."></v-breadcrumb>
            <div class="row">
                <div class="col-md-6 mx-auto">
                    <div class="card">
                        <div class="card-header">신규 경매 등록하기</div>
                        <div class="card-body">
                            <div v-if="!registered">
                                <div class="form-group">
                                    <label id="privateKey">지갑 개인키</label>
                                    <input id="privateKey" v-model="before.input.privateKey" type="text" class="form-control" placeholder="지갑 개인키를 입력해주세요.">
                                </div>
                                <div class="form-group">
                                    <label id="work">작품 선택</label>
                                    <select v-model="before.selectedWork" class="form-control">
                                        <option v-for="work in before.works" :value="work['art_id']">{{ work['art_name'] }}</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label id="minPrice">최저가</label>
                                    <div class="input-group">
                                        <input id="minPrice" v-model="before.input.minPrice" type="text" class="form-control" placeholder="최저가를 입력해주세요.">
                                        <div class="input-group-append">
                                            <div class="input-group-text">ETH</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label id="startDateLabel">경매 시작일시</label>
                                    <input id="startDate" v-model="before.input.startDate" type="text" class="form-control" placeholder="2019-04-21 21:00:00">
                                </div>
                                <div class="form-group">
                                    <label id="untilDateLabel">경매 종료일시</label>
                                    <input id="untilDate" v-model="before.input.untilDate" type="text" class="form-control" placeholder="2019-05-03 12:00:00">
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <button class="btn btn-sm btn-primary" v-on:click="register" v-bind:disabled="isCreatingContract">{{ isCreatingContract ? "계약을 생성하는 중입니다." : "경매 등록하기" }}</button>
                                    </div>
                                    <div class="col-md-6 text-right">
                                        <button class="btn btn-sm btn-outline-secondary" v-on:click="goBack">이전으로 돌아가기</button>
                                    </div>
                                </div>
                            </div>
                            <div v-if="registered">
                                <div class="alert alert-success" role="alert">
                                    경매가 생성되었습니다.
                                </div>
                                <table class="table table-bordered mt-5">
                                    <tr>
                                        <th>경매작품</th>
                                        <td>{{ after.work['art_name'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>최저가</th>
                                        <td>{{ after.result['auction_min'] }} ETH</td>
                                    </tr>
                                    <tr>
                                        <th>시작일시</th>
                                        <td>{{ after.result['auction_start'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>종료일시</th>
                                        <td>{{ after.result['auction_end'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>컨트랙트 주소</th>
                                        <td>{{ after.result['auction_contract'] }}</td>
                                    </tr>
                                </table>
                                <div class="row mt-5">
                                    <div class="col-md-6">
                                        <router-link :to="{ name: 'auction' }" class="btn btn-sm btn-outline-secondary">경매 리스트로 돌아가기</router-link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            isCreatingContract:false,
            registered: false,
            sharedStates: store.state,

            // 경매 등록전 입력값
            before: {
                works: [],
                selectedWork: null,
                input: {

                }
            },

            // 경매 등록 후 등록 결과 완료 표시 용도
            after: {
                result: {},
                work: {}
            }
        }
    },
    watch :{
        'before.selectedWork' : function(val){
            var scope = this;
            auctionService.findAuctionByWorkID(this.before.selectedWork, function(data){
                result = data;
                if( result != 0) {
                    alert('현재 작품은 이미 경매 중입니다.');
                    scope.before.selectedWork=null;
                }
            });
        }
    },
    methods: {
        goBack: function(){
            this.$router.go(-1);
        },
        register: function(){
           /**
            * 계약 생성하기 버튼 클릭시 작동
             * 컨트랙트를 호출하여 경매를 생성하고
             * 경매 정보 등록 API를 호출합니다. 
             */
            var scope = this;
            this.isCreatingContract = true;
            
            var start = $('#startDate').val();
            var end = $('#untilDate').val();
            
            // 1. 내 지갑 주소를 가져옵니다.
            walletService.findAddressById(this.sharedStates.user.id, function(walletAddress){
                
                // 2. 경매 컨트랙트를 블록체인에 생성합니다.
                // components/auctionFactory.js의 createAuction 함수를 호출합니다.
                // TODO createAuction 함수의 내용을 완성합니다. 
                
                createAuction({
                    workId: scope.before.selectedWork,
                    minValue: web3.utils.toWei(scope.before.input.minPrice, 'ether'),
                    startTime: new Date(start).getTime(),
                    endTime: new Date(end).getTime()
                }, walletAddress, scope.before.input.privateKey, function(log){
                    console.log("경매 생성 시 log", log);
                    var contractAddress = log.newAuction;
                    console.log("경매 생성 시 log.newAuction", contractAddress);
                    
                    var data = {
                        "auction_makerid": scope.sharedStates.user.id,
                        "auction_goodsid": scope.before.selectedWork,
                        "auction_start": new Date(start),
                        "auction_end": new Date(end),
                        "auction_min": Number(scope.before.input.minPrice),
                        "auction_contract": contractAddress,
                    }

                    // DB에 넣을 옥션 데이터
                    console.log("DB에 넣을 옥션 데이터(객체) ",data)

                    // 3. 선택한 작업 정보를 가져옵니다.
                    workService.findById(scope.before.selectedWork, function(result){
                        console.log("workService.findById ", result)
                        scope.after.work = result;
                    });
                    
                    // 4. 생성한 경매를 등록 요청 합니다.
                    auctionService.register(data, function(result){
                        console.log("auction register result ", result)
                        alert("경매가 등록되었습니다.");
                        scope.registered = true;
                        scope.after.result = data;
                    });

                    this.isCreatingContract = false;
                }); 
            });
        }
    },
    mounted: function(){
        var scope = this;
        
        // 내 작품 목록 가져오기
        workService.findWorksByOwner(this.sharedStates.user.id, function(result){
            scope.before.works = result;
        });
        
        $('#startDate').datetimepicker({ 
            footer: true,
            modal: true,
            format: 'yyyy-mm-dd HH:MM:ss'
          });

        $('#untilDate').datetimepicker({ 
            footer: true,
            modal: true,
            format: 'yyyy-mm-dd HH:MM:ss'
          });
    }
})