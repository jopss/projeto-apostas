appMain.controller("AppController", function ($location, $rootScope) {
        var ctrl = this;
        
        ctrl.init = function () {
                $location.path("/dashboard/");
        };
        
        ctrl.init();
});

