appMain.controller("AppController", function ($location, $rootScope, Seguranca) {
        var ctrl = this;
        
        ctrl.init = function () {
                $location.path("/dashboard/");
        };

        ctrl.logout = function(){
        	Seguranca.logout();
        }
        
        ctrl.init();
});

