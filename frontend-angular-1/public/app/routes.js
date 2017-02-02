appMain.config(function (RestangularProvider, $routeProvider, $httpProvider) {
        
        //para expiramento de sessao por ajax dos dados.
        RestangularProvider.setErrorInterceptor(function (response, deferred, responseHandler) {
                if (response.status === 401) {
                        return abrirLogin();
                }
                return true;
        });

        //PRA INDICAR AO SERVER QUE EH UMA REQUEST DO ANGULAR/AJAX
        RestangularProvider.setDefaultHeaders({'Content-Type': 'application/json'}); //para o services
        $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
        $httpProvider.defaults.cache = false;
        
        $routeProvider.when("/dashboard/", {
                templateUrl:"/views/home.html",
                controller: "DashboardController"
        });
        $routeProvider.when("/usuario/", {
                templateUrl:"/views/usuario/cadastro.html",
                controller: "UsuarioController"
        });
        $routeProvider.when("/usuario/:acao", {
                templateUrl:"/views/usuario/cadastro.html",
                controller: "UsuarioController"
        });
        $routeProvider.when("/aposta/", {
                templateUrl:"/views/aposta/cadastro.html",
                controller: "ApostaController"
        });
        $routeProvider.when("/aposta/:id", {
                templateUrl:"/views/aposta/cadastro.html",
                controller: "ApostaController"
        });
});
