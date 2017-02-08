appMain.service("Seguranca", ["$window", "$location", function ($window, $location) {
                
                this.guardarSessao = function(loginForm){
                        $window.localStorage['acesso'] = JSON.stringify(loginForm);
                };

                this.logout = function () {
                        $window.localStorage.clear();
                        window.location.href="/views/login.html"
                        $location.replace();
                };

                this.getLogado = function () {
                        var logado = $window.localStorage['acesso'];
                        if(logado && (JSON.parse(logado) && JSON.parse(logado).token)){
                            return JSON.parse(logado);
                        }
                        return {};
                };

                this.getToken = function () {
                        var logado = this.getLogado();
                        if(logado){
                            return ' '+logado.token;
                        }
                        return null;
                };

        }
]);