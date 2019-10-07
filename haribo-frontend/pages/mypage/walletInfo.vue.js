var walletInfoView = Vue.component('walletInfoView', {
    template: `
        <div>
            <div id="cover"></div>
            <v-nav></v-nav>
               <v-breadcrumb title="마이페이지" description="지갑을 생성하거나 작품을 업로드 할 수 있습니다."></v-breadcrumb>
                <div class="container">
                    <v-mypage-nav></v-mypage-nav>
                    <div id="mywallet-info" class="row">
                        <div class="col-md-12 mt-5">
                            <div class="card">
                                <table class="table table-bordered">
                                    <tr>
                                        <th>총보유 ETH</th>
                                        <td class="text-right">{{ wallet.wallet_money }} ETH</td>
                                        <td colspan="2">
                                            <button type="button" class="btn btn-secondary" v-on:click="charge()" v-bind:disabled="isCharging">{{ isCharging ? "충전중" : "ETH 충전하기"}}</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>내 지갑주소</th>
                                        <td class="text-right">{{ wallet.wallet_addr }}</td>
                                        <th>충전 회수</th>
                                        <td class="text-right">{{ wallet.wallet_chargecount }}회</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    `,
    data() {
        return {
            wallet: {
                wallet_id: 0,
                wallet_mem: 15,
                wallet_addr: "ss",
                wallet_money: 0,
                wallet_chargecount: 0
            },
            isCharging: false,          // 현재 코인을 충전하고 있는 중인지 확인하는 변수
            sharedState: store.state
        }
    },
    methods: {
        // ETH 충전하기
        charge: function () {
            if(this.wallet.wallet_chargecount<10){
                $("#cover").show("fast");
                var scope = this;
                scope.isCharging = true;
                walletService.chargeEther(this.wallet.wallet_addr, function (response) {
                    scope.isCharging = false;
                    alert("코인이 충전 되었습니다.");
                    scope.wallet=response;
                    $('#cover').hide("fast");
                })
            }else{
                alert("충전횟수(최대 10번)를 초과합니다")
            }
        },
        // 지갑 정보 가져오기
        fetchWalletInfo: function () {
            var scope = this;
            walletService.findById(this.sharedState.user.id, function (data) {
                scope.wallet=data
            });
        }
    },
    mounted: function () {
        this.fetchWalletInfo();
    }
})