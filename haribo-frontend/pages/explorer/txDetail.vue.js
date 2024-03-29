var explorerTxDetailView = Vue.component('ExplorerTxDetailView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Transaction Explorer" description="블록체인에서 생성된 거래내역을 보여줍니다."></v-breadcrumb>
            <div class="container">
                <explorer-nav></explorer-nav>
                <div class="row">
                    <div class="col-md-12">
                        <div class="card shadow-sm">
                            <div class="card-header"><strong>{{ tx.hash }}</strong></div>
                            <table class="table">
                                <tbody>
                                    <tr>
                                        <th width="200">트랜잭션 해시</th>
                                        <td>{{ tx.txHash }}</td>
                                    </tr>
                                    <tr>
                                        <th>블록 넘버</th>
                                        <td>{{ tx.blockId }}</td>
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
                                        <td>{{ tx.amount }} Ether</td>
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
            isExist: true, 
            tx: { }
        }
    },
    mounted: function(){

        var hash=this.$route.params.hash
        var scope=this
        if(hash) {
            explorerService.call_detailTx(hash,function(data){
                scope.tx=data
                scope.tx.timestamp=new Date(data.timestamp)
            })
         } else {
             this.isValid = false;  
         }
    }
})