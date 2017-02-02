appMain.service("Login", ["Restangular", "CONST", function (Restangular, CONST) {
                this.logar = function (login) {
                        var data = 'j_username=' + login.j_username + '&j_password=' + login.j_password;
                        return Restangular.one(CONST.url + "j_spring_security_check")
                                .customPOST(
                                        data, null, null, {'Content-Type': "application/x-www-form-urlencoded; charset=UTF-8"}
                                );
                };
                
                this.verificar = function(){
                        return Restangular.one(CONST.url + "api/usuario/verificar/").get();
                };
        }
]);