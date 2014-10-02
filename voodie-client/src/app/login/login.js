angular.module('voodie.login').controller('LoginCtrl', ['$scope', 'User', '$rootScope',
    function($scope, User, $rootScope){
        $scope.login = {};
        $scope.submit = function(){
            if($scope.loginForm.$valid){
                User.username = $scope.login.username;
                User.password = $scope.login.password;
                var path = "elections";
                if($rootScope.captureRedirect){
                    path = $rootScope.captureRedirect;
                }
                User.login(path);
            }else{
                $scope.loginForm.submitted = true;
            }
        };
    }
]);
