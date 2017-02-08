appMain.service("Login", ["Restangular", "CONST", "$window", function (Restangular, CONST, $window) {
                this.logar = function (credentials) {
                        var dados = {
                            client_id: 'exemploaplicativocliente',
                            client_secret: '9834ba657bb2c60b5bb53de6f4201905',
                            username: credentials.j_username,
                            password: credentials.j_password,
                            grant_type: 'password'
                        };

                        return Restangular.one(CONST.url + "api/usuario/logar").customPOST(dados);
                };
                
                this.verificar = function(){
                        return Restangular.one(CONST.url + "api/usuario/verificar/").get();
                };

        }
]);