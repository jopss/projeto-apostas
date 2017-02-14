appMain.service("Aposta", ["Restangular", "CONST", function (Restangular, CONST) {
                this.buscarPaginaAtual = function (apostaForm) {
                        return Restangular.one(CONST.url + "api/aposta/pagina/" +getParamCache()).customPOST(apostaForm);
                };
                this.salvar = function (aposta) {
                        return Restangular.one(CONST.url + "api/aposta/").customPOST(aposta);
                };
                this.editar = function (id) {
                        return Restangular.one(CONST.url + "api/aposta", id).get();
                };
                this.deletar = function (id) {
                        return Restangular.one(CONST.url + "api/aposta", id).remove();
                };
        }
]);