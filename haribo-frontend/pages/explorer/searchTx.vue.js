var explorerTxSearchView = Vue.component('ExplorerTxSearchView',{
    template : `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Transaction Explorer" description="블록체인에서 생성된 거래내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav/>
            <input v-model="search_data" @keyup.enter="search(search_data)">
            
            <div class="row" v-if="isExist==false">
                <div class="col-md-8 mx-auto">
                    <div class="alert alert-warning">No Found Search Transation</div>
                </div>
            </div>
            
            <div class="row" v-else class="col-md-12">
                <div class="card shadow-sm">
                    <div class="card-header"><strong>{{ tx.hash }}</strong></div>
                    <table class="table">
                        <tbody>
                            <tr>
                                <th width="200">트랜잭션 해시</th>
                                <td>{{ tx.hash }}</td>
                            </tr>
                            <tr>
                                <th>블록 넘버</th>
                                <td>{{ tx.blockNumber }}</td>
                            </tr>
                            <tr>
                                <th>날짜</th>
                                <td>{{ tx.timestamp }}</td>
                            </tr>
                            <tr>
                                <th>송신자 주소</th>
                                <td><router-link :to="{ name: 'address', params: { address: tx.from }}">{{ tx.from }}</router-link></td>
                            </tr>
                            <tr>
                                <th>수신자 주소</th>
                                <td><router-link :to="{ name: 'address', params: { address: tx.to }}">{{ tx.to }}</router-link></td>
                            </tr>
                            <tr>
                                <th>전송한 이더</th>
                                <td>{{ tx.value }} Ether</td>
                            </tr>
                            <tr>
                                <th>Gas</th>
                                <td>{{ tx.gas }}</td>
                            </tr>
                            <tr>
                                <th>Gas Price</th>
                                <td>{{ tx.gasPrice }}</td>
                            </tr>
                            <tr>
                                <th>Input Data</th>
                                <td>
                                    <textarea class="form-control" readonly>{{ tx.input }}</textarea>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </div>
    </div>
    `,
    data(){
        return {
            search_data: "",
            isExist: false, 
            tx: {
                hash: "-",
                timestamp: "-"
            }
        }
    },
    methods: {
        searach : function(data){
            alert(data)
        }
    }
})