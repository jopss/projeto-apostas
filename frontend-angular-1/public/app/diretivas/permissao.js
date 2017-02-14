(function () {
        'use strict';

        appMain.directive('apostaPermissao', function ($rootScope) {
                return {
                        restrict: 'E',
                        scope: {
                                id: "=",
                                papeis: "=",
                                mostrarMensagem: "=",
                                mensagem: "=",
                                substituir: "="
                        },
                        link: function (scope, element, attrs, controller) {
                                $rootScope.$watch(function(){
                                        var obj = {};
                                        obj.id = attrs.id;
                                        obj.papeis = attrs.papeis;
                                        obj.mostrarMensagem = attrs.mostrarMensagem;
                                        obj.mensagem = attrs.mensagem;
                                        obj.substituir = attrs.substituir;
                                        obj.permissoes = $rootScope.permissoes;
                                        return obj;
                                }, verificarPapel, true);

                                function verificarPapel(objValor) {
                                        if(angular.isUndefined(objValor.substituir)){
                                                objValor.substituir = false;
                                        }

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
                                                        abrirOuEsconderConteudo($("#"+objValor.id), false, objValor.substituir);
                                                }
                                        }else{
                                                abrirOuEsconderConteudo($("#"+objValor.id), true, objValor.substituir);
                                        }
                                }
                        },
                        template:
                                '<div ng-transclude></div>',
                        replace: true,
                        transclude: true
                };
        });

        function abrirOuEsconderConteudo(jcomp, abrir, substituir) {
                if (angular.isDefined(jcomp)) {
                        var par = jcomp.closest("li");
                        if(!abrir){
                                jcomp.hide();
                        }else{
                                if (par!= null && substituir) {
                                        //somente para menu lateral.
                                        //retira o '<div ng-transclude>' em volta do conteudo, colocando dentro da tag original. Sem isso quebra layout.
                                        par.html(jcomp.html());
                                        if (abrir) {
                                                par.show();
                                        } else {
                                                par.hide();
                                        }
                                        jcomp.remove();
                                }else{
                                        jcomp.show();
                                }
                        }
                }
        }
})();