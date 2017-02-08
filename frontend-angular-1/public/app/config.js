appMain.run(['$http', 'Seguranca', function ($http, Seguranca) {
        if(Seguranca.getToken() && Seguranca.getToken()!=null){
                $http.defaults.headers.common['Authorization'] = 'Bearer'+Seguranca.getToken();
        }
}]);

appMain.run(['Restangular', '$route', function (Restangular, $route) {
        Restangular.setErrorInterceptor(function(response, deferred, responseHandler) {
            if(response.status === 403) {
                var modalAberto = $('#login_modal').is(':visible');
                if(!modalAberto){
                        // Repeat the request and then call the handlers the usual way.
                        // $http(response.config).then(responseHandler, deferred.reject);

                        globalRotaAnterior = $route;
                        abrirLogin();
                        return false;
                }
            }
            else if(response.status === 401) {
                alert('Erro 401 - Permissao Negada\nRecurso: '+response.config.url);
            }
            return true;
        });
}]);

appMain.run(function ($rootScope, $templateCache, $route, Login) {
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
                //a toda rota verifica sessao expirada e permissoes
                Login.verificar()
                        .then(function (result) {
                                if(result != null){
                                        if(result.perfil != null){
                                                $rootScope.permissoes = new Array();
                                                jQuery.each(result.perfil.permissoes, function (i, permissao) {
                                                        $rootScope.permissoes.push(permissao.papel);
                                                });
                                        }
                                        
                                }else{
                                        alert("Servidor não responde. Tente novamente.");
                                }
                        });
        });
});
