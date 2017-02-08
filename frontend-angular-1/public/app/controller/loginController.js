//-------------------------------
// PARA QUEDA DE SESSAO COM AJAX
//-------------------------------
appMain.controller("LoginController", function ($window, Login, Seguranca, $location) {
        var ctrl = this;
        ctrl.login = {j_username: '', j_password: ''};

        ctrl.logar = function () {

                Login.logar(ctrl.login).then(function (result) {
                        if(result && result !=null){
                                if(!result.dado || result.dado==null){
                                        $("#div_mensagem_login").removeClass("hide");
                                        $("#div_mensagem_login").html(result.mensagens[0].valor);
                                }else{
                                        Seguranca.guardarSessao(result.dado);
                                        
                                        var modalLogin = $('#login_modal');
                                        if(modalLogin && modalLogin.is(':visible')){
                                                $('#login_modal').modal('hide');
                                        }

                                        if(globalRotaAnterior!=null){
                                                globalRotaAnterior.reload();
                                                globalRotaAnterior = null;
                                                $window.location.reload();
                                        }else{
                                                $window.location.href="/views/template.html#/dashboard/";
                                        }
                                        
                                }
                        }
                }, function (result) {
                        var msg = '';
                        if(result.data.dado == null){
                                msg = result.data.resposta.mensagens[0].valor;
                        }else{
                                msg = result.data.dado.mensagemDescricao ? result.data.dado.mensagemDescricao : result.data.dado.mensagemErro;
                        }

                        $("#div_mensagem_login").removeClass("hide");
                        $("#div_mensagem_login").html(msg);
                });
        };
});

function limparMensagensLogin(){
        $("#div_mensagem_login").html("");
        $("#div_mensagem_login").addClass("hide");
}
