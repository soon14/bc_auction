var explorerAuctionDetailView = Vue.component('ExplorerDetailView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Auction Explorer" description="블록체인에 기록된 경매내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <div class="alert alert-warning" v-if="contract && contract.highestBid == 0">
                        아직 경매에 참여한 이력이 없습니다.
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th colspan="2"># {{ contractAddress }}</th>
                            </tr> 
                        </thead>
                        <tbody>
                            <tr>
                                <th width="20%">Contract Address</th>
                                <td>{{ contractAddress }}</td>
                            </tr>
                            <tr>
                                <th width="20%">작품</th>
                                <td><router-link :to="{ name: 'work.detail', params: { id: work['art_id'] }}">{{ work && work['art_mem'] }}</router-link></td>
                            </tr>
                            <tr>
                                <th>Status</th>
                                <td>
                                    <span class="badge badge-primary" v-if="contract && !contract.aucInfo_close">Processing</span>
                                    <span class="badge badge-danger" v-if="contract && contract.aucInfo_close">Ended</span>
                                </td>
                            </tr>
                            <tr>
                                <th>Start Time Time</th>
                                <td>{{ contract && contract.aucInfo_start.toLocaleString() }}</td>
                            </tr>
                            <tr>
                                <th>Expire Time</th>
                                <td>{{ contract && contract.aucInfo_end.toLocaleString() }}</td>
                            </tr>
                            <tr>
                                <th>Highest Bid</th>
                                <td>{{ contract && contract.aucInfo_highest }} ETH</td>
                            </tr>
                            <tr>
                                <th>Highest Bidder</th>
                                <td>
                                    <span v-if="contract && contract.aucInfo_highest == 0">-</span>
                                    <span v-if="contract && contract.aucInfo_highest != 0">{{ contract && contract.aucInfo_highestBider }}</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `,
    data(){
        return {
            contractAddress: "",
            contract: null,
            work: {
                id: 0
            }
        }
    },
    mounted: async function(){
        /**
         * TODO 경매 컨트랙트로부터 경매 정보를 가져옵니다. 
         */
        var scope = this;
        scope.contractAddress = this.$route.params.contractAddress
        explorerService.call_auction_byContract(scope.contractAddress, function(data){
            scope.contract = data
            console.log("auctionContractService.경매정보조회 ", data)
            scope.work.id = data.aucInfo_artId
            workService.findById(scope.work.id, function(data){
                console.log("workService.findById 작품조회", data)
                scope.work = data
            })
        })
    }
})