var appMain = angular.module("app", ["ngRoute", "ngCookies", "ngResource", "restangular", "ui.bootstrap", "app.constants"]);

appMain.run(function ($rootScope, $templateCache, $route, Login) {
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
                //a toda rota verifica sessao expirada e permissoes
                Login.verificar().then(function (result) {
                        if(result != null){
                                if(result.mensagens[0].mensagem=='NOK'){
                                        //guarda a rota anterior para pegar os dados novamente em caso de sucesso no modal de login.
                                        globalRotaAnterior = $route;
                                        return abrirLogin();
                                }else{
                                        if(result.perfil != null){
                                                $rootScope.permissoes = new Array();
                                                jQuery.each(result.perfil.permissoes, function (i, permissao) {
                                                        $rootScope.permissoes.push(permissao.papel);
                                                });
                                        }
                                }
                        }else{
                                alert("Servidor não responde. Tente novamente.");
                        }
                });
        });
});

function getParamCache() {
        return "?" + new Date().getTime();
}

function abrirLogin(){
        limparMensagensLogin();
        $('#login_modal').modal({
                keyboard: false,
                backdrop: 'static'
        });
        return false;
}
