(function () {
        'use strict';

        appMain.controller("UsuarioController", function ($routeParams, Usuario) {
                var ctrl = this;
                limparTela(ctrl);

                ctrl.salvar = function () {
                        Usuario.salvar(ctrl.usuario).then(function (result) {
                                addMensagemRetorno(ctrl, result);
                                limparTela(ctrl);
                        }, function (result) {
                                addMensagemRetorno(ctrl, result);
                        });
                };

                ctrl.init = function (editar) {

                        Usuario.buscarPerfis().then(function (result) {
                                ctrl.perfis = result.lista;
                        }, function (result) {
                                addMensagemRetorno(ctrl, result);
                                limparTela(ctrl);
                        });

                        if(editar){
                                Usuario.buscarLogado().then(function (result) {
                                        ctrl.usuario = result.dado;
                                }, function (result) {
                                        addMensagemRetorno(ctrl, result);
                                        limparTela(ctrl);
                                });
                        }
                }
                        
                var acao = $routeParams.acao;
                if (acao == 'buscar') {
                        ctrl.init(true);
                }else{
                        ctrl.init(false);
                }
                limparMensagens(ctrl);
        });

        function limparTela(ctrl) {
                ctrl.usuario = {nome: '', login: '', senha: ''};
        }
})();