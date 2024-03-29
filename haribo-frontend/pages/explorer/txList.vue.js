var explorerTxListView = Vue.component('ExplorerTxListView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Transaction Explorer" description="블록체인에서 생성된 거래내역을 보여줍니다."></v-breadcrumb>
            <div class="container">
                <explorer-nav></explorer-nav>
                <div class="row" v-if="transactions.length == 0">
                    <div class="col-md-8 mx-auto">
                        <div class="alert alert-warning">No transaction recorded at blocks</div>
                    </div>
                </div>
                <div class="row">
                    <div id="transactions" class="col-md-8 mx-auto">
                        <div class="card shadow-sm">
                            <div class="card-header">Transactions</div>
                            <div class="card-body">
                                <div class="row tx-info" v-for="item in transactions">
                                    <div class="col-md-2">Tx</div>
                                    <div class="col-md-4">
                                        <router-link :to="{name: 'explorer.tx.detail', params: { hash: item.txHash }}" class="tx-number">{{ item.txHash | truncate(10) }}</router-link>
                                        <p class="tx-timestamp">{{ item.timestamp }}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><label class="text-secondary">From</label> {{ item.from | truncate(10) }}</p>
                                        <p><label class="text-secondary">To</label> {{ item.to | truncate(10) }}</p>
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
            transactions: []
        };
    },
    methods: {
        fetchTxes: function(){
            //TODO 최근 트랜잭션 10개 리스트를 반환합니다.
            var scope=this
            explorerService.call_txList(function(data){
                scope.transactions=data
                for(var tmp in data){
                    data[tmp].timestamp=explorerService.timeSince(data[tmp].timestamp)
                }
            })             
        }      
    },
    mounted: function(){
        this.fetchTxes();

        this.$nextTick(function () {
            window.setInterval(() => {
                this.fetchTxes();
            }, REFRESH_TIMES_OF_TRANSACTIONS);
        })
    }
})