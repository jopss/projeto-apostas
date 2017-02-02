(function () {
        'use strict';

        appMain.directive('apostaPermissao', function ($rootScope) {
                return {
                        restrict: 'E',
                        scope: {
                                id: "=",
                                papeis: "=",
                                mostrarMensagem: "=",
                                mensagem: "="
                        },
                        link: function (scope, element, attrs, controller) {
                                $rootScope.$watch(function(){
                                        var obj = {};
                                        obj.id = attrs.id;
                                        obj.papeis = attrs.papeis;
                                        obj.mostrarMensagem = attrs.mostrarMensagem;
                                        obj.mensagem = attrs.mensagem;
                                        obj.permissoes = $rootScope.permissoes;
                                        return obj;
                                }, verificarPapel, true);

                                function verificarPapel(objValor) {
                                        var permitido = false;
                                        if(objValor.permissoes!=null){
                                                jQuery.each(objValor.permissoes, function (i, rootPapel) {
                                                        if(rootPapel == objValor.papeis){
                                                                permitido = true;
                                                        }
                                                });
                                        }
                                        
                                        if(!permitido){
                                                if(objValor.mostrarMensagem != null && objValor.mostrarMensagem == 'true'){
                                                        var msg = objValor.mensagem;
                                                        if(msg == null){
                                                                msg = "Você não possui permissão para acessar esta funcionalidade.";
                                                        }
                                                        $("#"+objValor.id).html(msg);
                                                }else{
                                                        $("#"+objValor.id).hide();
                                                }
                                        }else{
                                                $("#"+objValor.id).show();
                                        }
                                }
                        },
                        template:
                                '<div ng-transclude></div>',
                        replace: true,
                        transclude: true
                };
        });
})();