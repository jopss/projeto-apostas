appMain.service("Usuario", ["Restangular", "CONST", function (Restangular, CONST) {
                this.salvar = function (usuario) {
                        return Restangular.one(CONST.url + "api/usuario/").customPOST(usuario);
                };

                this.buscarTodos = function () {
                        return Restangular.one(CONST.url + "api/usuario/todos" +getParamCache()).get();
                };

                this.buscarPerfis = function () {
                        return Restangular.one(CONST.url + "api/usuario/perfis/todos" +getParamCache()).get();
                };

                this.buscarLogado = function () {
                        return Restangular.one(CONST.url + "api/usuario/logado" +getParamCache()).get();
                }
        }
]);
