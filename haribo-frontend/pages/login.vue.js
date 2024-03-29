var loginView = Vue.component('LoginView', {
    template: `
        <div class="container">
            <div class="row">
                <div id="login-form" class="col-md-6 mx-auto bg-white">
                    <router-link to="/">Auction | HARIBO</router-link>
                    <div class="mt-4">
                        <div class="form-group">
                            <label for="email">이메일</label>
                            <input type="text" class="form-control" id="email" v-model="user.email" placeholder="이메일">
                        </div>
                        <div class="form-group">
                            <label for="password">비밀번호</label>
                            <input type="password" class="form-control" id="password" v-model="user.password" placeholder="비밀번호">
                        </div>
                        <button type="submit" class="btn btn-primary" v-on:click="login">로그인</button>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            user: {
                email: '',
                password: '',
            }
        }
    },
    methods: {
        login: function() {
            var scope = this;

            userService.login(
                this.user.email,
                this.user.password,
                function(data){
                    store.state.isSigned = true;
                    store.state.user.id = data.mem_id;

                    walletService.findById(store.state.user.id, function(response){
                        if(response === undefined) {
                            store.state.user.hasWallet = false;
                        } else {
                            store.state.user.hasWallet = true;
                        } 
                    });

                    scope.$router.push('/');
                },
                function(error){
                    alert("유저 이메일 혹은 비밀번호가 일치하지 않습니다.");
                }
            );
        }
    }
})