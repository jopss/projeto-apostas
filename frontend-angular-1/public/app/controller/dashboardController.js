appMain.controller("DashboardController", function ($scope, $location, Aposta, CONST) {
        var ctrl = this;
        
        ctrl.form = {paginaAtual: '', dataInicial: null, dataFinal: null, descricao: '', quantidadeRegistro: CONST.QTDREGISTROPAGINACAO, totalRegistro: ''};
        ctrl.deletar = function (id) {
                Aposta.deletar(id).then(function (result) {
                        addMensagemRetorno(ctrl, result);
                        ctrl.buscar();
                }, function (result) {
                        addMensagemRetorno(ctrl, result);
                });
        };

        ctrl.editar = function (id) {
                $location.path("/aposta/").search({id: id});
        };

        ctrl.buscar = function () {
                Aposta.buscarPaginaAtual(ctrl.form).then(function (result) {
                        ctrl.apostas = result.lista;
                        ctrl.totalItems = result.dado.totalRegistros;
                }, function (result) {
                        addMensagemRetorno(ctrl, result);
                });

        }

        $scope.$watch('ctrl.form.paginaAtual', function () {
                ctrl.buscar();
        });

        ctrl.init = function () {
                ctrl.form.paginaAtual = 1;
                ctrl.buscar();
        }

        ctrl.init();
});


