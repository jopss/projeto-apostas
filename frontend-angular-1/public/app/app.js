var appMain = angular.module("app", ["ngRoute", "ngCookies", "ngResource", "restangular", "ui.bootstrap", "app.constants"]);
globalRotaAnterior = null;

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
