angular.module('voodie.foodie.registration').controller('FoodieRegistrationCtrl', ['$scope', 'Voodie', '$rootScope', 'districts',
    function($scope, Voodie, $rootScope, districts){
        $scope.registration = {};
        $scope.districts = districts;
        $scope.submit = function(){
            if($scope.foodieRegistrationForm.$valid){
                var path = "elections";
                if($rootScope.captureRedirect){
                    path = $rootScope.captureRedirect;
                }
                Voodie.registerFoodie($scope.registration, path);
            }else{
                $scope.foodieRegistrationForm.submitted = true;
            }
        };
    }
]);
