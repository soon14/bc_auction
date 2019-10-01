var explorerAuctionView = Vue.component('ExplorerView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Auction Explorer" description="블록체인에 기록된 경매내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Auction</th>
                                <th>Status</th>
                                <th>Highest Bid</th>
                                <th>Highest Bidder</th>
                                <th>Expire Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in items">
                                <td><router-link :to="{ name: 'explorer.auction.detail', params: { contractAddress: item.aucInfo_contract } }">{{ item.aucInfo_artId | truncate(15) }}</router-link></td>
                                <td>
                                    <span class="badge badge-primary" v-if="items[index] && !items[index].aucInfo_end">Processing</span>
                                    <span class="badge badge-danger" v-if="items[index] && items[index].aucInfo_end">Ended</span>
                                </td>
                                <td>{{ items[index] && items[index].aucInfo_highest }} ETH</td>
                                <td>
                                    <span v-if="items[index] && items[index].higestBid != 0">{{ items[index] && items[index].aucInfo_highestBider | truncate(15) }}</span>
                                    <span v-if="items[index] && items[index].aucInfo_highest == 0">-</span>
                                </td>
                                <td>{{ items[index] && items[index].aucInfo_end.toLocaleString() }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `,
    data() {
        return {
            contracts: [],
            items: [],
        }
    },
    mounted: async function () {
        var scope = this;
        /**
         * TODO 
         * 1. AuctionFactory 컨트랙트로부터 경매컨트랙트 주소 리스트를 가져옵니다.
         * 2. 각 컨트랙트 주소로부터 경매의 상태(state) 정보를 가져옵니다. 
         * */
        explorerService.call_auction_list(function (data) {
            scope.contracts = data.aucInfo_contract
            
            scope.items = data
            console.log("auctionContractService의 경매컨트랙트주소리스트 ", scope.items)
        })
    }
})