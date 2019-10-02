var explorerTxSearchView = Vue.component('ExplorerTxSearchView',{
    template : `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Search Address" description="블록체인 계정의 검색기능을 제공합니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav/>
            <div class="w3-container">
                <label class="w3-text-blue"><b>Input Address : </b></label>
                <input class="w3-input w3-border" style="display : inline-block; width:50%" v-model="search_data" @keyup.enter="move(search_data)"></br>
                <div v-if="isExist==true" >
                    <label class="w3-text-blue"><b>Total Balance : {{tx.balance}} Ether</b></label></br>
                    <label class="w3-text-blue"><b>Total TxCount : {{tx.txCount}}</b></label></br>

                    <table class="w3-table-all">
                        <thead>
                            <tr class="w3-blue">
                                <th width="25%">Txn Hash</th>
                                <th width="10%">Block</th>
                                <th width="30%">Time</th>
                                <th width="30%">From</th>
                                <th width="30%">To</th>
                                <th width="10%">Value(Ether)</th>
                            </tr>
                        </thead>
                        <tr v-for="item in tx.trans" v-if="item.from==search_data || item.to==search_data">
                            <td style="color : blue"><router-link :to="{name: 'explorer.tx.detail', params: { hash: item.txHash }}" class="tx-number">{{ item.txHash | truncate(20) }}</router-link></td>
                            <td>{{ item.blockId | truncate(15) }}</td>
                            <td>{{ item.timestamp | truncate(15) }}</td>

                            <td v-if="search_data.toLowerCase()!=item.from" style="color : red">
                                <router-link :to="{name: 'explorer.search2', params: { address: item.from }}" class="tx-number">{{ item.from | truncate(20) }}</router-link>
                            </td>
                            <td v-else>{{ item.from | truncate(20) }}</td>
                            <td v-if="search_data.toLowerCase()!=item.to" style="color : red">
                                <router-link :to="{name: 'explorer.search2', params: { address: item.to }}" class="tx-number">{{ item.to | truncate(20) }}</router-link>
                            </td>
                            <td v-else>{{ item.to | truncate(20) }}</td>

                            <td>{{ item.amount | truncate(10) }}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `,
    data(){
        return {
            search_data: null,
            isExist : false,
            tx: {
                txCount : null,
                balance : null,
                trans : []
            }
        }
    },
    methods: {
        move : function(data){
            router.push("/explorer/search/"+data);
        },
        search : function(data){
            var scope=this
            explorerService.call_tx_byAddress(data,function(res){
                scope.tx.txCount=res.txCount
                scope.tx.balance=res.balance/Math.pow(10,18)
                scope.tx.trans=[]
                for(var tmp in res.trans){
                    console.log(res.trans[tmp].amount)
                    res.trans[tmp].timestamp=explorerService.timeSince(res.trans[tmp].timestamp)
                    res.trans[tmp].amount=res.trans[tmp].amount/Math.pow(10,18)
                    scope.tx.trans.unshift(res.trans[tmp])
                }
                scope.isExist=true;
            })
        }
    },
    watch : {
        '$route' : function(){
            this.search_data=this.$route.params.address
            this.search(this.search_data)
        }
    },
    mounted(){
        this.search_data=this.$route.params.address
        if(this.search_data!=null)
            this.search(this.search_data)
    }
})