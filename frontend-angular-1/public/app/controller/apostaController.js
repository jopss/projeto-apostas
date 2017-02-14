(function () {
        'use strict';

        appMain.controller("ApostaController", function ($routeParams, Aposta, Usuario) {
                var ctrl = this;
                limparTela(ctrl);

                ctrl.adicionarPalpite = function () {
                        if (ctrl.palpite.descricao != '' && ctrl.palpite.usuario != '') {
                                ctrl.aposta.palpites.push(ctrl.palpite);
                                ctrl.palpite = {descricao: '', usuario: ''};
                        } else {
                                addMensagemValidacao(ctrl, "Verifique os campos obrigatórios: Usuário e Palpite");
                        }
                };

                ctrl.removerPalpite = function (idx) {
                        ctrl.aposta.palpites.splice(idx, 1);
                };

                ctrl.declararVencedor = function (idx) {
                        ctrl.aposta.palpites[idx].venceu = true;
                };

                ctrl.desfazerVencedor = function (idx) {
                        ctrl.aposta.palpites[idx].venceu = false;
                };

                ctrl.apostaFinalizada = function () {
                        if (ctrl.aposta.dateFinalizacao != null) {
                                return ctrl.aposta.id && new Date() > moment(ctrl.aposta.dateFinalizacao, "DD/MM/YYYY").toDate();
                        }
                        return false;
                };

                ctrl.salvar = function () {
                        if (ctrl.aposta.palpites.length == 0) {
                                addMensagemValidacao(ctrl, "Adicione pelo menos um palpite");
                        } else {
                                Aposta.salvar(ctrl.aposta).then(function (result) {
                                        addMensagemRetorno(ctrl, result);
                                        limparTela(ctrl);
                                        ctrl.init();
                                }, function (result) {
                                        addMensagemRetorno(ctrl, result);
                                });
                        }
                };

                ctrl.init = function () {
                        var id = $routeParams.id;
                        if (angular.isDefined(id)) {
                                Aposta.editar(id).then(function (result) {
                                        ctrl.aposta = result.dado;
                                }, function (result) {
                                        addMensagemRetorno(ctrl, result);
                                        limparTela(ctrl);
                                });
                        }
                        //carrega a combo de usuarios utilizadas no cadastro de apostas
                        Usuario.buscarTodos().then(function (result) {
                                ctrl.usuarios = result.lista;
                        }, function (result) {
                                addMensagemRetorno(ctrl, result);
                                limparTela(ctrl);
                        });
                };
                ctrl.init();
                limparMensagens(ctrl);
        });

        function limparTela(ctrl) {
                ctrl.aposta = {descricao: '', dateFinalizacao: null, palpites: []};
                ctrl.palpite = {descricao: '', usuario: ''};
                ctrl.usuarios = {};
        }
})();