//-------------------------------
// PARA QUEDA DE SESSAO COM AJAX
//-------------------------------
appMain.controller("LoginController", function (Login) {
        var ctrl = this;
        ctrl.login = {j_username: '', j_password: ''};

        ctrl.logar = function () {
                Login.logar(ctrl.login).then(function (result) {
                        if(result !=null){
                                //o retorno podera ser uma pagina de login com a mensagem setada pelo spring sec, ou a pagina de sucesso.
                                //tenta pegar a mensagem de erro.
                                var msgSecurity = $($.parseHTML( result )).find("#msg_security");
                                
                                //caso contenha mensagem de erro vindo do servidor, exibe no modal.
                                if(msgSecurity!=null && msgSecurity.length > 0){
                                        var msg = msgSecurity.text();
                                        if(msg!=null && msg!=''){
                                                $("#div_mensagem_login").removeClass("hide");
                                                $("#div_mensagem_login").html(msg);
                                                return;
                                        }
                                }
                        }
                        //caso deu certo o novo login, refaz a ultima rota, para buscar os dados que foram bloqueados (como combos e tabelas).
                        if(globalRotaAnterior!=null){
                                globalRotaAnterior.reload();
                                globalRotaAnterior = null;
                        }
                        $('#login_modal').modal('hide');
                }, function (result) {
                        $("#div_mensagem_login").removeClass("hide");
                        $("#div_mensagem_login").html(result);
                });
        };
});

function limparMensagensLogin(){
        $("#div_mensagem_login").html("");
        $("#div_mensagem_login").addClass("hide");
}
