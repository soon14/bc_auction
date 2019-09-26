var explorerBlockView = Vue.component('ExplorerBlockView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Block Explorer" description="블록체인에서 생성된 블록내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <div id="blocks" class="col-md-8 mx-auto">
                        <div class="card shadow-sm">
                            <div class="card-header">Blocks</div>
                            <div class="card-body">
                                <div class="row block-info" v-for="item in blocks">
                                    <div class="col-md-2">BK</div>
                                    <div class="col-md-4">
                                        <router-link :to="{name:'explorer.block.detail', params: {blockNumber:item.blockNo}}" class="block-number">{{item.blockNo}}</router-link>
                                        <p class="block-timestamp">{{ item.timestamp }}</p>
                                    </div>
                                    <div class="col-md-6 text-right">
                                        <p class="block-num-transactions">{{ item.trans.length }} Txes</p>
                                    </div>
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
            lastReadBlock: 0,
            blocks: []
        }
    },
    methods: {
        fetchBlocks: function(){
            var scope = this;
             //data의 멤버변수는 백엔드의 block wrapper 클래스형태로 넘어옴
            explorerService.call_Blocklist(function(data){
                scope.blocks=data
                for(var tmp in data){
                    data[tmp].timestamp=explorerService.timeSince(data[tmp].timestamp)
                }
            })
        }
    },
    mounted: function(){
        this.fetchBlocks();

        this.$nextTick(function () {
            window.setInterval(() => {
                this.fetchBlocks();
            }, REFRESH_TIMES_OF_BLOCKS);
        })
    },
    watch : {
        
    }
})