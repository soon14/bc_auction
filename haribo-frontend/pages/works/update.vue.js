var worksUpdateView = Vue.component("worksUpdateView", {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="작품 등록" description="새로운 작품을 등록합니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-8 mx-auto">
                        <div class="card">
                            <div class="card-body">
                                <div class="form-group">
                                    <label id="name">작품 이름</label>
                                    <input type="text" class="form-control" id="name" v-model="work.name">
                                </div>
                                <div class="form-group">
                                    <label id="description">작품 설명</label>
                                    <textarea class="form-control" id="description" v-model="work.description"></textarea>
                                </div>
                                <div class="form-group">
                                    <label id="isActive">공개여부</label><br>
                                    <input type="checkbox" id="isActive" v-model="work.isActive">
                                </div>
                                <div class="form-group">
                                    <label id="status">상태</label><br>
                                    <input type="checkbox" id="status" v-model="work.status">
                                </div>
                                <button type="button" class="btn btn-primary" v-on:click="update">작품 등록하기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            work: {
                name: "",
                description: "",
                isActive: false,
                status: false,
                ownerId: 0
            },
            sharedStates: store.state
        }
    },
    methods: {
        update: function(){
            var scope = this;
            var workId = this.$route.params.id;

            workService.update({
                "art_id": workId,
                "art_mem": this.work.ownerId,
                "art_name": this.work.name,
                "art_detail": this.work.description,
                "art_isopen": this.work.isActive ? "Y" : "N",
                "art_status": this.work.status ? "Y" : "N"
            },
            function(){
                alert('작품이 수정되었습니다.');
                scope.$router.push('/works/detail/' + workId);
            }, 
            function(error){
                alert("입력폼을 모두 입력해주세요.");
            });
        }
    },
    mounted: function(){
        var scope = this;
        var workId = this.$route.params.id;

        workService.findById(workId, function(data){
            scope.work.name = data["art_name"];
            scope.work.description = data["art_detail"];
            scope.work.isActive = data["art_isopen"] == "Y" ? true : false;
            scope.work.status = data["art_status"] == "Y" ? true : false;
            scope.work.ownerId = data["art_mem"];
        });
    }
});