(function () {
        'use strict';

        appMain.directive('apostasMensagem', function () {
                return {
                        restrict: 'E',
                        scope: {
                                valor: "="
                        },
                        link: function (scope, element, attrs, controller) {
                                scope.$watch('valor', addMensagem, true);

                                function addMensagem(msg) {
                                        if (msg != null && ((msg.mensagens != null && msg.mensagens.length > 0) || (msg.mensagem != null && msg.mensagem != ''))) {
                                                if (msg.status == 403) {
                                                        addMsgInterna(scope, msg, "alert-warning", true);
                                                }
                                                else if (msg.status == 200) {
                                                        addMsgInterna(scope, msg, "alert-success", false);
                                                }
                                                else {
                                                        addMsgInterna(scope, msg, "alert-danger", false);
                                                }
                                        }
                                }
                                ;
                        },
                        template:
                                '<div id="div_mensagem" class="hide">' +
                                '{{mensagem}}' +
                                '</div>',
                        replace: true
                };
        });

        /**
         * FUNCOES INTERNAS DA DIRECTIVA
         */
        function addMsgInterna(scope, msg, alertcss, chaveValor) {
                scope.mensagem = '';

                $("#div_mensagem").removeClass();
                $("#div_mensagem").addClass("alert " + alertcss);
                var arr = msg.mensagens;
                var txt = msg.mensagem;

                if (arr != null && arr.length > 0) {
                        jQuery.each(arr, function (i, msg) {
                                if (chaveValor) {
                                        scope.mensagem = scope.mensagem + " " + msg.chave + " - " + msg.valor;
                                } else {
                                        scope.mensagem = scope.mensagem + " " + msg.valor;
                                }
                        });
                } else {
                        scope.mensagem = txt;
                }
        }
})();

/**
 * FUNCOES EXTERNAS
 */
function addMensagemValidacao(ctrl, mensagem) {
        limparMensagens(ctrl);
        ctrl.mensagem.status = 403;
        ctrl.mensagem.mensagem = mensagem;
}

function addMensagemRetorno(ctrl, result) {
        limparMensagens(ctrl);
        ctrl.mensagem.status = result.status;
        if (result.status == 404) {
                ctrl.mensagem.mensagem = "404 - Recurso nao encontrado. Verifique com o RH.";
        }
        else {
                if (result.status == null) {
                        if(result.mensagens!=null){
                                ctrl.mensagem.status = 200;
                                ctrl.mensagem.mensagens = result.mensagens;
                        }else{
                                ctrl.mensagem.status = 401;
                                ctrl.mensagem.mensagem = result;
                        }
                } else {
                        ctrl.mensagem.mensagens = result.data.mensagens;
                }
        }
}

function limparMensagens(ctrl) {
        ctrl.mensagem = {status: '', mensagem: '', mensagens: []};
}